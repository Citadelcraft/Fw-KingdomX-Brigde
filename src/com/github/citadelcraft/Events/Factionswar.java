package com.github.citadelcraft.Events;

import java.util.ArrayList;

import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import com.github.citadelcraft.KingdomsXBrigde;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.player.KingdomPlayer;

import io.github.guipenedo.factionwars.api.events.*;
import io.github.guipenedo.factionwars.models.WarMap;
import net.md_5.bungee.api.ChatColor;

public class Factionswar implements Listener{

          ///called when a player leaves a match
          @EventHandler
          public void onPlayerLeaveWar(FactionWarsPlayerLeaveWarEvent event) {
           
           Player player = event.getPlayer();
           KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);
           Kingdom kingdom = kp.getKingdom();
    
           if (kingdom.getHome() != null){
             player.teleport(kingdom.getHome());
             Titles.sendTitle(player, 10, 30, 20, "Teleported", "We moved you to your Kingdom Home!");
           }else if (kingdom.getNexus() != null){
             player.teleport(kingdom.getNexus().toBukkitLocation());
             Titles.sendTitle(player, 10, 30, 20, "Teleported", "We moved you to your Kingdom Nexus!");
           }else{
             player.teleport(player.getWorld().getSpawnLocation(), TeleportCause.PLUGIN);
             Titles.sendTitle(player, 10, 30, 20, "Teleported", "We moved you to spawn!");
           }
    
          }
    
          ///called when a war ends
          @EventHandler
          public void onWarEnd(FactionWarsWarEndEvent event) {
            WarMap map = event.getMap();
    
            Kingdom winner = Kingdom.getKingdom(event.getWinner());
            Kingdom loser = Kingdom.getKingdom(event.getLoser());
    
            winner.getOnlineMembers().forEach(player ->{
              KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);
              if (player.getWorld().getName().equals(map.getName())){
                if (winner.getHome() != null){
                  player.teleport(winner.getHome());
                }else if (winner.getNexus() != null){
                  player.teleport(winner.getNexus().toBukkitLocation());
                }
                kp.setPvp(false);
                ActionBar.sendActionBar(KingdomsXBrigde.get(), player, ChatColor.DARK_RED + "Pvp Mode OFF", 5 * 20);
              }
            });
    
            loser.getOnlineMembers().forEach(player -> {
              KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);
              if (player.getWorld().getName().equals(map.getName())){
                if (loser.getHome() != null){
                  player.teleport(loser.getHome());
                }else if (loser.getNexus() != null){
                  player.teleport(loser.getNexus().toBukkitLocation());
                }
                kp.setPvp(false);
                ActionBar.sendActionBar(KingdomsXBrigde.get(), player, ChatColor.DARK_RED + "Pvp Mode OFF", 5 * 20);
              }
            });
    
            
          }
    
          ///called when a war starts
          @EventHandler
          public void onWarstart(FactionWarsWarStartEvent event) {
            Kingdom team1 = Kingdom.getKingdom(event.getTeam1());
            Kingdom team2 = Kingdom.getKingdom(event.getTeam2());
            WarMap map = event.getMap();
    
            ArrayList<Player> team1players = event.getTeam1Players();
            ArrayList<Player> team2players = event.getTeam2Players();
    
            team1players.forEach(player -> {
              KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);
              kp.setPvp(true);
              ActionBar.sendActionBar(KingdomsXBrigde.get(), player, ChatColor.DARK_AQUA + "Pvp Mode ON", 5 * 20);
            });
            
            team2players.forEach(player -> {
              KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);
              kp.setPvp(true);
              ActionBar.sendActionBar(KingdomsXBrigde.get(), player, ChatColor.DARK_AQUA + "Pvp Mode ON", 5 * 20);
            });
    
          }
    
}
