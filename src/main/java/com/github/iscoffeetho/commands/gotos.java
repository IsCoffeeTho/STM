package com.github.iscoffeetho.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class gotos {
	HashMap<UUID, ArrayList<gotoRequest>> reqLUT;

	public gotos() {
		reqLUT = new HashMap<UUID, ArrayList<gotoRequest>>();
	}

	public void registerCommands(JavaPlugin p) {
		PluginCommand cmd;

		cmd = p.getCommand("goto");
		cmd.setExecutor(new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
				Player player = (Player) sender;
				if (!player.isOnline()) {
					sender.sendMessage("Must be a Player to run /goto");
					return true;
				}

				if (player.getDisplayName().equals(args[0]))
				{
					sender.sendMessage("Cannot request to goto yourself");
					return true;
				}

				Player reciever = Bukkit.getPlayer(args[0]);
				if (reciever == null) {
					sender.sendMessage("Must be sent to a Player that is online");
					return true;
				}

				UUID txUUID = player.getUniqueId();
				UUID rxUUID = reciever.getUniqueId();

				ArrayList<gotoRequest> rxReq = reqLUT.get(rxUUID);

				if (rxReq == null) {
					rxReq = new ArrayList<gotoRequest>();
					reqLUT.put(rxUUID, rxReq);
				} else {
					Iterator<gotoRequest> it = rxReq.iterator();
					while (it.hasNext()) {
						gotoRequest i = it.next();
						if (i.from.getUniqueId().equals(txUUID)) {
							sender.sendMessage("Already sent request");
							return true;
						}
					}
				}
				rxReq.add(new gotoRequest(player, reciever));

				TextComponent message = new TextComponent(player.getDisplayName() + " has sent you a goto request:\n");

				TextComponent accept = new TextComponent("[Accept]");
				accept.setClickEvent(
						new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/acceptgoto " + player.getDisplayName()));
				accept.setHoverEvent(
						new HoverEvent(HoverEvent.Action.SHOW_TEXT,
								new ComponentBuilder("Accept " + player.getDisplayName() + "'s request").create()));

				TextComponent sep = new TextComponent(" ");

				TextComponent deny = new TextComponent("[Deny]");
				deny.setClickEvent(
						new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/denygoto " + player.getDisplayName()));
				deny.setHoverEvent(
						new HoverEvent(HoverEvent.Action.SHOW_TEXT,
								new ComponentBuilder("Deny " + player.getDisplayName() + "'s request").create()));

				reciever.spigot().sendMessage(message, accept, sep, deny);
				return true;
			}
		});

		TabCompleter tabCompleteGoto = new TabCompleter() {
			@Override
			public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
				Player player = (Player) sender;
				if (!player.isOnline()) {
					return null;
				}
				ArrayList<String> complete = new ArrayList<String>();

				ArrayList<gotoRequest> reqs = reqLUT.get(player.getUniqueId());

				if (reqs == null)
					return complete;

				Iterator<gotoRequest> it = reqs.iterator();
				while (it.hasNext()) {
					gotoRequest i = it.next();
					complete.add(i.from.getDisplayName());
				}

				return complete;
			}
		};

		cmd = p.getCommand("acceptgoto");
		cmd.setExecutor(new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
				Player player = (Player) sender;
				if (!player.isOnline()) {
					sender.sendMessage("Must be a Player to run /acceptgoto");
					return true;
				}

				Player reciever = Bukkit.getPlayer(args[0]);
				if (reciever == null) {
					sender.sendMessage("This player doesn't exist or is not online");
					return true;
				}

				UUID rxUUID = player.getUniqueId();
				UUID txUUID = reciever.getUniqueId();

				ArrayList<gotoRequest> rxReq = reqLUT.get(rxUUID);

				if (rxReq == null) {
					sender.sendMessage("This player hasn't requested a goto");
					return true;
				}
				Iterator<gotoRequest> it = rxReq.iterator();
				while (it.hasNext()) {
					gotoRequest i = it.next();
					if (i.from.getUniqueId().equals(txUUID)) {
						sender.sendMessage("Accepted goto request");
						i.accept();
						it.remove();
						return true;
					}
				}

				sender.sendMessage("This player hasn't requested a goto");
				return true;
			}
		});
		cmd.setTabCompleter(tabCompleteGoto);
		cmd = p.getCommand("denygoto");
		cmd.setExecutor(new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
				Player player = (Player) sender;
				if (!player.isOnline()) {
					sender.sendMessage("Must be a Player to run /denygoto");
					return true;
				}

				Player reciever = Bukkit.getPlayer(args[0]);
				if (reciever == null) {
					sender.sendMessage("This player doesn't exist or is not online");
					return true;
				}

				UUID rxUUID = player.getUniqueId();
				UUID txUUID = reciever.getUniqueId();

				ArrayList<gotoRequest> rxReq = reqLUT.get(rxUUID);

				if (rxReq == null) {
					sender.sendMessage("This player hasn't requested a goto");
					return true;
				}
				Iterator<gotoRequest> it = rxReq.iterator();
				while (it.hasNext()) {
					gotoRequest i = it.next();
					if (i.from.getUniqueId().equals(txUUID)) {
						sender.sendMessage("Denied goto request");
						i.deny();
						it.remove();
						return true;
					}
				}
				sender.sendMessage("This player hasn't requested a goto");
				return true;
			}
		});
		cmd.setTabCompleter(tabCompleteGoto);
	}
}
