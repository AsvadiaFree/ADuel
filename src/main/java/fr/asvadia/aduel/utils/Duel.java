package fr.asvadia.aduel.utils;

import fr.asvadia.aduel.Main;
import fr.asvadia.aduel.modules.DuelParams;
import fr.asvadia.aduel.utils.file.FileManager;
import fr.asvadia.aduel.utils.file.Files;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Duel {
    public static HashMap<Player, ItemStack[]> playerInventory = new HashMap<>();
    public static HashMap<Player, Duel> duelPlayers = new HashMap<>();
    public static List<DuelParams> duelParams = new ArrayList<>();
    private boolean active;
    private DuelKits kit;
    private final HashMap<Player, Player> PLAYERS;
    private Player winner;

    public Duel(Player p1, Player p2) {
        this.active = false;
        this.kit = DuelKits.values()[0];
        this.PLAYERS = new HashMap<>();

        YamlConfiguration lang = FileManager.getValues().get(Files.Lang);

        p1.sendMessage(lang.getString("AcceptInvite").replaceAll("%player%", p2.getName()));

        this.PLAYERS.put(p1, p2);
        this.PLAYERS.put(p2, p1);
        // set duel and remove invitation
        duelPlayers.put(p1, this);
        duelPlayers.put(p2, this);

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

    public static HashMap<Player, ItemStack[]> getPlayerInventory() {
        return playerInventory;
    }

    public static HashMap<Player, Duel> getDuelPlayers() {
        return duelPlayers;
    }

    public static List<DuelParams> getDuelParams() {
        return duelParams;
    }

    public static void setDuelParams(List<DuelParams> duelParams) {
        Duel.duelParams = duelParams;
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
}
