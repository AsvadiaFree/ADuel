package fr.asvadia.aduel.modules;

import fr.asvadia.aduel.utils.Duel;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

public class NoKnockback extends DuelParams {

    public NoKnockback() {
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

    }

    @Override
    public void onVelocity(PlayerVelocityEvent event) {
        if (!active && !Duel.duelPlayers.get(event.getPlayer()).getDuelP().get(1).active)
            event.setCancelled(true);
    }

    @Override
    public DuelParams duplicate() {
        return new NoKnockback();
    }

    @Override
    public String getTitle() {
        return "duel.params.noknockback.title";
    }

    @Override
    public String getLore() {
        return "duel.params.noknockback.lore";
    }
}
