package com.github.citadelcraft.Handler;

import org.bukkit.OfflinePlayer;

import com.github.citadelcraft.KingdomsXBrigde;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class FwarPlaceholders extends PlaceholderExpansion {

    private final KingdomsXBrigde plugin;
    
    public FwarPlaceholders(KingdomsXBrigde plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getAuthor() {
        return "Nostyll";
    }
    
    @Override
    public String getIdentifier() {
        return "fwx";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }
    
    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("top1")){
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
        }
        
        if(params.equalsIgnoreCase("top2")){
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
        }

        if(params.equalsIgnoreCase("top3")){
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
        }

        if(params.equalsIgnoreCase("top4")){
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
        }

        if(params.equalsIgnoreCase("top5")){
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
        }

        if(params.equalsIgnoreCase("top6")){
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
        }

        if(params.equalsIgnoreCase("top7")){
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
        }

        if(params.equalsIgnoreCase("top8")){
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
        }

        if(params.equalsIgnoreCase("top9")){
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
        }

        if(params.equalsIgnoreCase("top10")){
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
        }
        
        return null; // Placeholder is unknown by the Expansion
    }
}
