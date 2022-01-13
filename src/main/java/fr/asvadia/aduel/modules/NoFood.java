package fr.asvadia.aduel.modules;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

public class NoFood extends DuelParams {

    public NoFood() {
        active = true;
    }

    @Override
    public void onDamage(EntityDamageEvent event) {

    }

    @Override
    public void onEntityDamage(EntityDamageByEntityEvent event) {

    }

    @Override
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (!active)
            event.setCancelled(true);
    }

    @Override
    public void onRegen(EntityRegainHealthEvent event) {

    }

    @Override
    public void onVelocity(PlayerVelocityEvent event) {

    }

    @Override
    public DuelParams duplicate() {
        return new NoFood();
    }


    @Override
    public String getTitle() {
        return "duel.params.nofood.title";
    }

    @Override
    public String getLore() {
        return "duel.params.nofood.lore";
    }
}
