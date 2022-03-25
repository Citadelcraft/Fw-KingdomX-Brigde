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
        //you may add any other additional setup you need here
        super(name);
    }

    private Kingdom getKingdom(String paramString) {
        return Kingdom.getKingdom(paramString);
      }
      
      public String getTeamName(String paramString) {
        Kingdom kingdom = getKingdom(paramString);
        return (kingdom != null) ? kingdom.getName() : null;
      }
      
      public String getTeamIdByName(String paramString) {
        Kingdom kingdom = getKingdom(paramString);
        return (kingdom == null) ? null : kingdom.getId().toString();
      }
      
      public List<Player> getOnlinePlayers(String paramString) {
        Kingdom kingdom = getKingdom(paramString);
        if (kingdom == null)
          return Collections.emptyList(); 
        ArrayList<Player> arrayList = new ArrayList();
        kingdom.getOnlineMembers().forEach(Player -> {
            arrayList.add(Player);
        });
        return arrayList;
      }
      
      public TeamHandler.Relation getRelationBetween(String paramString1, String paramString2) {
        Kingdom kingdom1 = getKingdom(paramString1);
        Kingdom kingdom2 = getKingdom(paramString2);
        if (kingdom1 == null || kingdom2 == null)
          return null; 
        if (kingdom1.getRelationWith(kingdom2) == KingdomRelation.ALLY || kingdom1.getRelationWith(kingdom2) == KingdomRelation.NATION)
          return TeamHandler.Relation.ALLY;
        if (kingdom1.getRelationWith(kingdom2) == KingdomRelation.ENEMY)
          return TeamHandler.Relation.ENEMY;

        return TeamHandler.Relation.NEUTRAL;
      }
      
      public String getBankName(String paramString) {
        Kingdom kingdom = getKingdom(paramString);
        if (kingdom == null)
          return null; 
        return FactionWars.get().getServer().getOfflinePlayer(kingdom.getKing().getPlayer().toString()).getName();
      }
      
      public void sendMessage(String paramString1, String paramString2) {
        Kingdom kingdom = getKingdom(paramString1);
        if (kingdom != null)
          kingdom.getOnlineMembers().forEach(player -> {
              player.sendMessage(paramString2);
        });
      }
      
      public List<String> getAllTeams() {
        ArrayList<String> arrayList = new ArrayList();
        Collection<Kingdom> kingdoms = DataHandler.get().getKingdomManager().getKingdoms();
        kingdoms.forEach(kingdom -> {
          arrayList.add(kingdom.getId().toString()); 
        });
        return arrayList;
      }
      
      public List<Player> getMembersWithRole(String paramString, TeamHandler.Role paramRole) {
        Kingdom kingdom = getKingdom(paramString);
        if (kingdom == null)
          return Collections.emptyList(); 
        ArrayList<Player> arrayList = new ArrayList();
        if (paramRole == TeamHandler.Role.ADMIN && FactionWars.get().getServer().getPlayer(kingdom.getKing().getPlayer().toString()) != null) {
          arrayList.add(FactionWars.get().getServer().getPlayer(kingdom.getKing().getPlayer().toString()));
        } else if (paramRole == TeamHandler.Role.MODERATOR) {
          kingdom.getOnlineMembers().forEach(player -> {
            KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);
            if (kp.hasPermission(DefaultKingdomPermission.MANAGE_RANKS)){
              arrayList.add(kp.getPlayer());
            }
          });
        } else if (paramRole == TeamHandler.Role.NORMAL) {
          return getOnlinePlayers(paramString);
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
          return (kingdomPlayer != null && kingdomPlayer.getKingdom().getId() != null) ? kingdomPlayer.getKingdom().getId().toString() : null;
        }
        if (paramObject instanceof OfflinePlayer){
          KingdomPlayer kingdomPlayer = KingdomPlayer.getKingdomPlayer((Player)paramObject);
          return (kingdomPlayer != null && kingdomPlayer.getKingdom().getId() != null) ? kingdomPlayer.getKingdom().getId().toString() : null;
        }
        return null;
      }
      
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
