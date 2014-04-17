/*
 * Portable Crafting Table - a bukkit plugin
 * Copyright (C) 2014 eq.tolbin@gmail.com

* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.gmail.tolbin.eq.pct;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PortableCraftingTable extends JavaPlugin implements Listener {

    public Permission pctuse = new Permission("pct.use");

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        PluginManager pm = getServer().getPluginManager();
        pm.addPermission(pctuse);
        getConfig().options().header("Portable Crafting config file");
        getConfig().options().copyHeader(true);
        saveConfig();
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void test(InventoryClickEvent e) {
        if (e.isRightClick() && (e.getCurrentItem().getType() == Material.WORKBENCH)) {
            Player player = (Player) e.getWhoClicked();
            if (getConfig().getBoolean(player.getName(), false) && player.hasPermission("pct.use")) {
                e.setCancelled(true);
                player.sendMessage(ChatColor.GREEN + "Opening Portable Crafting Table.");
                player.openWorkbench(null, true);
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("pct") && sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("pct.use")) {
                if (args.length == 0) {
                    if (getConfig().getBoolean(player.getName(), false)) {
                        getConfig().set(player.getName(), false);
                        saveConfig();
                        player.sendMessage(ChatColor.GREEN + "Portable Crafting Table disabled.");
                    } else {
                        getConfig().set(player.getName(), true);
                        saveConfig();
                        player.sendMessage(ChatColor.GREEN + "Portable Crafting Table enabled.");
                    }
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("on")) {
                        getConfig().set(player.getName(), true);
                        saveConfig();
                        player.sendMessage(ChatColor.GREEN + "Portable Crafting Table enabled.");
                    } else if (args[0].equalsIgnoreCase("off")) {
                        getConfig().set(player.getName(), false);
                        saveConfig();
                        player.sendMessage(ChatColor.GREEN + "Portable Crafting Table disabled.");
                    } else {
                        player.sendMessage(ChatColor.GREEN + "Usage: /pct [on/off]");
                    }
                } else {
                    player.sendMessage(ChatColor.GREEN + "Usage: /pct [on/off]");
                }
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "You don't have permissions to do that!");
                return true;
            }
        } else if (cmd.getName().equalsIgnoreCase("wb") && sender instanceof Player) {
            Player player = (Player) sender;
            if (getConfig().getBoolean(player.getName(), false) && player.hasPermission("pct.use")) {
                player.sendMessage(ChatColor.GREEN + "Opening Portable Crafting Table.");
                player.openWorkbench(null, true);
            } else if (!(player.hasPermission("pct.use"))) {
                player.sendMessage(ChatColor.RED + "You don't have permissions to do that!");
            } else {				
                player.sendMessage(ChatColor.GREEN + "Portable Crafting Table is turned off. Use /pct\" to toggle on.");
            }
            return true;
        }
        return false;
    }
}
