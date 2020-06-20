package tv.marius.easyteamchat;
/*

Project: EasyTeamChat
Author: Marius
Created: 20.06.2020, 11:23

*/

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class Plugin extends net.md_5.bungee.api.plugin.Plugin {
    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerCommand(this, new TeamChatCommand("teamchat", "§8[§bTeamChat§8] §7"));
        getProxy().getConsole().sendMessage(translate("§8[§bTeamChat§8] §7Enabled Plugin"));
    }

    @Override
    public void onDisable() {
        getProxy().getConsole().sendMessage(translate("§8[§bTeamChat§8] §7Disabled Plugin"));
    }

    private BaseComponent translate(String message) { return new TextComponent(message); }
}
