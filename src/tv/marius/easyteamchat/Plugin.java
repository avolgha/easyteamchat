package tv.marius.easyteamchat;
/*

Project: EasyTeamChat
Author: Marius
Created: 20.06.2020, 11:23

*/

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Plugin extends net.md_5.bungee.api.plugin.Plugin {

    private static Configuration config;
    private static ConfigurationProvider provider;
    private static File file;

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

    public static Configuration getConfig()  {
        if (config == null) {
            try {
                provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
                file = new File("plugins//EasyTeamChat//config.yml");
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdir();
                if (!file.exists())
                    file.createNewFile();
                config = provider.load(file);
                safeConfig();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return config;
    }

    public static void safeConfig() {
        if (config == null) getConfig();
        try {
            provider.save(config, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
