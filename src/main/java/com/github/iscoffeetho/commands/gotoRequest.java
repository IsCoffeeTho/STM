package com.github.iscoffeetho.commands;

import org.bukkit.entity.Player;

public class gotoRequest {
	Player from;
	Player to;

	public gotoRequest(Player from, Player to) {
		this.from = from;
		this.to = to;
	}

	public void accept()
	{
		from.teleport(to);
		from.sendMessage(to.getDisplayName() + " accepted your request.");
	}

	public void deny()
	{
		from.sendMessage(to.getDisplayName() + " denied your request.");
	}

	public void failed() {
		from.sendMessage("Your goto request to " + to.getDisplayName() + " has failed.");
	}

}
