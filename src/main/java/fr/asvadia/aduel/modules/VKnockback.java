package fr.asvadia.aduel.modules;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

public class VKnockback extends DuelParams {
    private boolean velocity = false;

    @Override
    public void onDamage(EntityDamageEvent event) {}

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
        if (active && !velocity) {
            velocity = true;
            event.setCancelled(true);
            event.getPlayer().setVelocity(new Vector(0, 0.5, 0));
        } else
            velocity = false;
    }

    @Override
    public DuelParams duplicate() {
        return new VKnockback();
    }

    @Override
    public String getTitle() {
        return "duel.params.vknockback.title";
    }

    @Override
    public String getLore() {
        return "duel.params.vknockback.lore";
    }
}
