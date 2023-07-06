package com.github.iscoffeetho.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class homes {
	CommandExecutor home_executor;

	public homes() {

	}

	public void registerCommands(JavaPlugin p) {
		PluginCommand cmd;

		cmd = p.getCommand("home");
		cmd.setExecutor(new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
				Player player = (Player) sender;
				if (!player.isOnline()) {
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
	}
}

