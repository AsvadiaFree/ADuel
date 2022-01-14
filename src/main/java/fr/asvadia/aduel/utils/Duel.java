package fr.asvadia.aduel.utils;

import fr.asvadia.aduel.Main;
import fr.asvadia.aduel.modules.*;
import fr.asvadia.aduel.utils.file.FileManager;
import fr.asvadia.aduel.utils.file.Files;
import fr.asvadia.api.bukkit.menu.inventory.AInventoryGUI;
import fr.skyfighttv.simpleitem.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Duel {
    public static HashMap<Player, ItemStack[]> playerInventory = new HashMap<>();
    public static HashMap<Player, Duel> duelPlayers = new HashMap<>();
    public static HashMap<Player, List<Player>> duelInvitations = new HashMap<>();
    private static final List<DuelParams> duelParams = Arrays.asList(new NoKnockback(), new VKnockback(), new NoFall(), new NoFood(), new TpPvP(), new NoFire(), new NaturalRegen(), new OneShoot());
    private static final int[] duelParamsSlot = new int[]{15, 16, 24, 25, 33, 34, 42, 43};
    private static AInventoryGUI.Builder inventory;
    private final List<DuelParams> duelP;
    private boolean active;
    private DuelKits kit;
    private final HashMap<Player, Player> PLAYERS;
    private Player winner;

    public Duel(Player p1, Player p2) {
        this.active = false;
        this.kit = DuelKits.values()[0];
        this.PLAYERS = new HashMap<>();
        this.duelP = new ArrayList<>();
        duelParams.forEach(duelParams1 -> duelP.add(duelParams1.duplicate()));

        YamlConfiguration lang = FileManager.getValues().get(Files.Lang);

        p1.sendMessage(lang.getString("AcceptInvite").replaceAll("%player%", p2.getName()));

        this.PLAYERS.put(p1, p2);
        this.PLAYERS.put(p2, p1);
        duelInvitations.get(p1).remove(p2);
        duelPlayers.put(p1, this);
        duelPlayers.put(p2, this);

        YamlConfiguration config = FileManager.getValues().get(Files.Config);

        if (inventory == null) {
            AInventoryGUI.Builder inv = AInventoryGUI.builder()
                    .title(lang.getString("GUI.Title"))
                    .size(54);

            int slot = config.getInt("GUI.Items.SOn.Slot");
            inv.item(slot, new ItemCreator(Material.matchMaterial(config.getString("GUI.Items.SOn.Material")))
                    .setName(lang.getString("GUI.Items.SOn.Title"))
                    .setLore(lang.getStringList("GUI.Items.SOn.Lore"))
                    .toItemStack());
            inv.clickButton(slot, (player, aInventoryGUI, clickType) -> {
                if (Duel.getDuelPlayers().containsKey(player)
                        && Duel.getDuelPlayers().get(player) != null)
                    Duel.getDuelPlayers().get(player).start();
                player.closeInventory();
            });

            slot = config.getInt("GUI.Items.SOff.Slot");
            inv.item(slot, new ItemCreator(Material.matchMaterial(config.getString("GUI.Items.SOff.Material")))
                    .setName(lang.getString("GUI.Items.SOff.Title"))
                    .setLore(lang.getStringList("GUI.Items.SOff.Lore"))
                    .toItemStack());
            inv.clickButton(slot, (player, aInventoryGUI, clickType) -> {
                if (Duel.getDuelPlayers().containsKey(player)
                        && Duel.getDuelPlayers().get(player) != null)
                    Duel.getDuelPlayers().get(player).end(null);
                player.closeInventory();
            });

            for (DuelKits k : DuelKits.values()) {
                ItemCreator item = new ItemCreator(k.getIcon())
                        .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                        .setName(lang.getString("Kits." + k.getName().toLowerCase() + ".Title"))
                        .setLore(lang.getStringList("Kits." + k.getName().toLowerCase() + ".Lore"));
                if (k == DuelKits.values()[0])
                    item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

                inv.item(k.getSlot(), item.toItemStack());
                inv.clickButton(k.getSlot(), (player, aInventoryGUI, clickType) -> {
                   if (Duel.getDuelPlayers().containsKey(player)
                           && Duel.getDuelPlayers().get(player) != null) {
                       int s  = Duel.getDuelPlayers().get(player).getKit().getSlot();
                       Objects.requireNonNull(aInventoryGUI.getInventory().getItem(s))
                               .removeEnchantment(Enchantment.DURABILITY);
                       Duel.getDuelPlayers().get(player).setKit(k);
                       Objects.requireNonNull(aInventoryGUI.getInventory().getItem(k.getSlot()))
                               .addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                   }
                });
            }

            for (DuelParams duelParams : Duel.getDuelParams()) {
                slot = duelParamsSlot[Duel.getDuelParams().indexOf(duelParams)];
                ItemCreator item = new ItemCreator(duelParams.mDisable);
                if (Duel.getDuelPlayers().get(p1).getDuelP().get(Duel.getDuelParams().indexOf(duelParams)).active)
                    item = new ItemCreator(duelParams.mEnable);
                item.setName(duelParams.getTitle())
                        .setLore(duelParams.getLore());
                inv.item(slot, item.toItemStack());
                int finalSlot = slot;
                inv.clickButton(slot, (player, aInventoryGUI, clickType) -> {
                   Duel.getDuelPlayers().get(p1).getDuelP()
                           .get(Duel.getDuelParams().indexOf(duelParams))
                           .onClick(finalSlot, player, aInventoryGUI, clickType);
                });
            }

            inventory = inv;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!p1.isOnline())
                    end(p2);
                else if (!p2.isOnline())
                    end(p1);
                if (winner != null)
                    this.cancel();
            }
        }.runTaskTimer(Main.getInstance(), 40, 40);
    }

    public void start() {
        YamlConfiguration lang = FileManager.getValues().get(Files.Lang);
        this.active = true;
        this.PLAYERS.forEach((player, player2) -> {
            playerInventory.put(player, player.getInventory().getContents());
            Bukkit.getOnlinePlayers().forEach(player1 -> {
                if (!player1.equals(player) && !player1.equals(player2))
                    player.hidePlayer(Main.getInstance(), player1);
            });
            player.showPlayer(Main.getInstance(), player2);
            player.getInventory().clear();
            player.getInventory().setArmorContents(new ItemStack[4]);
            player.setAllowFlight(false);
            this.kit.apply(player);
            player.sendMessage(lang.getString("DuelStart"));
        });
    }

    public void end(Player winner) {
        if (this.winner != null)
            return;
        YamlConfiguration lang = FileManager.getValues().get(Files.Lang);
        this.winner = winner;
        this.PLAYERS.forEach((player, player2) -> {
            if (player == null || !player.isOnline())
                return;

            player.setHealth(20.0);
            player.setFoodLevel(20);
            player.setFireTicks(0);
            player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
            player.getInventory().clear();
            player.getInventory().setArmorContents(new ItemStack[4]);
            player.getInventory().setContents(playerInventory.get(player));

            playerInventory.remove(player);
            Bukkit.getOnlinePlayers().forEach(player1 -> player.showPlayer(Main.getInstance(), player1));

            if (winner == null || !this.active) {
                player.sendMessage(lang.getString("DuelCancel"));
            } else {
                if (player.equals(winner))
                    player.sendMessage(lang.getString("DuelWin"));
                else
                    player.sendMessage(lang.getString("DuelLose"));
            }
            getDuelPlayers().remove(player);
        });
    }

    public static void openDuelInv(Player p) {
        p.openInventory(inventory.build().getInventory());
    }

    public static HashMap<Player, ItemStack[]> getPlayerInventory() {
        return playerInventory;
    }

    public static HashMap<Player, Duel> getDuelPlayers() {
        return duelPlayers;
    }

    public static List<DuelParams> getDuelParams() {
        return duelParams;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public DuelKits getKit() {
        return kit;
    }

    public void setKit(DuelKits kit) {
        this.kit = kit;
    }

    public HashMap<Player, Player> getPLAYERS() {
        return PLAYERS;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public static HashMap<Player, List<Player>> getDuelInvitations() {
        return duelInvitations;
    }

    public List<DuelParams> getDuelP() {
        return duelP;
    }


}
