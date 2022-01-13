package fr.asvadia.aduel.modules;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

public class NaturalRegen extends DuelParams {
    public NaturalRegen() {
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

    }

    @Override
    public void onRegen(EntityRegainHealthEvent event) {
        if (!active)
            event.setCancelled(true);
    }

    @Override
    public void onVelocity(PlayerVelocityEvent event) {

    }

    @Override
    public DuelParams duplicate() {
        return new NaturalRegen();
    }

    @Override
    public String getTitle() {
        return "duel.params.naturalregen.title";
    }

    @Override
    public String getLore() {
        return "duel.params.naturalregen.lore";
    }
}
