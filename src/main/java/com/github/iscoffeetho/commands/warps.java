package com.github.iscoffeetho.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
// import org.bukkit.Bukkit;
// import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.iscoffeetho.nbt.*;

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
				Player player = null;
				try {
					player = (Player) sender;
				} catch (ClassCastException err) {
					sender.sendMessage("Must be a player to run /setwarp");
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
				Player player = null;
				try {
					player = (Player) sender;
				} catch (ClassCastException err) {
					sender.sendMessage("Must be a player to run /warp");
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

	public compoundTag toNBT() {
		Iterator<String> warpEndpoints = warplocations.keySet().iterator();
		compoundTag warpsRoot = new compoundTag("warps");
		while (warpEndpoints.hasNext()) {
			String warpName = warpEndpoints.next();

			compoundTag thisWarp = new compoundTag(warpName);
			Location warpPos = warplocations.get(warpName);

			UUID world = warpPos.getWorld().getUID();
			longArrayTag worlduuid = new longArrayTag();
			worlduuid.arrayList.add(new longTag(world.getLeastSignificantBits()));
			worlduuid.arrayList.add(new longTag(world.getMostSignificantBits()));

			intArrayTag position = new intArrayTag();
			position.arrayList.add(new intTag(warpPos.getBlockX()));
			position.arrayList.add(new intTag(warpPos.getBlockY()));
			position.arrayList.add(new intTag(warpPos.getBlockZ()));

			compoundTag rotation = new compoundTag("rotation");
			rotation.add("pitch", new floatTag(warpPos.getPitch()));
			rotation.add("yaw", new floatTag(warpPos.getYaw()));

			thisWarp.add("world", worlduuid);
			thisWarp.add("pos", position);
			thisWarp.add(rotation);

			warpsRoot.add(thisWarp);
		}
		return warpsRoot;
	}

	public void fromNBT(compoundTag saveRoot) {
		warplocations = new HashMap<String, Location>();
		compoundTag warpsRoot = (compoundTag) saveRoot.get("warps");
		if (warpsRoot == null)
			return;
		Iterator<NBTTag> it = warpsRoot.iterator();
		while (it.hasNext()) {
			compoundTag warp = (compoundTag) it.next();
			String name = warp.name;

			longArrayTag uuidList = (longArrayTag) warp.get("world");
			long lower = uuidList.arrayList.get(0).get();
			long higher = uuidList.arrayList.get(1).get();
			UUID worldUUID = new UUID(higher, lower);
			World world = Bukkit.getWorld(worldUUID);

			intArrayTag positionTag = (intArrayTag) warp.get("pos");
			int x = positionTag.arrayList.get(0).get();
			int y = positionTag.arrayList.get(1).get();
			int z = positionTag.arrayList.get(2).get();

			compoundTag rotationTag = (compoundTag) warp.get("rotation");
			float pitch = ((floatTag) rotationTag.get("pitch")).get();
			float yaw = ((floatTag) rotationTag.get("yaw")).get();

			Location loc = world.getBlockAt(x, y, z).getLocation();
			loc.add(0.5, 0.0, 0.5); // center to block
			loc.setPitch(pitch);
			loc.setYaw(yaw);

			warplocations.put(name, loc);
		}
	}
}
