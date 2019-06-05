package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Instant;

public class HelpCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().equalsIgnoreCase(References.prefix + "help")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            User usr = e.getAuthor();

            usr.openPrivateChannel().queue((ch) -> sendHelp(ch, usr, e.getGuild()));
            Methods.sendSENT(e.getChannel(), "Help", "You have received a direct message.");
        }

    }

    private void sendHelp(PrivateChannel channel, User usr, Guild guild) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Commands:", null, "https://i.imgur.com/UZREy9n.png");
        builder.setColor(Color.decode("#e67e22"));
        builder.setFooter("Helper • Developed by Mr_Cookies", "https://i.imgur.com/nepS3Lp.jpg");

        if (guild.getMember(usr).getRoles().stream().anyMatch(role -> role.getIdLong() == Core.getConfig().getYml().getLong("Roles.permission"))) {
            builder.appendDescription("**Prefix:** `" + References.prefix + "`\n\n" +
                    "`• about` - Bot and server information.\n" +
                    "`• clear` - Clear chat messages.\n" +
                    "`• announce` - Announce something.\n" +
                    "`• send` - Say something.\n" +
                    "`• random` - Random user or number.\n" +
                    "`• role` - Add or remove user's roles.\n" +
                    "`• file` - Send files.\n" +
                    "`• countgame` - Manage countgame.\n" +
                    "`• coins` - Coins owned.\n" +
                    "`• eco` - Manage user's economy.\n" +
                    "`• lb` - Leaderboard.\n" +
                    "`• ping` - Ping and WebSocket.\n" +
                    "`• pay` - Pay user.\n" +
                    "`• daily` - Claim daily reward.\n" +
                    "`• profile` - Profile information.\n" +
                    "`• rob` - Rob user's coins.\n" +
                    "`• reset` - Reset user's account.\n" +
                    "`• oracle` - Answers every question.\n" +
                    "`• sd` - Shutdown bot.\n" +
                    "`• mute` - Mute user.\n" +
                    "`• unmute` - Unmute user.\n" +
                    "`• warn` - Warn user.\n" +
                    "`• id` - ID of emotes and roles.\n" +
                    "`• license` - Create licenses.\n" +
                    "`• presence` - Set presence.\n" +
                    "`• info` - Get user's info.\n" +
                    "`• giveaway` - Manage giveaways.\n" +
                    "`• chatlog` - Create a chatlog."
            );

            channel.sendMessage(builder.build()).queue();
            return;
        }

        builder.appendDescription("**Prefix:** `" + References.prefix + "`\n\n" +
                "`• about` - Bot and server information.\n" +
                "`• coins` - Coins owned.\n" +
                "`• lb` - Leaderboard.\n" +
                "`• ping` - Ping and WebSocket.\n" +
                "`• pay` - Pay user.\n" +
                "`• daily` - Claim daily reward.\n" +
                "`• profile` - Profile information.\n" +
                "`• rob` - Rob user's coins.\n" +
                "`• oracle` - Answers every question."
        );

        channel.sendMessage(builder.build()).queue();
    }

}
