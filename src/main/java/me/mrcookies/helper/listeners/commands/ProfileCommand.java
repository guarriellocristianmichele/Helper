package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Instant;

public class ProfileCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "profile")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();
            User usr = e.getAuthor();
            Member mem = e.getMember();

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length > 2) {
                Methods.sendErrorMessage(channel, "Use • `profile | profile [@User]`");
                return;
            }

            if (msg.length == 1) {
                sendProfile(channel, usr, mem, e.getGuild());
                return;
            }

            if (msg.length == 2) {

                if (e.getMessage().getMentionedMembers().isEmpty()) {
                    Methods.sendErrorMessage(channel, "Use • `profile [@User]`");
                    return;
                }

                User target = e.getMessage().getMentionedUsers().get(0);

                if (target.isBot()) {
                    Methods.sendErrorMessage(channel, "Invalid user.");
                    return;
                }

                Member targ = e.getGuild().getMember(target);

                sendProfile(channel, target, targ, e.getGuild());
            }

        }

    }

    private void sendProfile(TextChannel channel, User usr, Member mem, Guild guild) {
        EmbedBuilder builder = new EmbedBuilder();
        String url;
        builder.setAuthor("Profile of " + usr.getName(), null, "https://i.imgur.com/IUFgzzq.png");
        builder.appendDescription("```There will be all informations about " + usr.getName() + ".```");

        if (mem.getNickname() != null) {
            builder.addField("Nickname:", "`" + mem.getNickname() + "`", false);
        }

        builder.addField("Tag:", "`" + usr.getAsTag() + "`", false);
        builder.addField("ID:", "`" + usr.getIdLong() + "`", false);
        builder.addField("Role:", "`" + mem.getRoles().get(0).getName() + "`", true);
        builder.addField("Coins:", "`" + Core.getMySQL().getCoins(usr.getIdLong()) + "`", true);
        builder.addField("Warns:", "`" + Core.getMySQL().getWarns(usr.getIdLong()) + "/3`", false);
        builder.appendDescription("\n");
        builder.setColor(Color.decode("#fdcb6e"));
        builder.setFooter("Helper", "https://i.imgur.com/nepS3Lp.jpg");
        builder.setTimestamp(Instant.now());

        if (usr.getDefaultAvatarUrl() != null) {
            builder.setThumbnail(usr.getAvatarUrl());
        }

        if (!Core.getMySQL().hasSocial(usr.getIdLong())) {
            builder.appendDescription("```No social linked to this account. \nIf you have one use " + References.prefix + "link```");
        }

        if (Core.getMySQL().hasSocial(usr.getIdLong(), "facebook")) {
            url = Core.getMySQL().getString("members", "facebook", "id_long", String.valueOf(usr.getIdLong()));
            builder.addField(guild.getEmoteById(References.facebook).getAsMention(), "[Click here.](" + url + " \"Profile\")", true);
        }

        if (Core.getMySQL().hasSocial(usr.getIdLong(), "instagram")) {
            url = Core.getMySQL().getString("members", "instagram", "id_long", String.valueOf(usr.getIdLong()));
            builder.addField(guild.getEmoteById(References.instagram).getAsMention(), "[Click here.](" + url + " \"Profile\")", true);
        }

        channel.sendMessage(builder.build()).queue();
    }

}
