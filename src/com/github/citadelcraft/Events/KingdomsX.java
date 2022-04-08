package com.github.citadelcraft.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kingdoms.events.general.KingdomCreateEvent;
import org.kingdoms.events.general.KingdomDisbandEvent;
import org.kingdoms.events.members.KingdomJoinEvent;
import org.kingdoms.events.members.KingdomLeaveEvent;

import io.github.guipenedo.factionwars.FactionWars;
import io.github.guipenedo.factionwars.handler.TeamHandlerListener;

public class KingdomsX implements Listener{

    @EventHandler
    public void onKingdomMemberJoin(KingdomJoinEvent event) {
      TeamHandlerListener.onTeamChange(FactionWars.get().getServer().getPlayer(event.getKingdom().getId()));
    }
    
    @EventHandler
    public void onKingdomMemberLeave(KingdomLeaveEvent event) {
      TeamHandlerListener.onTeamChange(FactionWars.get().getServer().getPlayer(event.getKingdomPlayer().getKingdomId()));
    }
    
    @EventHandler
    public void onKingdomCreate(KingdomCreateEvent event) {
      TeamHandlerListener.onTeamCreate(event.getKingdom().getId().toString());
    }
    
    @EventHandler
    public void onKingdomDelete(KingdomDisbandEvent event) {
      TeamHandlerListener.onTeamDelete(event.getKingdom().getId().toString());
    }
    
}
