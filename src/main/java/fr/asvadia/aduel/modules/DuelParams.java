package fr.asvadia.aduel.modules;

import fr.asvadia.api.bukkit.menu.inventory.AInventoryGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.ItemStack;

public abstract class DuelParams {
    public boolean active = false;
    public ItemStack mEnable = new ItemStack(Material.GREEN_WOOL);
    public ItemStack mDisable = new ItemStack(Material.GRAY_WOOL);
    public void onClick(Integer slot, Player player, AInventoryGUI aInventoryGUI, ClickType clickType) {
        active = !active;
        if (active)
            aInventoryGUI.getInventory().setItem(slot, mEnable);
        else
            aInventoryGUI.getInventory().setItem(slot, mDisable);
        player.openInventory(aInventoryGUI.getInventory());
    }
    public abstract void onDamage(EntityDamageEvent event);
    public abstract void onEntityDamage(EntityDamageByEntityEvent event);
    public abstract void onFoodChange(FoodLevelChangeEvent event);
    public abstract void onRegen(EntityRegainHealthEvent event);
    public abstract void onVelocity(PlayerVelocityEvent event);
    public abstract DuelParams duplicate();
    public abstract String getTitle();
    public abstract String getLore();
}
