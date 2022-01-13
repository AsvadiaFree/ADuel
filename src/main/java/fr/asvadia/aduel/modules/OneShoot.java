package fr.asvadia.aduel.modules;

import fr.asvadia.aduel.utils.Duel;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

public class OneShoot extends DuelParams {
    @Override
    public void onDamage(EntityDamageEvent event) {

    }

    @Override
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (active) {
            Player p = (Player) event.getEntity();
            if (Duel.duelPlayers.get(p).isActive())
                Duel.duelPlayers.get(p).end(Duel.duelPlayers.get(p).getPLAYERS().get(p));
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
        return new OneShoot();
    }

    @Override
    public String getTitle() {
        return "duel.params.oneshoot.title";
    }

    @Override
    public String getLore() {
        return "duel.params.oneshoot.lore";
    }
}
