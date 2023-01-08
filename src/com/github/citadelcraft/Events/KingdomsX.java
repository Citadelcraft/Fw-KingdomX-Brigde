package com.github.citadelcraft.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.events.general.GroupRenameEvent;
import org.kingdoms.events.general.KingdomCreateEvent;
import org.kingdoms.events.general.KingdomDisbandEvent;
import org.kingdoms.events.general.KingdomGUIOpenEvent;
import org.kingdoms.events.masswar.MassWarStartEvent;
import org.kingdoms.events.members.KingdomJoinEvent;
import org.kingdoms.events.members.KingdomLeaveEvent;
import org.kingdoms.gui.GUIOption;
import org.kingdoms.gui.InteractiveGUI;
import org.kingdoms.gui.KingdomsGUI;
import org.kingdoms.utils.xseries.messages.Titles;

import java.util.Optional;

import io.github.guipenedo.factionwars.handler.TeamHandlerListener;
import io.github.guipenedo.factionwars.managers.MatchManager;
import io.github.guipenedo.factionwars.menus.MainMenu;
import io.github.guipenedo.factionwars.models.WarMap;
import net.md_5.bungee.api.ChatColor;

public class KingdomsX implements Listener{

    @EventHandler
    public void onKingdomMemberJoin(KingdomJoinEvent event) {
      TeamHandlerListener.onTeamChange(event.getPlayer().getPlayer());
    }

    @EventHandler
    public void onKingdomCreate(KingdomCreateEvent event) {
      TeamHandlerListener.onTeamCreate(event.getKingdom().getName());
    }
    
    @EventHandler
    public void onKingdomMemberLeave(KingdomLeaveEvent event) {
      TeamHandlerListener.onTeamChange(event.getPlayer().getPlayer());
    }
    
    @EventHandler
    public void onKingdomDelete(KingdomDisbandEvent event) {
      TeamHandlerListener.onTeamDelete(event.getKingdom().getName());
    }

    @EventHandler
    public void onKingdomRename(GroupRenameEvent event){
      Kingdom kingdom = event.getPlayer().getKingdom();
      TeamHandlerListener.onTeamRename(kingdom.getName());
    }

    /// We need a gui in nexus
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

  /// All players in war needs to be back for the Masswar!
  @EventHandler
  public void onMassWar(MassWarStartEvent event){
    Bukkit.getOnlinePlayers().forEach(player -> {
      WarMap warMap = MatchManager.get().getPlayerMap(player);
          if (player == null) return;
          if (warMap == null) return;
          Titles.clearTitle(player);
          Titles.sendTitle(player, 10, 30, 20, ChatColor.DARK_AQUA + "GAME END", "MassWar has been started, we all need you there!");
          MatchManager.get().endGame(warMap, true);
    });

  }
    
}
