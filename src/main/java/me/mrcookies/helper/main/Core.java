package me.mrcookies.helper.main;

import me.mrcookies.helper.configuration.ConfigManager;
import me.mrcookies.helper.database.MySQL;
import me.mrcookies.helper.listeners.ListenerManager;
import me.mrcookies.helper.listeners.events.BotStartEvent;
import me.mrcookies.helper.listeners.events.BotStatusEvent;
import me.mrcookies.helper.redeem.MessageRedeemEvent;
import me.mrcookies.helper.tickets.HelpMessageEvent;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import java.io.File;

public class Core {

    private static JDA jda;
    private static ConfigManager config;
    private static MySQL mysql;
    private static ListenerManager eventManager;

    public static void main(String[] args) throws Exception {
        setupConfig();
        setupFolders();

        if (config.getYml().getString("Settings.token").isEmpty()) {
            System.out.println("Helper > NO BOT TOKEN FOUND! Cannot start :C");
            return;
        }

        mysql = new MySQL();
        eventManager = new ListenerManager();

        mysql.initialize();
        System.out.println("Helper > Database ready.");
        jda = new JDABuilder(AccountType.BOT).setToken(config.getYml().getString("Settings.token")).addEventListener(new BotStartEvent(),
                new BotStatusEvent(), new HelpMessageEvent(), new MessageRedeemEvent()).build().awaitReady();
        eventManager.initialize();
        jda.getPresence().setGame(Game.playing("mc.titanetwork.eu"));
        jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
        System.out.println("Helper > Bot ready to use.");
    }

    private static void setupConfig() {
        config = new ConfigManager("config", "Helper");
        config.addDefault("Prefix", "");
        config.addDefault("Settings.token", "");
        config.addDefault("Settings.discord-link", "");
        config.addDefault("Server.ip", "");
        config.addDefault("Server.port", "");
        config.addDefault("Roles.permission", "");
        config.addDefault("Roles.helper", "");
        config.addDefault("Roles.utility", "");
        config.addDefault("Reactions.like", "");
        config.addDefault("Reactions.dislike", "");
        config.addDefault("Roles.no-tag", "");
        config.addDefault("Games.Countgame.channel", "");
        config.addDefault("Games.Countgame.surprise", 10);
        config.addDefault("Games.Countgame.reference", 1);
        config.addDefault("Games.Countgame.last-author", "");
        config.addDefault("Games.QuickMath.channel", "");
        config.addDefault("Games.QuickMath.reference", "");
        config.addDefault("Games.HigherLower.channel", "");
        config.addDefault("Games.HigherLower.reference", "");
        config.addDefault("MySQL.host", "");
        config.addDefault("MySQL.port", "");
        config.addDefault("MySQL.database", "");
        config.addDefault("MySQL.username", "");
        config.addDefault("MySQL.password", "");
        config.addDefault("Channels.support-channel", "");
        config.addDefault("Roles.support", "");
        config.addDefault("Roles.muted", "");
        config.addDefault("Roles.player", "");
        config.addDefault("Roles.support", "");
        config.addDefault("Channels.command-channel", "");
        config.addDefault("Channels.logs-channel", "");
        config.addDefault("Channels.command-channel-staff", "");
        config.addDefault("Channels.redeem-channel", "");
        config.addDefault("Channels.status-channel", "");
        config.addDefault("Channels.tickets-channel", "");
        config.addDefault("Channels.rules-channel", "");
        config.addDefault("Reactions.online", "");
        config.addDefault("Reactions.offline", "");
        config.addDefault("Reactions.loading", "");
        config.addDefault("Category.tickets", "");
        config.addDefault("Channels.requests-channel", "");
        config.saveDefaults();
    }

    private static void setupFolders() {

        if (!new File("Helper/Images").exists()) {
            new File("Helper/Images").mkdir();
        }

        if (!new File("Helper/Chatlogs").exists()) {
            new File("Helper/Chatlogs").mkdir();
        }

    }

    public static ConfigManager getConfig() {
        return config;
    }

    public static String getVersion() {
        return "2.2.0";
    }

    public static JDA getJDA() {
        return jda;
    }

    public static MySQL getMySQL() {
        return mysql;
    }

}
