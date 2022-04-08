package com.github.citadelcraft.Handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.github.citadelcraft.KingdomsXBrigde;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.group.model.KingdomRelation;
import org.kingdoms.constants.player.DefaultKingdomPermission;
import org.kingdoms.constants.player.KingdomPlayer;
import org.kingdoms.data.DataHandler;


import io.github.guipenedo.factionwars.FactionWars;
import io.github.guipenedo.factionwars.api.TeamHandler;

public class KingdomsXFw extends TeamHandler{

  KingdomsXBrigde plugin;
    
    public KingdomsXFw(String name, KingdomsXBrigde plugin) {
        super(name);
        this.plugin = plugin;
    }

    private Kingdom getKingdom(String Kingdomname) {
        return Kingdom.getKingdom(Kingdomname);
      }
      
      public String getTeamName(String Kingdomname) {
        Kingdom kingdom = getKingdom(Kingdomname);
        return (kingdom != null) ? kingdom.getName() : null;
      }
      
      public String getTeamIdByName(String Kingdomname) {
        Kingdom kingdom = getKingdom(Kingdomname);
        return (kingdom == null) ? null : kingdom.getId().toString();
      }
      
      public List<Player> getOnlinePlayers(String Kingdomname) {
        Kingdom kingdom = getKingdom(Kingdomname);
        if (kingdom == null)
          return Collections.emptyList(); 
        ArrayList<Player> arrayList = new ArrayList<Player>();
        kingdom.getOnlineMembers().forEach(Player -> {
            arrayList.add(Player);
        });
        return arrayList;
      }
      
      public TeamHandler.Relation getRelationBetween(String Kingdomname1, String Kingdomname2) {
        Kingdom kingdom1 = getKingdom(Kingdomname1);
        Kingdom kingdom2 = getKingdom(Kingdomname2);
        if (kingdom1 == null || kingdom2 == null)
          return null; 
        if (kingdom1.getRelationWith(kingdom2) == KingdomRelation.ALLY || kingdom1.getRelationWith(kingdom2) == KingdomRelation.NATION)
          return TeamHandler.Relation.ALLY;
        if (kingdom1.getRelationWith(kingdom2) == KingdomRelation.ENEMY)
          return TeamHandler.Relation.ENEMY;

        return TeamHandler.Relation.NEUTRAL;
      }

      public String getBankName(String KingdomName) {
        return null;
      }
      
      public double getBalance(String KingdomName) {
        Kingdom kingdom = Kingdom.getKingdom(KingdomName);
        return (kingdom == null) ? 0.0D : kingdom.getResourcePoints();
      }
      
      public void changeBalance(String KingdomName, double rp) {
        Kingdom kingdom = Kingdom.getKingdom(KingdomName);
        if (kingdom != null)
        kingdom.setResourcePoints(BigDecimal.valueOf(kingdom.getResourcePoints() + rp).longValue());
      }
      
      public void sendMessage(String Kingdomname1, String Kingdomname2) {
        Kingdom kingdom = getKingdom(Kingdomname1);
        if (kingdom != null)
          kingdom.getOnlineMembers().forEach(player -> {
              player.sendMessage(Kingdomname2);
        });
      }
      
      public List<String> getAllTeams() {
        ArrayList<String> arrayList = new ArrayList<String>();
        Collection<Kingdom> kingdoms = DataHandler.get().getKingdomManager().getKingdoms();
        kingdoms.forEach(kingdom -> {
          arrayList.add(kingdom.getName()); 
        });
        return arrayList;
      }
      
      public List<Player> getMembersWithRole(String Kingdomname, TeamHandler.Role role) {
        Kingdom kingdom = getKingdom(Kingdomname);
        if (kingdom == null)
          return Collections.emptyList(); 
        ArrayList<Player> arrayList = new ArrayList<Player>();
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
          return getOnlinePlayers(Kingdomname);
        } 
        return arrayList;
      }
      
      public String getPlayerTeam(Object player) {
        if (player instanceof String)
          player = FactionWars.get().getServer().getPlayer((String)player); 
        if (player == null)
          return null; 
        if (player instanceof Player) {
          KingdomPlayer kingdomPlayer = KingdomPlayer.getKingdomPlayer((Player)player);
          Kingdom kingdom = kingdomPlayer.getKingdom();
          return (kingdomPlayer != null && kingdom != null) ? kingdomPlayer.getKingdom().getName() : null;
        }
        if (player instanceof OfflinePlayer){
          KingdomPlayer kingdomPlayer = KingdomPlayer.getKingdomPlayer((Player)player);
          Kingdom kingdom = kingdomPlayer.getKingdom();
          return (kingdomPlayer != null && kingdom != null) ? kingdomPlayer.getKingdom().getName() : null;
        }
        return null;
      }
}
