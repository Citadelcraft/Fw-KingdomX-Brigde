package com.github.citadelcraft.Handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
import io.github.guipenedo.factionwars.handler.TeamHandlerListener;

public class KingdomsXFw extends TeamHandler implements Listener{
    
    public KingdomsXFw(String name) {
        super(name);
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

}
