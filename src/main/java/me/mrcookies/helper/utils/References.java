package me.mrcookies.helper.utils;

import me.mrcookies.helper.main.Core;

public class References {

    public static final String host = Core.getConfig().getYml().getString("MySQL.host");
    public static final int port = Core.getConfig().getYml().getInt("MySQL.port");
    public static final String database = Core.getConfig().getYml().getString("MySQL.database");
    public static final String username = Core.getConfig().getYml().getString("MySQL.username");
    public static final String password = Core.getConfig().getYml().getString("MySQL.password");
    public static String prefix = Core.getConfig().getYml().getString("Prefix");
    public static Long idCountGame = Core.getConfig().getYml().getLong("Games.Countgame.channel");
    public static Long idQuickMath = Core.getConfig().getYml().getLong("Games.QuickMath.channel");
    public static Long idHigherLower = Core.getConfig().getYml().getLong("Games.HigherLower.channel");
    public static Long idSupportChannel = Core.getConfig().getYml().getLong("Channels.support-channel");
    public static Long idCommands = Core.getConfig().getYml().getLong("Channels.command-channel");
    public static Long idChatLogs = Core.getConfig().getYml().getLong("Channels.chat-logs-channel");
    public static Long idStaffCommands = Core.getConfig().getYml().getLong("Channels.command-channel-staff");
    public static Long idRedeem = Core.getConfig().getYml().getLong("Channels.redeem-channel");
    public static Long idRules = Core.getConfig().getYml().getLong("Channels.rules-channel");
    public static Long idStatus = Core.getConfig().getYml().getLong("Channels.status-channel");
    public static Long idTickets = Core.getConfig().getYml().getLong("Channels.tickets-channel");
    public static Long idRequests = Core.getConfig().getYml().getLong("Channels.requests-channel");
    public static Long online = Core.getConfig().getYml().getLong("Reactions.online");
    public static Long offline = Core.getConfig().getYml().getLong("Reactions.offline");
    public static Long loading = Core.getConfig().getYml().getLong("Reactions.loading");
    public static Long like = Core.getConfig().getYml().getLong("Reactions.like");
    public static Long dislike = Core.getConfig().getYml().getLong("Reactions.dislike");
    public static Long facebook = Core.getConfig().getYml().getLong("Reactions.facebook");
    public static Long instagram = Core.getConfig().getYml().getLong("Reactions.instagram");

}
