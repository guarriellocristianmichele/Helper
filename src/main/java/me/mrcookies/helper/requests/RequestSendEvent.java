package me.mrcookies.helper.requests;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class RequestSendEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (e.getChannel().getIdLong() != References.idRequests) return;

        Message msg = e.getMessage();
        User usr = e.getAuthor();

        if (e.getMember().getRoles().stream().anyMatch(role -> role.getIdLong() == Core.getConfig().getYml().getLong("Roles.utility"))) {

            usr.openPrivateChannel().queue((ch) -> {
                Methods.sendSENT(ch, "Request", "You can't create a request.");
                ch.close().queue();
            });

            msg.delete().queue();
            return;
        }

        if (msg.getContentRaw().startsWith(References.prefix)) {

            usr.openPrivateChannel().queue((ch) -> {
                Methods.sendSENT(ch, "Request", "You can't execute commands in requests channel.");
                ch.close().queue();
            });

            msg.delete().queue();
            return;
        }

        if (msg.getContentRaw().length() < 15) {

            usr.openPrivateChannel().queue((ch) -> {
                Methods.sendSENT(ch, "Request", "Description too short **(Min 15 letters)**.");
                ch.close().queue();
            });

            msg.delete().queue();
            return;
        }

        TextChannel channel = e.getChannel();

        msg.delete().queue();
        MessageEmbed mse = sendRequest(usr, e.getMessage().getContentRaw());
        channel.sendMessage(mse).queue((message -> {
            message.addReaction(e.getGuild().getEmoteById(References.like)).queue();
            message.addReaction(e.getGuild().getEmoteById(References.dislike)).queue();
        }));
    }

    private MessageEmbed sendRequest(User usr, String desc) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Request", null, "https://i.imgur.com/p3owVKR.png");
        builder.setDescription(desc);
        builder.setColor(Color.decode("#2ecc71"));
        builder.setFooter("Helper • Request created by " + usr.getName(), "https://i.imgur.com/nepS3Lp.jpg");
        MessageEmbed msg = builder.build();
        return msg;
    }

}


