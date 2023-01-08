package com.github.citadelcraft;

import com.github.citadelcraft.Events.Factionswar;
import com.github.citadelcraft.Events.KingdomsX;
import com.github.citadelcraft.Handler.KingdomsXFw;
import com.github.citadelcraft.Untils.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.guipenedo.factionwars.api.FactionWarsAddonPlugin;
import io.github.guipenedo.factionwars.api.TeamHandler;
import io.github.guipenedo.factionwars.gamemodes.GamemodeManager;
import io.github.guipenedo.factionwars.models.WarMap;


public class KingdomsXBrigde extends JavaPlugin implements FactionWarsAddonPlugin {

	private static KingdomsXBrigde instance;

	@Override
	public void onEnable() {
		ConsoleCommandSender console = Bukkit.getConsoleSender();
		PluginManager pm = Bukkit.getPluginManager();

		console.sendMessage(chat.formatText("&a============================="));
		console.sendMessage(chat.formatText(String.format("&7%s %s by &5Nostyll <3&7!", this.getName(), this.getDescription().getVersion())));

		if (!pm.isPluginEnabled("FactionWars") || !pm.isPluginEnabled("Kindoms")) {
			getLogger().severe("*** !! FactionWars & KindomsX are need !! ***");
			getLogger().severe("*** FactionWars or KindomsX is not installed or not enabled. ***");
			getLogger().severe("*** This plugin will be disabled. ***");
			this.setEnabled(false);
			return;
		}
		console.sendMessage(chat.formatText("&7Action: &aEnabling&7..."));
		registerEvent((Listener)new Factionswar());
		registerEvent((Listener)new KingdomsX());
		console.sendMessage(chat.formatText("&7Action: &Enabled&7..."));
		console.sendMessage(chat.formatText("&a============================="));
	}

	@Override
	public GamemodeManager getGamemodeManager(String customType, WarMap map, ConfigurationSection config) {
		return null;
	}

	@Override
	public TeamHandler getTeamHandler() {
		return new KingdomsXFw("KingdomsXBrigde", this); //CustomTeamHandler extends TeamHandler. The String parameter is the name that FactionWars will display in the console (your plugin name)
	}

	private void registerEvent(Listener listener) {
		getServer().getPluginManager().registerEvents(listener, (Plugin)this);
	}

	public static KingdomsXBrigde get() {
		return instance;
	}

	@Override
	public void onDisable() {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(chat.formatText("&a============================="));
        console.sendMessage(chat.formatText("&7FactionsWars + KingdomsX " + this.getDescription().getVersion() + " by &55Nostyll <3!"));
        console.sendMessage(chat.formatText("&7Action: &cDisabling&7..."));
        console.sendMessage(chat.formatText("&a============================="));

	}

}
