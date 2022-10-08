package com.github.citadelcraft.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.kingdoms.events.general.KingdomCreateEvent;
import org.kingdoms.events.general.KingdomDisbandEvent;
import org.kingdoms.events.general.KingdomGUIOpenEvent;
import org.kingdoms.events.general.KingdomRenameEvent;
import org.kingdoms.events.members.KingdomJoinEvent;
import org.kingdoms.events.members.KingdomLeaveEvent;
import org.kingdoms.gui.GUIOption;
import org.kingdoms.gui.InteractiveGUI;
import org.kingdoms.gui.KingdomsGUI;

import java.util.Optional;

import io.github.guipenedo.factionwars.FactionWars;
import io.github.guipenedo.factionwars.handler.TeamHandlerListener;
import io.github.guipenedo.factionwars.menus.MainMenu;

public class KingdomsX implements Listener{

    @EventHandler
    public void onKingdomMemberJoin(KingdomJoinEvent event) {
      TeamHandlerListener.onTeamChange(FactionWars.get().getServer().getPlayer(event.getKingdomPlayer().getId()));
    }
    
    @EventHandler
    public void onKingdomMemberLeave(KingdomLeaveEvent event) {
      TeamHandlerListener.onTeamChange(FactionWars.get().getServer().getPlayer(event.getKingdomPlayer().getId()));
    }
    
    @EventHandler
    public void onKingdomCreate(KingdomCreateEvent event) {
      TeamHandlerListener.onTeamCreate(event.getKingdom().getId().toString());
    }
    
    @EventHandler
    public void onKingdomDelete(KingdomDisbandEvent event) {
      TeamHandlerListener.onTeamDelete(event.getKingdom().getId().toString());
    }

    @EventHandler
    public void onKingdomRename(KingdomRenameEvent event){
      TeamHandlerListener.onTeamRename(event.getKingdom().getId().toString());
    }



    @EventHandler(ignoreCancelled = true)
    public void onNexusOpen(KingdomGUIOpenEvent event) {
        Player player = event.getPlayer().getPlayer();
        InteractiveGUI gui = event.getGUI();
        if (!gui.is(KingdomsGUI.STRUCTURES_NEXUS_NEXUS)) return;

        Optional<GUIOption> optionOpt = gui.getOption("war");
        if (!optionOpt.isPresent()) return;

        gui.push("war", () ->{
          MainMenu mainMenu = new MainMenu(player);
          mainMenu.open();
        }, new Object[0]);
}
    
}
