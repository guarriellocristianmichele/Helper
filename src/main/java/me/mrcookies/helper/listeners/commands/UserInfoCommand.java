package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.time.OffsetDateTime;

public class UserInfoCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "info")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();

            if (!Methods.hasPermission(e.getMember(), channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length > 2) {
                Methods.sendErrorMessage(channel, "Use • `info | info [@User]`");
                return;
            }

            if (msg.length == 1) {
                sendUserInfo(channel, e.getMember());
                return;
            }

            if (msg.length == 2) {

                if (e.getMessage().getMentionedMembers().isEmpty()) {
                    Methods.sendErrorMessage(channel, "Use • `info [@User]`");
                    return;
                }

                User target = e.getMessage().getMentionedUsers().get(0);

                if (target.isBot()) {
                    Methods.sendErrorMessage(channel, "Invalid user.");
                    return;
                }

                Member targ = e.getGuild().getMember(target);

                sendUserInfo(channel, targ);
            }

        }

    }

    private void sendUserInfo(TextChannel channel, Member mem) {

        User usr = mem.getUser();
        int warns = Core.getMySQL().getWarns(usr.getIdLong());
        OffsetDateTime date = mem.getJoinDate();
        String format = Methods.getCap(date.getDayOfWeek().toString().substring(0, 3).toLowerCase())
                + ", " + Methods.getCap(date.getMonth().name().toLowerCase()) + " " + date.getDayOfMonth() + ", " + date.getYear();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(usr.getName() + "'s info", null, "https://i.imgur.com/IUFgzzq.png");

        if (mem.isOwner()) {
            builder.addField("Owner:", "Yes", false);
        }

        if (mem.getNickname() != null) {
            builder.addField("Nickname:", "`" + mem.getNickname() + "`", false);
        }

        builder.addField("Tag:", usr.getAsTag(), false);
        builder.addField("ID:", "`" + usr.getIdLong() + "`", false);
        builder.addField("Joined:", format, false);
        builder.addField("Status:", Methods.getCap(mem.getOnlineStatus().name().toLowerCase().replace("_", " ")), false);
        builder.addField("Role:", mem.getRoles().get(0).getName(), false);
        builder.addField("Coins:", "`" + Core.getMySQL().getCoins(usr.getIdLong()) + "`", false);

        if (warns > 3) {
            builder.addField("Active Punishment:", "Muted", false);
        } else {
            builder.addField("Warns:", "`" + warns + "/3`", false);
        }

        builder.setColor(Color.decode("#fdcb6e"));
        builder.setFooter(References.h + " • Info", "https://i.imgur.com/nepS3Lp.jpg");

        if (usr.getDefaultAvatarUrl() != null) {
            builder.setThumbnail(usr.getAvatarUrl());
        }

        channel.sendMessage(builder.build()).queue();
    }

}
