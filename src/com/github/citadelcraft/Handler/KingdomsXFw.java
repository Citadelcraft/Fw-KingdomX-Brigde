package com.github.citadelcraft.Handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.cryptomorin.xseries.messages.ActionBar;
import com.github.citadelcraft.KingdomsXBrigde;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.group.model.KingdomRelation;
import org.kingdoms.constants.player.DefaultKingdomPermission;
import org.kingdoms.constants.player.KingdomPlayer;
import org.kingdoms.data.DataHandler;
import org.kingdoms.events.general.KingdomCreateEvent;
import org.kingdoms.events.general.KingdomDisbandEvent;
import org.kingdoms.events.members.KingdomJoinEvent;
import org.kingdoms.events.members.KingdomLeaveEvent;

import io.github.guipenedo.factionwars.FactionWars;
import io.github.guipenedo.factionwars.api.TeamHandler;
import io.github.guipenedo.factionwars.api.events.*;
import io.github.guipenedo.factionwars.handler.TeamHandlerListener;
import io.github.guipenedo.factionwars.models.WarMap;

public class KingdomsXFw extends TeamHandler implements Listener{

  KingdomsXBrigde plugin;
    
    public KingdomsXFw(String name, KingdomsXBrigde plugin) {
        super(name);
        this.plugin = plugin;
    }
      
      public String getTeamName(String KingdomName) {
        Kingdom kingdom = Kingdom.getKingdom(KingdomName);
        return (kingdom != null) ? kingdom.getName() : null;
      }
      
      public String getTeamIdByName(String KingdomName) {
        Kingdom kingdom = Kingdom.getKingdom(KingdomName);
        return (kingdom == null) ? null : kingdom.getId().toString();
      }
      
      public List<Player> getOnlinePlayers(String KingdomName) {
        Kingdom kingdom = Kingdom.getKingdom(KingdomName);
        if (kingdom == null)
          return Collections.emptyList(); 
        ArrayList<Player> arrayList = new ArrayList();
        kingdom.getOnlineMembers().forEach(Player -> {
            arrayList.add(Player);
        });
        return arrayList;
      }
      
      public TeamHandler.Relation getRelationBetween(String KingdomName1, String KingdomName2) {
        Kingdom kingdom1 = Kingdom.getKingdom(KingdomName1);
        Kingdom kingdom2 = Kingdom.getKingdom(KingdomName2);
        if (kingdom1 == null || kingdom2 == null)
          return null; 
        if (kingdom1.getRelationWith(kingdom2) == KingdomRelation.ALLY)
          return TeamHandler.Relation.ALLY;
        if (kingdom1.getRelationWith(kingdom2) == KingdomRelation.ENEMY)
          return TeamHandler.Relation.ENEMY;

        return TeamHandler.Relation.NEUTRAL;
      }
      
      public String getBankName(String KingdomName) {
        Kingdom kingdom = Kingdom.getKingdom(KingdomName);
        if (kingdom == null)
          return null; 
        return String.valueOf(kingdom.getBank());
      }
      
      public void sendMessage(String KingdomName, String msg) {
        Kingdom kingdom = Kingdom.getKingdom(KingdomName);
        if (kingdom != null)
          kingdom.getOnlineMembers().forEach(player -> {
              player.sendMessage(msg);
        });
      }
      
      public List<String> getAllTeams() {
        ArrayList<String> arrayList = new ArrayList();
        Collection<Kingdom> kingdoms = DataHandler.get().getKingdomManager().getKingdoms();
        kingdoms.forEach(kingdom -> {
          arrayList.add(kingdom.getName()); 
        });
        return arrayList;
      }
      
