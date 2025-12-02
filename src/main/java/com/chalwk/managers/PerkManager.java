package com.chalwk.managers;

import com.chalwk.PerkMenu;
import com.chalwk.config.PerkConfig;
import com.chalwk.util.MessageHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PerkManager {
    private final PerkMenu plugin;
    private final Map<UUID, Integer> openPages = new HashMap<>();
    private final Map<UUID, String> viewingCategory = new HashMap<>();

    public PerkManager(PerkMenu plugin) {
        this.plugin = plugin;
    }

    public void openCategoryMenu(Player player) {
        PerkConfig config = plugin.getConfigManager().getConfig();
        int rows = config.getGUIRows();
        String title = config.getGUITitle();

        Inventory gui = Bukkit.createInventory(null, rows * 9,
                LegacyComponentSerializer.legacyAmpersand().deserialize(title));

        Material fillMaterial = Material.valueOf(config.getFillEmptyMaterial());
        ItemStack fillItem = createItem(fillMaterial, config.getFillEmptyName(), new ArrayList<>());

        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, fillItem);
        }

        Map<String, List<String>> categories = config.getCategories();
        int slot = 10;

        for (Map.Entry<String, List<String>> entry : categories.entrySet()) {
            List<String> categoryData = entry.getValue();

            String permission = categoryData.get(4);
            if (!player.hasPermission(permission) && !permission.isEmpty()) continue;

            Material material = Material.valueOf(categoryData.get(1));
            String displayName = categoryData.get(0);
            String description = categoryData.get(2);
            int customSlot = Integer.parseInt(categoryData.get(3));

            if (customSlot != -1) slot = customSlot;

            List<String> lore = new ArrayList<>();
            if (!description.isEmpty()) {
                lore.add("&7" + description);
            }
            lore.add("");
            lore.add("&eClick to view perks!");

            ItemStack categoryItem = createItem(material, displayName, lore);
            gui.setItem(slot, categoryItem);

            slot++;
            if (slot % 9 == 8) {
                slot += 2;
            }
            if (slot >= gui.getSize() - 9) {
                break;
            }
        }

        player.openInventory(gui);
        openPages.put(player.getUniqueId(), 1);
        viewingCategory.remove(player.getUniqueId());
    }

    public void openPerkMenu(Player player, String category, int page) {
        PerkConfig config = plugin.getConfigManager().getConfig();
        List<Map<String, Object>> perks = config.getPerksByCategory(category);

        if (perks.isEmpty()) {
            MessageHelper.sendMessage(player, "&cNo perks found in this category!");
            return;
        }

        int rows = config.getGUIRows();
        String title = config.getGUITitle() + " &8- &f" + category;
        int itemsPerPage = (rows - 2) * 7;

        int startIndex = (page - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, perks.size());

        if (startIndex >= perks.size()) {
            page = 1;
            startIndex = 0;
            endIndex = Math.min(itemsPerPage, perks.size());
        }

        Inventory gui = Bukkit.createInventory(null, rows * 9,
                LegacyComponentSerializer.legacyAmpersand().deserialize(title));

        Material fillMaterial = Material.valueOf(config.getFillEmptyMaterial());
        ItemStack fillItem = createItem(fillMaterial, config.getFillEmptyName(), new ArrayList<>());

        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, fillItem);
        }

        int slot = 10;
        for (int i = startIndex; i < endIndex; i++) {
            Map<String, Object> perkData = perks.get(i);

            Object permissionObj = perkData.get("permission");
            boolean hasPermission = false;

            if (permissionObj instanceof List<?>) {
                hasPermission = true;
                for (Object obj : (List<?>) permissionObj) {
                    if (!(obj instanceof String) || !player.hasPermission((String) obj)) {
                        hasPermission = false;
                        break;
                    }
                }
            } else if (permissionObj instanceof String) {
                hasPermission = player.hasPermission((String) permissionObj);
            }

            if (!hasPermission) continue;

            Material material = (Material) perkData.get("material");
            String displayName = (String) perkData.get("display_name");
            String description = (String) perkData.get("description");
            boolean enabled = (boolean) perkData.get("enabled");

            List<String> lore = new ArrayList<>();
            if (!description.isEmpty()) {
                lore.add("&7" + description);
            }

            int cost = (int) perkData.get("cost");
            if (cost > 0) {
                lore.add("&6Cost: &f$" + cost);
            }

            // Show required permissions if multiple
            if (permissionObj instanceof List<?> permList) {
                if (permList.size() > 1) {
                    lore.add("&6Required Permissions:");
                    for (Object obj : permList) {
                        if (obj instanceof String) {
                            lore.add("&7- &e" + obj);
                        }
                    }
                    lore.add("");
                }
            }

            lore.add("");
            if (enabled) {
                lore.add("&a✓ Enabled");
            } else {
                lore.add("&c✗ Disabled");
            }

            if (perkData.containsKey("action_type")) {
                lore.add("&eClick to activate!");
            }

            ItemStack perkItem = createItem(material, displayName, lore);
            gui.setItem(slot, perkItem);

            slot++;
            if (slot % 9 == 8) slot += 2;
            if (slot >= gui.getSize() - 9) break;
        }

        if (page > 1) {
            Material prevMaterial = Material.valueOf(config.getNavigationMaterial("previous"));
            ItemStack prevItem = createItem(prevMaterial, config.getNavigationName("previous"),
                    List.of("&7Go to previous page"));
            gui.setItem(config.getNavigationSlot("previous"), prevItem);
        }

        if (endIndex < perks.size()) {
            Material nextMaterial = Material.valueOf(config.getNavigationMaterial("next"));
            ItemStack nextItem = createItem(nextMaterial, config.getNavigationName("next"),
                    List.of("&7Go to next page"));
            gui.setItem(config.getNavigationSlot("next"), nextItem);
        }

        Material backMaterial = Material.valueOf(config.getNavigationMaterial("back"));
        ItemStack backItem = createItem(backMaterial, config.getNavigationName("back"),
                List.of("&7Return to category selection"));
        gui.setItem(config.getNavigationSlot("back"), backItem);

        player.openInventory(gui);
        openPages.put(player.getUniqueId(), page);
        viewingCategory.put(player.getUniqueId(), category);
    }

    private ItemStack createItem(Material material, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (displayName != null && !displayName.isEmpty()) {
                meta.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize(displayName));
            }

            if (lore != null && !lore.isEmpty()) {
                List<Component> loreComponents = new ArrayList<>();
                for (String line : lore) {
                    loreComponents.add(LegacyComponentSerializer.legacyAmpersand().deserialize(line));
                }
                meta.lore(loreComponents);
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public void handlePerkClick(Player player, String perkId) {
        PerkConfig config = plugin.getConfigManager().getConfig();
        Map<String, Object> perkData = config.getPerk(perkId);

        if (perkData == null) return;

        Object permissionObj = perkData.get("permission");
        boolean hasPermission = false;

        if (permissionObj instanceof List<?>) {
            hasPermission = true;
            for (Object obj : (List<?>) permissionObj) {
                if (!(obj instanceof String) || !player.hasPermission((String) obj)) {
                    hasPermission = false;
                    break;
                }
            }
        } else if (permissionObj instanceof String) {
            hasPermission = player.hasPermission((String) permissionObj);
        }

        if (!hasPermission) {
            MessageHelper.sendMessage(player, "&cYou don't have permission for this perk!");
            return;
        }

        boolean enabled = (boolean) perkData.get("enabled");
        if (!enabled) {
            MessageHelper.sendMessage(player, "&cThis perk is currently disabled!");
            return;
        }

        if (perkData.containsKey("action_type")) {
            String actionType = (String) perkData.get("action_type");
            String actionValue = (String) perkData.get("action_value");
            String soundType = (String) perkData.get("action_sound");

            switch (actionType.toLowerCase()) {
                case "command":
                    String command = actionValue.replace("{player}", player.getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    MessageHelper.sendMessage(player, "&aPerk activated!");
                    break;
                case "message":
                    MessageHelper.sendMessage(player, actionValue);
                    break;
                case "sound":
                    try {
                        Sound sound = Sound.valueOf(soundType);
                        player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().warning("Invalid sound type: " + soundType);
                    }
                    break;
            }
        } else {
            MessageHelper.sendMessage(player, "&eThis perk doesn't have an action configured.");
        }

        player.closeInventory();
    }

    public int getCurrentPage(UUID playerId) {
        return openPages.getOrDefault(playerId, 1);
    }

    public String getViewingCategory(UUID playerId) {
        return viewingCategory.get(playerId);
    }
}