package fr.asvadia.aduel.utils;

import fr.asvadia.aduel.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

import java.util.Arrays;
import java.util.List;

public class DuelListeners implements Listener {
    private final List<EntityRegainHealthEvent.RegainReason> rReason = Arrays.asList(EntityRegainHealthEvent.RegainReason.EATING, EntityRegainHealthEvent.RegainReason.SATIATED, EntityRegainHealthEvent.RegainReason.REGEN);

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Duel.duelPlayers.keySet().forEach(player ->
                player.hidePlayer(Main.getInstance(), event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageEvent event) {
        Bukkit.getLogger().info("DAMAGE !");
        if (event.getEntity() instanceof Player p) {
            Bukkit.getLogger().info("DAMAGE  = PLAYER !");
            if (!Duel.duelPlayers.containsKey(p) || !Duel.getDuelPlayers().get(p).isActive()) {
                Bukkit.getLogger().info("DAMAGE  = CANCEL !");
                event.setCancelled(true);
                return;
            }
            Bukkit.getLogger().info("DAMAGE  = GOOD !");
            Duel.getDuelPlayers().get(p).getDuelP().forEach(duelParams -> duelParams.onDamage(event));
            if (p.getHealth() - event.getFinalDamage() <= 0.0 && !event.isCancelled())
                Duel.duelPlayers.get(p).end(Duel.duelPlayers.get(p).getPLAYERS().get(p));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Bukkit.getLogger().info("DAMAGEEntity  = GOOD !");
        if (event.getEntity() instanceof Player
                && (event.getDamager() instanceof Player
                || event.getDamager() instanceof Arrow)) {
            Bukkit.getLogger().info("DAMAGEEntity  = Player and player or arrow !");
            Duel d1 = Duel.duelPlayers.get((Player) event.getEntity());
            if (event.getDamager() instanceof Arrow) {
                Player p2 = Bukkit.getPlayer(event.getDamager().getCustomName());
                if(Duel.duelPlayers.containsKey(p2)
                        && d1 == Duel.duelPlayers.get(p2)
                        && d1.isActive())
                    d1.getDuelP().forEach((duelParams) -> duelParams.onEntityDamage(event));
                else
                    event.setCancelled(true);
                event.getDamager().remove();
                return;
            }
            Bukkit.getLogger().info("DAMAGEEntity  = Player and player !");
            Duel d2 = Duel.getDuelPlayers().get((Player) event.getDamager());
            if(d1 != null
                    && d1 == d2
                    && d1.isActive()) {
                Duel.getDuelParams().forEach((duelParams) -> duelParams.onEntityDamage(event));
                Bukkit.getLogger().info("DAMAGEEntity  = Player and player EVENT !!");
            } else
                event.setCancelled(true);
        } else
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFoodChange(FoodLevelChangeEvent event) {
        Duel d = Duel.getDuelPlayers().get((Player) event.getEntity());
        if (d == null || !d.isActive())
            event.setCancelled(true);
        else
            Duel.getDuelParams().forEach((duelParams) -> duelParams.onFoodChange(event));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNaturalRegenerate(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof  Player
                && rReason.contains(event.getRegainReason())) {
            Duel d = Duel.getDuelPlayers().get((Player) event.getEntity());
            if (d != null && d.isActive())
                Duel.getDuelParams().forEach((duelParams) -> duelParams.onRegen(event));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player)
            event.getProjectile().setCustomName(event.getEntity().getName());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onProjectile(ProjectileHitEvent event) {
        event.getEntity().remove();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVelocity(PlayerVelocityEvent event) {
        Duel d = Duel.getDuelPlayers().get(event.getPlayer());
        if (d != null && d.isActive())
            Duel.getDuelParams().forEach((duelParams) -> duelParams.onVelocity(event));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEat(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.GLASS_BOTTLE)
            event.getItem().setType(Material.AIR);
    }
}
