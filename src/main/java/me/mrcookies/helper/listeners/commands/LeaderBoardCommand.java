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
import java.util.ArrayList;

public class LeaderBoardCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().equalsIgnoreCase(References.prefix + "lb")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            sendLeaderboard(e.getChannel(), e.getAuthor());
        }

    }

    private void sendLeaderboard(TextChannel channel, User user) {

        ArrayList<Long> ids = Core.getMySQL().getTop(0, 0, "members", "id_long", "coins", false);
        int cont = 1;
        int pos = 0;

        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Stats", null, "https://i.imgur.com/YWQIikM.png");
        builder.setColor(Color.decode("#9b59b6"));
        builder.appendDescription("```Here are shown top 3 users.```\n");
        builder.setThumbnail("https://i.imgur.com/8h4hvKY.png");

        for (Long id : ids) {

            User usr = Core.getJDA().getUserById(id);
            int coins = Core.getMySQL().getCoins(id);

            if (user.getIdLong() == id) {

                pos = cont;

                if (cont == 1) {
                    builder.appendDescription("\uD83E\uDD47 " + usr.getAsMention() + " - `" + coins
                            + "` coins \u2B05\n\n");
                    cont++;
                    continue;
                }

                if (cont == 2) {
                    builder.appendDescription("\uD83E\uDD48 " + usr.getAsMention() + " - `" + coins
                            + "` coins \u2B05\n\n");
                    cont++;
                    continue;
                }

                if (cont == 3) {
                    builder.appendDescription("\uD83E\uDD49 " + usr.getAsMention() + " - `" + coins
                            + "` coins \u2B05\n\n");
                    cont++;
                    continue;
                }

                cont++;
                continue;
            }

            if (cont == 1) {
                builder.appendDescription("\uD83E\uDD47 " + usr.getAsMention() + " - `" + coins + "` coins\n\n");
                cont++;
                continue;
            }

            if (cont == 2) {
                builder.appendDescription("\uD83E\uDD48 " + usr.getAsMention() + " - `" + coins + "` coins\n\n");
                cont++;
                continue;
            }

            if (cont == 3) {
                builder.appendDescription("\uD83E\uDD49 " + usr.getAsMention() + " - `" + coins + "` coins\n\n");
                cont++;
                continue;
            }

            cont++;
        }

        if (pos > 3) {
            builder.addBlankField(true);
            builder.addField("Your Position:", "• **" + pos + "** " + user.getAsMention() + " - `" + Core.getMySQL().getCoins(user.getIdLong()) + "` coins", false);
        }

        builder.setFooter("Helper", "https://i.imgur.com/nepS3Lp.jpg");
        builder.setTimestamp(Instant.now());

        channel.sendMessage(builder.build()).queue();
    }

}
