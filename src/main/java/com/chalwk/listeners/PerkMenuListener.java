package com.chalwk.listeners;

import com.chalwk.managers.PerkManager;
import com.chalwk.PerkMenu;
import com.chalwk.config.PerkConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record PerkMenuListener(PerkMenu plugin) implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        // Get inventory title
        Component titleComponent = event.getView().title();
        String title = LegacyComponentSerializer.legacySection().serialize(titleComponent);

        if (!title.contains("Perk Menu")) return;

        event.setCancelled(true);

        ItemMeta meta = clickedItem.getItemMeta();
        if (meta == null) return;

        Component displayNameComponent = meta.displayName();
        if (displayNameComponent == null) return;

        String displayName = LegacyComponentSerializer.legacySection().serialize(displayNameComponent);
        UUID playerId = player.getUniqueId();
        PerkManager perkManager = plugin.getPerkManager();
        PerkConfig config = plugin.getConfigManager().getConfig();

        // Check if it's a navigation item
        String backName = config.getNavigationName("back");
        String prevName = config.getNavigationName("previous");
        String nextName = config.getNavigationName("next");

        if (displayName.contains(backName)) {
            perkManager.openCategoryMenu(player);
            return;
        }

        if (displayName.contains(prevName)) {
            String category = perkManager.getViewingCategory(playerId);
            int currentPage = perkManager.getCurrentPage(playerId);
            perkManager.openPerkMenu(player, category, currentPage - 1);
            return;
        }

        if (displayName.contains(nextName)) {
            String category = perkManager.getViewingCategory(playerId);
            int currentPage = perkManager.getCurrentPage(playerId);
            perkManager.openPerkMenu(player, category, currentPage + 1);
            return;
        }

        // Check if it's a category item
        Map<String, List<String>> categories = config.getCategories();
        for (Map.Entry<String, List<String>> entry : categories.entrySet()) {
            String categoryName = entry.getValue().getFirst();
            if (displayName.contains(stripColor(categoryName))) {
                perkManager.openPerkMenu(player, entry.getKey(), 1);
                return;
            }
        }

        // Check if it's a perk item
        Map<String, Map<String, Object>> perks = config.getPerks();
        for (Map.Entry<String, Map<String, Object>> entry : perks.entrySet()) {
            String perkName = (String) entry.getValue().get("display_name");
            if (displayName.contains(stripColor(perkName))) {
                perkManager.handlePerkClick(player, entry.getKey());
                return;
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player) {
            plugin.getPerkManager().getViewingCategory(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getPerkManager().getViewingCategory(player.getUniqueId());
    }

    private String stripColor(String text) {
        return text.replaceAll("&[0-9a-fk-or]", "").replaceAll("§[0-9a-fk-or]", "");
    }
}