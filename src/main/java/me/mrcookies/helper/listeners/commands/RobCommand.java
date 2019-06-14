package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class RobCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "rob")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();

            User usr = e.getAuthor();

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length != 3) {
                Methods.sendErrorMessage(channel, "Use • `rob [@User] [amount]`");
                return;
            }

            if (e.getMessage().getMentionedMembers().isEmpty()) {
                Methods.sendErrorMessage(channel, "Use • `rob [@User] [amount]`");
                return;
            }

            User target = e.getMessage().getMentionedUsers().get(0);

            if (target.isBot()) {
                Methods.sendErrorMessage(channel, "Invalid user.");
                return;
            }

            Long now = System.currentTimeMillis();
            Long start = Core.getMySQL().getLong("members", "rob_start", "id_long", String.valueOf(usr.getIdLong()));

            if (start == 0) {

                int n;

                try {
                    n = Integer.parseInt(msg[2]);
                } catch (NumberFormatException ex) {
                    Methods.sendErrorMessage(channel, "Invalid number");
                    return;
                }

                tryRob(channel, n, target, usr, now);
                return;
            }

            long sub = now - start;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(sub);

            if (seconds > 86400) {

                int n;

                try {
                    n = Integer.parseInt(msg[2]);
                } catch (NumberFormatException ex) {
                    Methods.sendErrorMessage(channel, "Invalid number");
                    return;
                }

                tryRob(channel, n, target, usr, now);
                return;
            }

            Methods.sendErrorMessage(channel, "You have already robbed, wait `24h`, command executed `" + Methods.getTimeFormat(sub).toLowerCase() + "` ago.");
        }

    }

    private void tryRob(TextChannel channel, int n, User target, User usr, Long now) {

        if (target.getIdLong() == usr.getIdLong()) {
            Methods.sendErrorMessage(channel, "You can't rob yourself.");
            return;
        }

        if (Core.getMySQL().getCoins(target.getIdLong()) < n) {
            Methods.sendErrorMessage(channel, target.getAsMention() + " hasn't `" + n + "` coins.");
            return;
        }

        if (Core.getMySQL().getCoins(usr.getIdLong()) < n) {
            Methods.sendErrorMessage(channel, "You don't have `" + n + "` coins." +
                    "\n``To rob is needed the amount of the rob.``");
            return;
        }

        int ran = Methods.getRandom(100, 1);
        int coin = Core.getMySQL().getCoins(usr.getIdLong());

        if (ran > 35) {

            int lost = Methods.getRandom(n, 1);
            int newCoins = coin - lost;

            Core.getMySQL().setInt("members", "coins", newCoins, "id_long", String.valueOf(usr.getIdLong()));
            sendRobMSG(channel, "Thief", usr.getAsMention() + " lost `" + lost +
                    "` coins trying to rob to " + target.getAsMention(), "#e74c3c");
            return;
        }

        int coinz = Core.getMySQL().getCoins(target.getIdLong());
        int lost = Methods.getRandom(n, 1);
        int newLostCoins = coinz - lost;
        int newCoins = coin + lost;

        Core.getMySQL().setInt("members", "coins", newLostCoins, "id_long", String.valueOf(target.getIdLong()));
        Core.getMySQL().setInt("members", "coins", newCoins, "id_long", String.valueOf(usr.getIdLong()));
        Core.getMySQL().setLong("members", "rob_start", now, "id_long", String.valueOf(usr.getIdLong()));
        sendRobMSG(channel, "Thief", usr.getAsMention() + " robbed `" + lost +
                "` coins to " + target.getAsMention(), "#2ecc71");
    }

    private void sendRobMSG(TextChannel channel, String title, String Description, String color) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(title, null, "https://i.imgur.com/hES9Aha.png");
        builder.setDescription(Description);
        builder.setColor(Color.decode(color));
        builder.setFooter(References.h, "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue();
    }

}
