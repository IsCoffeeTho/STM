package com.github.iscoffeetho;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import com.github.iscoffeetho.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

public class stm extends JavaPlugin {
	public static Logger LOGGER = Logger.getLogger("ServeTheMasses");

	private warps _warp;
	private homes _home;
	private gotos _goto;

	public void onEnable() {
		this._warp = new warps();
		this._home = new homes();
		this._goto = new gotos();

		this.deserialize();

		this._warp.registerCommands(this);
		this._home.registerCommands(this);
		this._goto.registerCommands(this);

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

			f.write("STM".getBytes());
			f.write(0);

			this._warp.serialize(f);
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

			byte[] sig = f.readNBytes(4);
			if (sig[0] != (int) 'S' || sig[1] != (int) 'T' || sig[2] != (int) 'M' || sig[3] != 0) {
				LOGGER.info("STM file had a bad file signature");
				f.close();
				return;
			}

			this._warp.deserialize(f);
			
			f.close();
		} catch (IOException err) {
			LOGGER.info("Couldn't load STM");
		}
	}
}
