package fr.asvadia.aduel.modules;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

import java.util.Objects;

public class TpPvP extends DuelParams {
    @Override
    public void onDamage(EntityDamageEvent event) {

    }

    @Override
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (active) {
            Location l = event.getEntity().getLocation().clone();
            if (event.getDamager() instanceof Arrow) {
                Player p = Bukkit.getPlayer(Objects.requireNonNull(event.getDamager().getCustomName()));
                assert p != null;
                event.getEntity().teleport(p);
                p.teleport(l);
            } else {
                event.getEntity().teleport(event.getDamager());
                event.getDamager().teleport(l);
            }
        }
    }

    @Override
    public void onFoodChange(FoodLevelChangeEvent event) {

    }

    @Override
    public void onRegen(EntityRegainHealthEvent event) {

    }

    @Override
    public void onVelocity(PlayerVelocityEvent event) {

    }

    @Override
    public DuelParams duplicate() {
        return new TpPvP();
    }

    @Override
    public String getTitle() {
        return "duel.params.tppvp.title";
    }

    @Override
    public String getLore() {
        return "duel.params.tppvp.lore";
    }
}
