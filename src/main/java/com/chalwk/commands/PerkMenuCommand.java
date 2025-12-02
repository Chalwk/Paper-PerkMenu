package com.chalwk.commands;

import com.chalwk.PerkMenu;
import com.chalwk.util.MessageHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record PerkMenuCommand(PerkMenu plugin) implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            MessageHelper.sendMessage(sender, "&cOnly players can use this command!");
            return true;
        }

        if (!player.hasPermission("perkmenu.use")) {
            MessageHelper.sendMessage(sender, "&cYou don't have permission to view perks!");
            return true;
        }

        if (args.length == 0) {
            plugin.getPerkManager().openCategoryMenu(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload") && player.hasPermission("perkmenu.admin")) {
            plugin.reload();
            MessageHelper.sendMessage(sender, "&aConfiguration reloaded!");
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            MessageHelper.sendMessage(sender, "&6&lPerkMenu Help");
            MessageHelper.sendMessage(sender, "&e/perks &7- Open the perk menu");
            if (player.hasPermission("perkmenu.admin")) {
                MessageHelper.sendMessage(sender, "&e/perks reload &7- Reload the configuration");
            }
            return true;
        }

        MessageHelper.sendMessage(sender, "&cUsage: /perks");
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender,
                                      @NotNull Command command,
                                      @NotNull String label,
                                      @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String partial = args[0].toLowerCase();

            if ("help".startsWith(partial)) completions.add("help");

            if (sender.hasPermission("perkmenu.admin")) {
                if ("reload".startsWith(partial)) {
                    completions.add("reload");
                }
            }
        }

        return completions;
    }
}