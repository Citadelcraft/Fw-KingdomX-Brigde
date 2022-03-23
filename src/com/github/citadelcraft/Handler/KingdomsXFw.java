package com.github.citadelcraft.Handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kingdoms.constants.kingdom.Kingdom;
import org.kingdoms.constants.player.KingdomPlayer;
import org.kingdoms.events.general.KingdomCreateEvent;
import org.kingdoms.events.general.KingdomDisbandEvent;
import org.kingdoms.events.members.KingdomJoinEvent;
import org.kingdoms.events.members.KingdomLeaveEvent;

import io.github.guipenedo.factionwars.FactionWars;
import io.github.guipenedo.factionwars.api.TeamHandler;
import io.github.guipenedo.factionwars.gamemodes.GamemodeManager;
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
        }
        return arrayList;
      }
      
      public TeamHandler.Relation getRelationBetween(String paramString1, String paramString2) {
        Kingdom kingdom1 = getKingdom(paramString1), kingdom2 = getKingdom(paramString2);
        if (kingdom1 == null || kingdom2 == null)
          return null; 
        if (kingdom1.isAllianceWith(kingdom2))
          return TeamHandler.Relation.ALLY; 
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
        for (UUID uUID : GameManagement.getKingdomManager().getKingdomList().keySet())
          arrayList.add(uUID.toString()); 
        return arrayList;
      }
      
      public List<Player> getMembersWithRole(String paramString, TeamHandler.Role paramRole) {
        Kingdom kingdom = getKingdom(paramString);
        if (kingdom == null)
          return Collections.emptyList(); 
        ArrayList<Player> arrayList = new ArrayList();
        if (paramRole == TeamHandler.Role.ADMIN && FactionWars.get().getServer().getPlayer(kingdom.getKing()) != null) {
          arrayList.add(FactionWars.get().getServer().getPlayer(kingdom.getKing()));
        } else if (paramRole == TeamHandler.Role.MODERATOR) {
          for (KingdomPlayer kingdomPlayer : kingdom.getOnlineMembers()) {
            if (kingdomPlayer.getRank() == Rank.MODS)
              arrayList.add(kingdomPlayer.getPlayer()); 
          } 
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
          KingdomPlayer kingdomPlayer = GameManagement.getPlayerManager().getSession((Player)paramObject);
          return (kingdomPlayer != null && kingdomPlayer.getKingdomUuid() != null) ? kingdomPlayer.getKingdomUuid().toString() : null;
        } 
        if (paramObject instanceof OfflinePlayer) {
          OfflineKingdomPlayer offlineKingdomPlayer = GameManagement.getPlayerManager().getOfflineKingdomPlayer((OfflinePlayer)paramObject);
          return (offlineKingdomPlayer != null && offlineKingdomPlayer.getKingdomUuid() != null) ? offlineKingdomPlayer.getKingdomUuid().toString() : null;
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
