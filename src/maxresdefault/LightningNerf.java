package maxresdefault;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class LightningNerf extends JavaPlugin {
	
	public static final String VERSION = "R01";
	private Configuration cfg;
	public boolean quietMode;
	
	@Override
	public void onEnable() {
		
		if (!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}
		File configFile = new File(getDataFolder(), "config.yml");
		cfg = new Configuration(configFile);
		if (!configFile.exists()) {
			cfg.setProperty("quiet-mode", false);
			cfg.save();
		} else {
			cfg.load();
		}
		quietMode = cfg.getBoolean("quiet-mode", false);
		
		getServer().getPluginManager().registerEvent(Event.Type.BLOCK_IGNITE, new MyBlockListener(), Event.Priority.Normal, this);
    	
		if(quietMode) {
			log("Quiet mode enabled - plugin will not announce blocked lightning fires");
		}
		log("Enabled");
	}
	
	@Override
	public void onDisable() {
		log("Disabled");
	}
	
	public void log(String s) {
		Logger.getLogger("Minecraft").info("[LightningNerf " + VERSION + "]: " + s);
	}
	
	public class MyBlockListener extends BlockListener {
		@Override
		public void onBlockIgnite(BlockIgniteEvent event) {
			if(event.getCause() == BlockIgniteEvent.IgniteCause.LIGHTNING) {
				event.setCancelled(true);
				Block block = event.getBlock();
				if(!quietMode) {
					log("Stopped lightning fire at x=" + block.getLocation().getBlockX() + ", y=" + block.getLocation().getBlockY() + ", z=" + block.getLocation().getBlockZ());
				}
			}
		}
	}
}