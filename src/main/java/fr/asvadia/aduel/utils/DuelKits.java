package fr.asvadia.aduel.utils;

import fr.skyfighttv.simpleitem.ItemCreator;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.List;

public enum DuelKits {
    IRON("iron", 10, Material.IRON_CHESTPLATE,
            Arrays.asList(
                    new ItemStack(Material.IRON_SWORD),
                    new ItemStack(Material.GOLDEN_APPLE, 4),
                    new ItemStack(Material.POTION, 1, (byte)8226)
            ),
            new ItemStack[]{
                    new ItemStack(Material.IRON_BOOTS),
                    new ItemStack(Material.IRON_LEGGINGS),
                    new ItemStack(Material.IRON_CHESTPLATE),
                    new ItemStack(Material.IRON_HELMET)
            }),
    DIAMOND("diamond", 11, Material.DIAMOND_CHESTPLATE,
            Arrays.asList(
                    new ItemStack(Material.DIAMOND_SWORD),
                    new ItemStack(Material.GOLDEN_APPLE, 8),
                    new ItemStack(Material.POTION, 1, (byte)8226)
            ),
            new ItemStack[]{
                    new ItemStack(Material.DIAMOND_BOOTS),
                    new ItemStack(Material.DIAMOND_LEGGINGS),
                    new ItemStack(Material.DIAMOND_CHESTPLATE),
                    new ItemStack(Material.DIAMOND_HELMET)
            }),
    NO_DEBUFF("nodebuff", 19, Material.POTION,
            Arrays.asList(
                    new ItemCreator(Material.DIAMOND_SWORD)
                            .addEnchant(Enchantment.DAMAGE_ALL, 2)
                            .addEnchant(Enchantment.DURABILITY, 3)
                            .toItemStack(),
                    new ItemStack(Material.POTION, 1, (byte)8226)
            ),
            new ItemStack[]{
                    new ItemCreator(Material.DIAMOND_BOOTS)
                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                            .addEnchant(Enchantment.DURABILITY, 2)
                            .toItemStack(),
                    new ItemCreator(Material.DIAMOND_LEGGINGS)
                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                            .addEnchant(Enchantment.DURABILITY, 2)
                            .toItemStack(),
                    new ItemCreator(Material.DIAMOND_CHESTPLATE)
                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                            .addEnchant(Enchantment.DURABILITY, 2)
                            .toItemStack(),
                    new ItemCreator(Material.DIAMOND_HELMET)
                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                            .addEnchant(Enchantment.DURABILITY, 2)
                            .toItemStack()
            }),
    ARCHER("archer", 20, Material.BOW,
            Arrays.asList(
                    new ItemCreator(Material.BOW)
                            .addEnchant(Enchantment.ARROW_INFINITE, 1)
                            .toItemStack(),
                    new ItemStack(Material.GOLDEN_APPLE, 2),
                    new ItemStack(Material.ARROW)
            ),
            new ItemStack[]{
                    new ItemStack(Material.LEATHER_BOOTS),
                    new ItemStack(Material.LEATHER_LEGGINGS),
                    new ItemStack(Material.LEATHER_CHESTPLATE),
                    new ItemStack(Material.LEATHER_HELMET)
            }),
    GAPPLE("gapple", 28, Material.GOLDEN_APPLE,
            Arrays.asList(
                    new ItemCreator(Material.DIAMOND_SWORD)
                            .addEnchant(Enchantment.DAMAGE_ALL, 5)
                            .addEnchant(Enchantment.DURABILITY, 3)
                            .addEnchant(Enchantment.FIRE_ASPECT, 2)
                            .toItemStack(),
                    new ItemStack(Material.GOLDEN_APPLE, 8, (byte) 1),
                    new ItemCreator(Material.DIAMOND_HELMET)
                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                            .toItemStack(),
                    new ItemCreator(Material.DIAMOND_CHESTPLATE)
                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                            .toItemStack(),
                    new ItemCreator(Material.DIAMOND_LEGGINGS)
                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                            .toItemStack(),
                    new ItemCreator(Material.DIAMOND_BOOTS)
                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                            .toItemStack()
            ),
            new ItemStack[]{
                    new ItemCreator(Material.DIAMOND_BOOTS)
                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                            .toItemStack(),
                    new ItemCreator(Material.DIAMOND_LEGGINGS)
                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                            .toItemStack(),
                    new ItemCreator(Material.DIAMOND_CHESTPLATE)
                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                            .toItemStack(),
                    new ItemCreator(Material.DIAMOND_HELMET)
                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                            .toItemStack()
            });

    private final String name;
    private final int slot;
    private final Material icon;
    private final ItemStack[] items;
    private final ItemStack[] armors;

    DuelKits(String name, int slot, Material icon, List<ItemStack> items, ItemStack[] armors) {
        this.name = name;
        this.slot = slot;
        this.icon = icon;
        this.items = new ItemStack[36];
        items.forEach(itemStack -> this.items[items.indexOf(itemStack)] = itemStack);
        this.items[8] = new ItemStack(Material.COOKED_BEEF, 16);
        this.armors = armors;
        if (this.name.equals("nodebuff")) {
            for (int i = 0; i<36; i++)
                if (this.items[i] == null)
                    this.items[i] = new ItemCreator(Material.SPLASH_POTION)
                            .setPotion(PotionType.INSTANT_HEAL, true)
                            .toItemStack();
        }
    }

    public int getSlot() {
        return slot;
    }

    public String getName() {
        return name;
    }

    public Material getIcon() {
        return icon;
    }

    public void apply(Player player) {
        player.getInventory().setContents(this.items);
        player.getInventory().setArmorContents(this.armors);
    }
}
