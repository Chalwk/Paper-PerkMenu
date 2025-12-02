package com.chalwk.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class PerkConfig {
    private final Map<String, Object> settings = new HashMap<>();
    private final Map<String, List<String>> categories = new HashMap<>();
    private final Map<String, Map<String, Object>> perks = new HashMap<>();

    public void loadFromConfig(org.bukkit.configuration.ConfigurationSection config) {
        // GUI Settings
        settings.put("gui.rows", config.getInt("gui.rows", 6));
        settings.put("gui.title", config.getString("gui.title", "&6&lPerk Menu"));
        settings.put("gui.fill_empty", config.getString("gui.fill_empty", "GRAY_STAINED_GLASS_PANE"));
        settings.put("gui.fill_empty_name", config.getString("gui.fill_empty_name", "&7"));

        // Navigation Items
        settings.put("navigation.previous.material", config.getString("navigation.previous.material", "ARROW"));
        settings.put("navigation.previous.name", config.getString("navigation.previous.name", "&aPrevious Page"));
        settings.put("navigation.previous.slot", config.getInt("navigation.previous.slot", 45));

        settings.put("navigation.next.material", config.getString("navigation.next.material", "ARROW"));
        settings.put("navigation.next.name", config.getString("navigation.next.name", "&aNext Page"));
        settings.put("navigation.next.slot", config.getInt("navigation.next.slot", 53));

        settings.put("navigation.back.material", config.getString("navigation.back.material", "BARRIER"));
        settings.put("navigation.back.name", config.getString("navigation.back.name", "&cBack to Categories"));
        settings.put("navigation.back.slot", config.getInt("navigation.back.slot", 49));

        // Load Categories
        ConfigurationSection categoriesSection = config.getConfigurationSection("categories");
        if (categoriesSection != null) {
            for (String category : categoriesSection.getKeys(false)) {
                ConfigurationSection categorySection = categoriesSection.getConfigurationSection(category);
                if (categorySection != null) {
                    List<String> categoryData = new ArrayList<>();
                    categoryData.add(categorySection.getString("display_name", category));
                    categoryData.add(categorySection.getString("icon", "CHEST"));
                    categoryData.add(categorySection.getString("description", ""));
                    categoryData.add(String.valueOf(categorySection.getInt("slot", -1)));
                    categoryData.add(categorySection.getString("permission", "perkmenu.category." + category));
                    categories.put(category, categoryData);
                }
            }
        }

        // Load Perks
        ConfigurationSection perksSection = config.getConfigurationSection("perks");
        if (perksSection != null) {
            for (String perkId : perksSection.getKeys(false)) {
                ConfigurationSection perkSection = perksSection.getConfigurationSection(perkId);
                if (perkSection != null) {
                    Map<String, Object> perkData = new HashMap<>();
                    perkData.put("category", perkSection.getString("category", "general"));

                    Object permissionObj = perkSection.get("permission");
                    if (permissionObj instanceof List) {
                        perkData.put("permission", permissionObj);
                    } else {
                        String permission = perkSection.getString("permission", "perkmenu.perk." + perkId);
                        perkData.put("permission", Collections.singletonList(permission));
                    }

                    perkData.put("display_name", perkSection.getString("display_name", perkId));
                    perkData.put("description", perkSection.getString("description", ""));
                    perkData.put("icon", perkSection.getString("icon", "PAPER"));
                    perkData.put("material", Material.valueOf(perkSection.getString("icon", "PAPER")));
                    perkData.put("enabled", perkSection.getBoolean("enabled", true));

                    if (perkSection.contains("action")) {
                        perkData.put("action_type", perkSection.getString("action.type", "message"));
                        perkData.put("action_value", perkSection.getString("action.value", ""));
                        perkData.put("action_sound", perkSection.getString("action.sound", "BLOCK_NOTE_BLOCK_PLING"));
                    }
                    perkData.put("cost", perkSection.getInt("cost", 0));
                    perks.put(perkId, perkData);
                }
            }
        }
    }

    public int getGUIRows() {
        return (int) settings.get("gui.rows");
    }

    public String getGUITitle() {
        return (String) settings.get("gui.title");
    }

    public String getFillEmptyMaterial() {
        return (String) settings.get("gui.fill_empty");
    }

    public String getFillEmptyName() {
        return (String) settings.get("gui.fill_empty_name");
    }

    public Map<String, List<String>> getCategories() {
        return Collections.unmodifiableMap(categories);
    }

    public Map<String, Map<String, Object>> getPerks() {
        return Collections.unmodifiableMap(perks);
    }

    public List<Map<String, Object>> getPerksByCategory(String category) {
        List<Map<String, Object>> categoryPerks = new ArrayList<>();
        for (Map.Entry<String, Map<String, Object>> entry : perks.entrySet()) {
            if (entry.getValue().get("category").equals(category)) {
                categoryPerks.add(entry.getValue());
            }
        }
        return categoryPerks;
    }

    public Map<String, Object> getPerk(String perkId) {
        return perks.get(perkId);
    }

    public String getNavigationMaterial(String type) {
        return (String) settings.get("navigation." + type + ".material");
    }

    public String getNavigationName(String type) {
        return (String) settings.get("navigation." + type + ".name");
    }

    public int getNavigationSlot(String type) {
        return (int) settings.get("navigation." + type + ".slot");
    }
}