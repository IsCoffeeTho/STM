package com.github.iscoffeetho.commands;

import java.util.ArrayList;
import java.util.List;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class utilityCommands {
	CommandExecutor home_executor;

	public utilityCommands() {

	}

	public void registerCommands(JavaPlugin p) {
		PluginCommand cmd;

		cmd = p.getCommand("home");
		cmd.setExecutor(new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
				Player player = null;

				try {
					player = (Player) sender;
				} catch (ClassCastException err) {
					sender.sendMessage("Must be a Player to run /home");
					return true;
				}
				Location bed = player.getBedSpawnLocation();
				if (bed == null) {
					sender.sendMessage("You do not have a spawn bed.");
					return true;
				}
				player.teleport(bed);
				return true;
			}
		});

		cmd = p.getCommand("heal");
		cmd.setExecutor(new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
				if (!sender.isOp()) {
					sender.sendMessage("Must be OP to run /heal");
					return true;
				}

				Player target = null;

				if (args.length > 0) {
					if (args[0].equals("*")) {
						Iterator<? extends Player> players = Bukkit.getServer().getOnlinePlayers().iterator();
						if (!players.hasNext()) {
							sender.sendMessage("Healed no one (There are no players online).");
							return true;
						}
						while (players.hasNext()) {
							Player p = players.next();
							p.setSaturation((float) 20.0);
							p.setExhaustion((float) 0.0);
							p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
						}
						sender.sendMessage("Healed all Players");
						return true;
					}
					target = Bukkit.getPlayer(args[0]);
					if (target == null) {
						sender.sendMessage("That player doesn't exist");
						return true;
					}
				} else {
					try {
						target = (Player) sender;
					} catch (ClassCastException err) {
						sender.sendMessage("Cannot heal a non player");
						return false;
					}
				}

				target.setSaturation((float) 20.0);
				target.setExhaustion((float) 0.0);
				target.setHealth(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
				sender.sendMessage("Healed " + target.getDisplayName());
				return true;
			}
		});
		cmd.setTabCompleter(new TabCompleter() {
			@Override
			public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
				List<String> complete = new ArrayList<String>();
				Iterator<? extends Player> players = Bukkit.getServer().getOnlinePlayers().iterator();

				while (players.hasNext()) {
					Player p = players.next();
					complete.add(p.getDisplayName());
				}
				complete.add("*");

				return complete;
			}
		});
	}
}
