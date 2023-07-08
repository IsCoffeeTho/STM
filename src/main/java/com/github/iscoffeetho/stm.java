package com.github.iscoffeetho;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import com.github.iscoffeetho.commands.*;
import com.github.iscoffeetho.nbt.*;

import org.bukkit.plugin.java.JavaPlugin;

public class stm extends JavaPlugin {
	public static Logger LOGGER = Logger.getLogger("ServeTheMasses");

	private warps _warp;
	private gotos _goto;
	private utilityCommands _util;

	public void onEnable() {
		this._warp = new warps();
		this._goto = new gotos();
		this._util = new utilityCommands();

		this.deserialize();

		this._warp.registerCommands(this);
		this._goto.registerCommands(this);
		this._util.registerCommands(this);

		LOGGER.info("STM is active");

	}

	public void onDisable() {
		this.serialize();

		LOGGER.info("STM is now inactive");
	}

	public void serialize() {
		try {
			File savefile = new File("STM.save");
			if (!savefile.exists())
				savefile.createNewFile();
			OutputStream f = new FileOutputStream(savefile);

			compoundTag saveData = new compoundTag("STM");
			saveData.add(this._warp.toNBT());

			saveData.writeTo(f);
			f.close();
		} catch (IOException err) {
			LOGGER.info("Couldn't save STM");
		}
	}

	public void deserialize() {
		try {
			File savefile = new File("STM.save");
			if (!savefile.exists())
				savefile.createNewFile();
			InputStream f = new FileInputStream(savefile);

			compoundTag saveData = new compoundTag("");
			saveData.readFrom(f);

			this._warp.fromNBT(saveData);

			
			f.close();
		} catch (IOException err) {
			LOGGER.info("Couldn't load STM");
		}
	}
}
