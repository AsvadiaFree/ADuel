package fr.asvadia.aduel.commands;

import fr.asvadia.aduel.utils.Duel;
import fr.asvadia.aduel.utils.file.FileManager;
import fr.asvadia.aduel.utils.file.Files;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class DuelCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p))
            return true;

        if (args.length == 0) return false;

        YamlConfiguration lang = FileManager.getValues().get(Files.Lang);

        switch (args[0].toLowerCase()) {
            case "accept" -> {
                if (args.length == 1) return true;

                Player dueLP = Bukkit.getPlayer(args[1]);
                if (dueLP != null) {
                    if (Duel.duelInvitations.containsKey(p)
                            && Duel.duelInvitations.get(p).contains(dueLP)) {
                        if (!Duel.duelPlayers.containsKey(p)
                                && !Duel.duelPlayers.containsKey(dueLP)) {
                            Duel.duelPlayers.put(p, new Duel(p, dueLP));
                            Duel.openDuelInv(p);
                        } else p.sendMessage(lang.getString("DuelAlready"));
                    } else p.sendMessage(lang.getString("DontReceiveInvite"));
                }
            }

            case "deny" -> {
                if (args.length == 1) return true;

                Player dueLP = Bukkit.getPlayer(args[1]);
                if (dueLP != null) {
                    if (Duel.duelInvitations.containsKey(p)
                            && Duel.duelInvitations.get(p).contains(dueLP)) {
                        Duel.duelInvitations.get(p).remove(dueLP);
                        dueLP.sendMessage(lang.getString("DenyInvite").replaceAll("%player%", p.getName()));
                    } else p.sendMessage(lang.getString("DontReceiveInvite"));
                }
            }

            default -> {
                Player dueLP = Bukkit.getPlayer(args[0]);
                if (dueLP != null) {
                    if (dueLP != p) {
                        if (!Duel.duelPlayers.containsKey(dueLP)) {
                            if (!Duel.duelPlayers.containsKey(p)) {
                                if (!Duel.duelInvitations.containsKey(dueLP)
                                        || !Duel.duelInvitations.get(dueLP).contains(p)) {
                                    Duel.duelInvitations.putIfAbsent(dueLP, new ArrayList<>());
                                    Duel.duelInvitations.get(dueLP).add(p);

                                    TextComponent accept = new TextComponent(lang.getString("Invite.Accept.Text").replaceAll("%player%", p.getName()));
                                    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + cmd.getLabel() + " accept " + p.getName()));
                                    accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(lang.getString("Invite.Accept.HoverText"))));

                                    TextComponent deny = new TextComponent(lang.getString("Invite.Deny.Text").replaceAll("%player%", p.getName()));
                                    deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + cmd.getLabel() + " deny " + p.getName()));
                                    deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(lang.getString("Invite.Deny.HoverText"))));

                                    dueLP.spigot().sendMessage(new TextComponent(lang.getString("Invite.Text").replaceAll("%player%", p.getName())),
                                            accept,
                                            new TextComponent(" - "),
                                            deny);
                                    p.sendMessage(lang.getString("SendInvite").replaceAll("%player%", dueLP.getName()));
                                } else p.sendMessage(lang.getString("AlreadySendInvite"));
                            } else p.sendMessage(lang.getString("DuelAlready"));
                        } else p.sendMessage(lang.getString("CantSendBecauseDuel"));
                    } else p.sendMessage(lang.getString("CantDuelWithYou"));
                }
            }
        }
        return false;
    }
}