      public List<Player> getMembersWithRole(String KingdomName, TeamHandler.Role role) {
        Kingdom kingdom = Kingdom.getKingdom(KingdomName);
        if (kingdom == null)
          return Collections.emptyList(); 
        ArrayList<Player> arrayList = new ArrayList();
        if (role == TeamHandler.Role.ADMIN && FactionWars.get().getServer().getPlayer(kingdom.getKing().getPlayer().getUniqueId()) != null) {
          arrayList.add(FactionWars.get().getServer().getPlayer(kingdom.getKing().getPlayer().getUniqueId()));
        } else if (role == TeamHandler.Role.MODERATOR) {
          kingdom.getOnlineMembers().forEach(player -> {
            KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);
            if (kp.hasPermission(DefaultKingdomPermission.MANAGE_RANKS)){
              arrayList.add(kp.getPlayer());
            }
          });
        } else if (role == TeamHandler.Role.NORMAL) {
          return getOnlinePlayers(KingdomName);
        } 
        return arrayList;
      }
      
      public String getPlayerTeam(Object paramObject) {
        if (paramObject instanceof String)
          paramObject = FactionWars.get().getServer().getPlayer((String)paramObject); 
        if (paramObject == null)
          return null; 
        if (paramObject instanceof Player) {
          KingdomPlayer kingdomPlayer = KingdomPlayer.getKingdomPlayer((Player)paramObject);
          Kingdom kingdom = kingdomPlayer.getKingdom();
          return (kingdomPlayer != null && kingdom != null) ? kingdomPlayer.getKingdom().getName() : null;
        }
        return null;
      }
      
      @EventHandler
      public void onKingdomMemberJoin(KingdomJoinEvent event) {
        TeamHandlerListener.onTeamChange(event.getPlayer().getPlayer());
      }
      
      @EventHandler
      public void onKingdomMemberLeave(KingdomLeaveEvent event) {
        TeamHandlerListener.onTeamChange(event.getKingdomPlayer().getPlayer());
      }
      
      @EventHandler
      public void onKingdomCreate(KingdomCreateEvent event) {
        TeamHandlerListener.onTeamCreate(event.getKingdom().getId().toString());
      }
      
      @EventHandler
      public void onKingdomDelete(KingdomDisbandEvent event) {
        TeamHandlerListener.onTeamDelete(event.getKingdom().getId().toString());
      }

      ///called when a player leaves a match
      @EventHandler
      public void onPlayerLeaveWar(FactionWarsPlayerLeaveWarEvent event) {
       
       WarMap map = event.getMap();
       Player player = event.getPlayer();
       KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);
       Kingdom kingdom = kp.getKingdom();

       if (kingdom.getHome() != null){
         player.teleport(kingdom.getHome());
       }else if (kingdom.getNexus() != null){
         player.teleport(kingdom.getNexus().toBukkitLocation());
       }

      }

      ///called when a war ends
      @EventHandler
      public void onPlayerLeaveWar(FactionWarsWarEndEvent event) {
        WarMap map = event.getMap();
        boolean tied = event.isTied();

        Kingdom winner = Kingdom.getKingdom(event.getWinner());
        Kingdom loser = Kingdom.getKingdom(event.getLoser());

        winner.getOnlineMembers().forEach(player ->{
          if (player.getWorld().getName().equals(map.getName())){
            if (winner.getHome() != null){
              player.teleport(winner.getHome());
            }else if (winner.getNexus() != null){
              player.teleport(winner.getNexus().toBukkitLocation());
            }
          }
        });

        loser.getOnlineMembers().forEach(player -> {
          if (player.getWorld().getName().equals(map.getName())){
            if (loser.getHome() != null){
              player.teleport(loser.getHome());
            }else if (loser.getNexus() != null){
              player.teleport(loser.getNexus().toBukkitLocation());
            }
          }
        });

        
      }

      ///called when a war starts
      @EventHandler
      public void onPlayerLeaveWar(FactionWarsWarStartEvent event) {
        Kingdom team1 = Kingdom.getKingdom(event.getTeam1());
        Kingdom team2 = Kingdom.getKingdom(event.getTeam2());
        WarMap map = event.getMap();

        ArrayList<Player> team1players = event.getTeam1Players();
        ArrayList<Player> team2players = event.getTeam2Players();

        team1players.forEach(player -> {
          KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);
          kp.setPvp(true);
          ActionBar.sendActionBar(plugin, player, ChatColor.DARK_AQUA + "Pvp Mode Active", 5 * 20);
        });
        
        team2players.forEach(player -> {
          KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);
          kp.setPvp(true);
          ActionBar.sendActionBar(plugin, player, ChatColor.DARK_AQUA + "Pvp Mode Active", 5 * 20);
        });

      }

}
