package com.github.iscoffeetho.commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class warps {
	HashMap<String, Location> warplocations;

	public warps() {
		this.warplocations = new HashMap<String, Location>();
	}

	public void registerCommands(JavaPlugin p) {
		PluginCommand cmd;

		cmd = p.getCommand("setwarp");
		cmd.setExecutor(new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
				if (args.length != 1)
					return false;
				if (!sender.isOp()) {
					sender.sendMessage("Must be an operator to run /setwarp");
					return true;
				}
				Player player = (Player) sender;
				if (!player.isOnline()) {
					sender.sendMessage("Must be a Player to run /setwarp");
					return true;
				}
				warplocations.put(args[0], player.getLocation());
				sender.sendMessage("Created Warp " + args[0]);
				return true;
			}
		});
		cmd.setTabCompleter(new TabCompleter() {
			@Override
			public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
				return new ArrayList<String>(); // empty, so when writing the name the author doesn't get annoyed
			}
		});

		TabCompleter tabCompleteWarps = new TabCompleter() {
			@Override
			public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
				List<String> complete = new ArrayList<String>();
				Iterator<String> warpEndpoints = warplocations.keySet().iterator();
				while (warpEndpoints.hasNext())
					complete.add(warpEndpoints.next());
				return complete;
			}
		};

		cmd = p.getCommand("warp");
		cmd.setExecutor(new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
				if (args.length != 1)
					return false;
				Player player = (Player) sender;
				if (!player.isOnline()) {
					sender.sendMessage("Must be a Player to run /warp");
					return true;
				}
				Location pos = warplocations.get(args[0]);
				if (pos == null) {
					sender.sendMessage("That warp doesn't exist");
					return true;
				}
				player.teleport(pos);
				sender.sendMessage("Warping to " + args[0]);
				return true;
			}
		});
		cmd.setTabCompleter(tabCompleteWarps);

		cmd = p.getCommand("delwarp");
		cmd.setExecutor(new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
				if (args.length != 1)
					return false;
				if (!sender.isOp()) {
					sender.sendMessage("Must be OP to run /delwarp");
					return false;
				}
				Player player = (Player) sender;
				if (!player.isOnline()) {
					sender.sendMessage("Must be a Player to run /delwarp");
					return true;
				}
				Location pos = warplocations.get(args[0]);
				if (pos == null) {
					sender.sendMessage("Can't delete a warp that doesn't exist");
					return true;
				}
				warplocations.remove(args[0]);
				sender.sendMessage("Deleted Warp " + args[0]);
				return true;
			}
		});
		cmd.setTabCompleter(tabCompleteWarps);
	}

	public void serialize(OutputStream f) throws IOException {
		int size = warplocations.size();
		for (int j = 0; j < 4; j++) {
			f.write(size & 0xFF);
			size >>= 8;
		}
		Iterator<String> warpEndpoints = warplocations.keySet().iterator();
		while (warpEndpoints.hasNext()) {
			String warpName = warpEndpoints.next();
			Location warpPos = warplocations.get(warpName);
			f.write(warpName.getBytes());
			f.write(0);
			UUID world = warpPos.getWorld().getUID();
			long lower = world.getLeastSignificantBits();
			long higher = world.getMostSignificantBits();
			for (int j = 0; j < 8; j++) {
				f.write((int) (lower & 0xFF));
				lower >>= 8;
			}
			for (int j = 0; j < 8; j++) {
				f.write((int) (higher & 0xFF));
				higher >>= 8;
			}
			f.write(0);

			int x = warpPos.getBlockX();
			int y = warpPos.getBlockY();
			int z = warpPos.getBlockZ();
			for (int j = 0; j < 4; j++) {
				f.write(x & 0xFF);
				x >>= 8;
			}
			for (int j = 0; j < 4; j++) {
				f.write(z & 0xFF);
				z >>= 8;
			}
			for (int j = 0; j < 2; j++) {
				f.write(y & 0xFF);
				y >>= 8;
			}	
			f.write(0);
		}
	}

	public void deserialize(InputStream f) throws IOException {
		warplocations = new HashMap<String, Location>();
		int i = 0;
		for (int j = 0; j < 4; j++) {
			i <<= 8;
			i |= f.read();
		}
		while (i-- > 0) {
			String name = "";
			while (true) {
				int c = f.read();
				if (c == 0)
					break;
				name += (char) c;
			}

			long lower = 0;
			long higher = 0;

			for (int j = 0; j < 8; j++) {
				lower <<= 8;
				lower |= f.read();
			}
			for (int j = 0; j < 8; j++) {
				higher <<= 8;
				higher |= f.read();
			}
			UUID worldUUID = new UUID(lower, higher);

			int x = 0;
			int y = 0;
			int z = 0;

			for (int j = 0; j < 4; j++) {
				x <<= 8;
				x |= f.read();
			}
			for (int j = 0; j < 4; j++) {
				z <<= 8;
				z |= f.read();
			}
			for (int j = 0; j < 2; j++) {
				y <<= 8;
				y |= f.read();
			}

			if (f.read() != 0) // has to be zero from serialize
				throw new IOException("Illformed savefile");

			Location pos = Bukkit.getWorld(worldUUID).getBlockAt(x, y, z).getLocation();

			warplocations.put(name, pos);
		}
	}
}
