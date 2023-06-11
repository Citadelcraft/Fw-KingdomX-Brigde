package com.github.citadelcraft.Events;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.player.KingdomPlayer;
import org.kingdoms.libs.xseries.messages.Titles;
import org.kingdoms.managers.Masswar;

import io.github.guipenedo.factionwars.api.events.*;
import io.github.guipenedo.factionwars.models.WarMap;

public class Factionswar implements Listener{

          ///called when a player leaves a match
          @EventHandler
          public void onPlayerLeaveWar(FactionWarsPlayerLeaveWarEvent event) {
           
           Player player = event.getPlayer();
           KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);
           Kingdom kingdom = kp.getKingdom();
    
           if (kingdom.getHome() != null){
             player.teleport(kingdom.getHome(), TeleportCause.PLUGIN);
             Titles.sendTitle(player, 10, 30, 20, ChatColor.DARK_AQUA + "Teleported", "We moved you to your Kingdom Home!");
           }else if (kingdom.getNexus() != null){
             player.teleport(kingdom.getNexus().toBukkitLocation(), TeleportCause.PLUGIN);
             Titles.sendTitle(player, 10, 30, 20, ChatColor.DARK_AQUA + "Teleported", "We moved you to your Kingdom Nexus!");
           }else{
             player.teleport(player.getWorld().getSpawnLocation(), TeleportCause.PLUGIN);
             Titles.sendTitle(player, 10, 30, 20, ChatColor.DARK_AQUA + "Teleported", "We moved you to spawn!");
           }
    
          }
    
          ///called when a war ends
          @EventHandler
          public void onWarEnd(FactionWarsWarEndEvent event) {
            WarMap map = event.getMap();
    
            Kingdom winner = Kingdom.getKingdom(event.getWinner());
            Kingdom loser = Kingdom.getKingdom(event.getLoser());
            boolean pvp = false;
    
            winner.getOnlineMembers().forEach(player ->{
              KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);
              if (player.getWorld().getName().equals(map.getName())){
                if (winner.getHome() != null){
                  player.teleport(winner.getHome(), TeleportCause.PLUGIN);
                  Titles.sendTitle(player, 10, 30, 20, ChatColor.DARK_AQUA + "Teleported", "We moved you to your Kingdom Home!");
                }else if (winner.getNexus() != null){
                  player.teleport(winner.getNexus().toBukkitLocation(), TeleportCause.PLUGIN);
                  Titles.sendTitle(player, 10, 30, 20, ChatColor.DARK_AQUA +"Teleported", "We moved you to your Kingdom Nexus!");
                }else{
                  player.teleport(player.getWorld().getSpawnLocation(), TeleportCause.PLUGIN);
                  Titles.sendTitle(player, 10, 30, 20, ChatColor.DARK_AQUA +"Teleported", "We moved you to spawn!");
                } 
                kp.setPvp(pvp);
              }             
            });
    
            loser.getOnlineMembers().forEach(player -> {
              KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);
              if (player.getWorld().getName().equals(map.getName())){
                if (loser.getHome() != null){
                  player.teleport(loser.getHome(), TeleportCause.PLUGIN);
                  Titles.sendTitle(player, 10, 30, 20, ChatColor.DARK_AQUA +"Teleported", "We moved you to your Kingdom Home!");
                }else if (loser.getNexus() != null){
                  player.teleport(loser.getNexus().toBukkitLocation(), TeleportCause.PLUGIN);
                  Titles.sendTitle(player, 10, 30, 20, ChatColor.DARK_AQUA +"Teleported", "We moved you to your Kingdom Nexus!");
                }else{
                  player.teleport(player.getWorld().getSpawnLocation(), TeleportCause.PLUGIN);
                  Titles.sendTitle(player, 10, 30, 20, ChatColor.DARK_AQUA + "Teleported", "We moved you to spawn!");
                }
                kp.setPvp(pvp);
              }
            });
    
            
          }
    
          ///called when a war starts
          @EventHandler
          public void onWarstart(FactionWarsWarStartEvent event) {
            if (Masswar.getInstance().isRunning()){
              event.setCancelled(true);
            }
            boolean pvp = true;
    
            ArrayList<Player> team1players = event.getTeam1Players();
            ArrayList<Player> team2players = event.getTeam2Players();
    
            team1players.forEach(player -> {
              KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);
              kp.setPvp(pvp);
            });
            
            team2players.forEach(player -> {
              KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);
              kp.setPvp(pvp);
            });
    
          }

          
    
}
