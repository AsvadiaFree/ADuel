package fr.asvadia.aduel.modules;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

public class NoFire extends DuelParams {

    public NoFire() {
        active = true;
    }

    @Override
    public void onDamage(EntityDamageEvent event) {
        if (!active && event.getCause() == EntityDamageEvent.DamageCause.FIRE)
            event.setCancelled(true);
    }

    @Override
    public void onEntityDamage(EntityDamageByEntityEvent event) {

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
        return new NoFire();
    }

    @Override
    public String getTitle() {
        return "duel.params.nofire.title";
    }

    @Override
    public String getLore() {
        return "duel.params.nofire.lore";
    }
}
