package tv.marius.easyteamchat;
/*

Project: EasyTeamChat
Author: Marius
Created: 20.06.2020, 11:25

*/

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class TeamChatCommand extends Command {
    private final String pr;
    private final ArrayList<ProxiedPlayer> teamchat = new ArrayList<>();
    private final Configuration config;

    public TeamChatCommand(String name, String prefix) {
        super(name);
        this.pr = prefix;
        this.config = Plugin.getConfig();
        setupConfig();
    }

    private void setupConfig() {
        if (config.get("pattern") == null)
            config.set("pattern", "%prefix% &a%displayname% &8=> &7%message%");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer executor = (ProxiedPlayer)commandSender;
            if (executor.hasPermission("easyteamchat.use")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("list")) {
                        StringBuilder builder = new StringBuilder();
                        if (!teamchat.isEmpty()) {
                            builder.append(pr).append("Online Players logged in to teamchat:");
                            teamchat.forEach(p -> builder.append("\n").append(pr).append("§8- §e").append(p.getDisplayName()).append(" §8[§2").append(p.getServer().getInfo().getName()).append("§8]"));
                        } else {
                            builder.append(pr).append("§cNo Player online");
                        }
                        executor.sendMessage(translate(builder.toString()));
                    } else if (args[0].equalsIgnoreCase("login")) {
                        if (teamchat.contains(executor)) {
                            executor.sendMessage(translate(pr + "§cYou already logged in into teamchat"));
                        } else {
                            teamchat.add(executor);
                            executor.sendMessage(translate(pr + "§cLogged in"));
                            teamchat.forEach(p -> p.sendMessage(translate(pr + "§e" + executor.getDisplayName() + " §alogged in")));
                        }
                    } else if (args[0].equalsIgnoreCase("logout")) {
                        if (!teamchat.contains(executor)) {
                            executor.sendMessage(translate(pr + "§cYou aren't logged in"));
                        } else {
                            teamchat.remove(executor);
                            executor.sendMessage(translate(pr + "§cLogged out"));
                            teamchat.forEach(p -> p.sendMessage(translate(pr + "§e" + executor.getDisplayName() + " §clogged out")));
                        }
                    }
                } else if (args.length >= 2 && args[0].equalsIgnoreCase("message")) {
                    if (teamchat.contains(executor)) {
                        AtomicReference<String> message = new AtomicReference<>("");
                        for (int i = 1; i < args.length; i++) message.set(message.get() + args[i] + " ");
                        String msg = ChatColor.translateAlternateColorCodes('&', config.getString("pattern")
                                    .replaceAll("%prefix%", pr).replaceAll("%displayname%", executor.getDisplayName())
                                    .replaceAll("%message%", message.get()));
                        teamchat.forEach(p -> p.sendMessage(translate(msg)));
                    } else {
                        executor.sendMessage(translate(pr + "§cYou have to be logged in to send messages"));
                    }
                } else {
                    executor.sendMessage(translate(pr + "§cUse: §8/teamchat <message / list / login / logout> <Message>"));
                }
            } else {
                executor.sendMessage(translate(pr + "§cNo permission"));
            }
        } else {
            commandSender.sendMessage(translate(pr + "§cOnly Players can execute EasyTeamChat-Commands"));
        }
    }

    private BaseComponent translate(String message) { return new TextComponent(message); }

}
