package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class DailyCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().equalsIgnoreCase(References.prefix + "daily")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();
            User usr = e.getAuthor();
            Long now = System.currentTimeMillis();
            Long start = Core.getMySQL().getLong("members", "daily_start", "id_long", String.valueOf(usr.getIdLong()));

            if (start == 0) {
                addDailyCoins(channel, usr);
                Core.getMySQL().setLong("members", "daily_start", now, "id_long", String.valueOf(usr.getIdLong()));
                return;
            }

            long sub = now - start;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(sub);

            if (seconds > 86400) {
                addDailyCoins(channel, usr);
                Core.getMySQL().setLong("members", "daily_start", now, "id_long", String.valueOf(usr.getIdLong()));
                return;
            }

            Methods.sendErrorMessage(channel, "Coins already claimed, wait `24h`, command executed `" + Methods.getTimeFormat(sub).toLowerCase() + "` ago.");
        }

    }

    private void addDailyCoins(TextChannel channel, User usr) {
        int rcoins = Methods.getRandom(100, 1);
        int sum = Core.getMySQL().getCoins(usr.getIdLong()) + rcoins;

        Core.getMySQL().setInt("members", "coins", sum, "id_long", String.valueOf(usr.getIdLong()));
        Methods.sendSimpleEmbed(channel, "Coins", usr.getAsMention() + " has claimed `" + rcoins + "` coins as daily reward.");
    }

}
