package me.mrcookies.helper.giveaway;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GiveawayCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "giveaway")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();

            if (!Methods.hasPermission(e.getMember(), channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length < 2) {
                Methods.sendErrorMessage(channel, "Use • `giveaway start | end | delete | info`");
                return;
            }

            TextChannel gaChannel = e.getGuild().getTextChannelById(References.idGiveaways);
            TextChannel aChannel = e.getGuild().getTextChannelById(References.idAnnouncements);
            Emote emote = e.getGuild().getEmoteById(References.check);

            switch (msg[1].toLowerCase()) {

                case "start": {

                    String[] text = e.getMessage().getContentRaw().split(" - ");

                    if (text.length != 2) {
                        Methods.sendErrorMessage(channel, "Use • `giveaway start - [prize]`");
                        return;
                    }

                    List<Message> msgs = gaChannel.getHistory().retrievePast(1).complete();

                    if (!msgs.isEmpty()) {
                        Methods.sendErrorMessage(channel, "There is already a giveaway started.");
                        return;
                    }

                    sendGiveaway(text[1], gaChannel, emote);
                    aChannel.sendMessage(e.getGuild().getPublicRole().getAsMention());
                    Methods.sendSENT(aChannel, "Giveaway", "```A giveaway has been started.```\n\n**Join it** " + gaChannel.getAsMention());
                    Methods.sendSENT(channel, "Giveaway", "Giveaway started successfully.");
                    break;
                }

                case "end": {

                    List<Message> msgs = gaChannel.getHistory().retrievePast(1).complete();

                    if (msgs.isEmpty()) {
                        Methods.sendErrorMessage(channel, "There isn't any giveaway started.");
                        return;
                    }

                    String id = String.valueOf(Core.getMySQL().getGiveawayID());

                    if (id == null) {
                        Methods.sendErrorMessage(channel, "There isn't any giveaway started.");
                        return;
                    }

                    String prize = Core.getMySQL().getString("giveaway", "prize", "id", id);
                    Message message = gaChannel.getMessageById(id).complete();
                    int joined = getJoinedUsers(message);

                    if (joined == 0) {
                        Methods.sendErrorMessage(channel, "Nobody joined the giveaway.\nIf you wanna delete it, use ``" + References.prefix + "giveaway delete``");
                        return;
                    }

                    User winner = getWinner(message);

                    if (winner == null) {
                        Methods.sendErrorMessage(channel, "No winner found.");
                        return;
                    }

                    message.delete().queue();
                    sendEndGiveaway(gaChannel, prize, winner);
                    aChannel.sendMessage(e.getGuild().getPublicRole().getAsMention());
                    Methods.sendSENT(aChannel, "Giveaway", "```The giveaway ended.```\n\n**Winner** " + winner.getAsMention());
                    Core.getMySQL().dropEntry("giveaway", "id", id);
                    Methods.sendSENT(channel, "Giveaway", "Giveaway ended successfully.");
                    break;
                }

                case "info": {

                    List<Message> msgs = gaChannel.getHistory().retrievePast(1).complete();

                    if (msgs.isEmpty()) {
                        Methods.sendErrorMessage(channel, "There isn't any giveaway started.");
                        return;
                    }

                    String ID = String.valueOf(Core.getMySQL().getGiveawayID());

                    if (ID == null) {
                        Methods.sendErrorMessage(channel, "There isn't any giveaway started.");
                        return;
                    }

                    Message message = gaChannel.getMessageById(ID).complete();
                    String prize = Core.getMySQL().getString("giveaway", "prize", "id", ID);
                    String n = String.valueOf(getJoinedUsers(message));

                    sendInfo(channel, ID, prize, n);
                    break;
                }

                case "delete": {

                    List<Message> msgs = gaChannel.getHistory().retrievePast(1).complete();

                    if (msgs.isEmpty()) {
                        Methods.sendErrorMessage(channel, "There isn't any giveaway started.");
                        return;
                    }

                    String ID = String.valueOf(Core.getMySQL().getGiveawayID());

                    if (ID == null) {
                        Methods.sendErrorMessage(channel, "There isn't any giveaway started.");
                        return;
                    }

                    Message message = gaChannel.getMessageById(ID).complete();

                    aChannel.sendMessage(e.getGuild().getPublicRole().getAsMention());
                    Methods.sendSENT(aChannel, "Giveaway", "```The giveaway has been deleted.```");
                    message.delete().queue();
                    Core.getMySQL().dropEntry("giveaway", "id", ID);
                    Methods.sendSENT(channel, "Giveaway", "Giveaway deleted successfully.");
                    break;
                }

                default: {
                    Methods.sendErrorMessage(channel, "Use • `giveaway start | end | delete | info`");
                    break;
                }

            }

        }

    }

    private void sendGiveaway(String prize, TextChannel channel, Emote emote) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Giveaway", null, "https://i.imgur.com/nNpzsAY.png");
        builder.addField("Prize:", prize, false);
        builder.addField("How to join:", "React with " + emote.getAsMention() + " to join.", false);
        builder.setThumbnail("https://i.imgur.com/gl3SIVs.png");
        builder.setColor(Color.decode("#5e9cab"));
        builder.setFooter("Helper • Join the giveaway!", "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue(msg -> {
            msg.addReaction(emote).queue();
            Core.getMySQL().setLong("giveaway", "id", msg.getIdLong(), "prize", prize);
        });
    }

    private void sendEndGiveaway(TextChannel channel, String prize, User winner) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Giveaway", null, "https://i.imgur.com/nNpzsAY.png");
        builder.addField("Prize:", prize, false);
        builder.addField("Winner:", winner.getAsMention(), false);
        builder.addField("How to claim the prize:", "Just open a ticket and be patient.", false);
        builder.setThumbnail("https://i.imgur.com/gl3SIVs.png");
        builder.setColor(Color.decode("#5e9cab"));
        builder.setFooter("Helper • See you to the next giveaway!", "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue();
    }

    private User getWinner(Message msg) {

        List<MessageReaction> reactions = msg.getReactions();
        List<User> users = new ArrayList<>();

        for (MessageReaction re : reactions) {

            for (User usr : re.getUsers()) {

                if (usr.isBot()) {
                    continue;
                }

                users.add(usr);
            }

        }

        return users.get(Methods.getRandom(users.size(), 0));
    }

    private int getJoinedUsers(Message msg) {

        List<MessageReaction> reactions = msg.getReactions();
        int cont = 0;

        for (MessageReaction re : reactions) {

            for (User usr : re.getUsers()) {

                if (usr.isBot()) {
                    continue;
                }

                cont++;
            }

        }

        return cont;
    }

    private void sendInfo(TextChannel channel, String ID, String prize, String joined) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Giveaway", null, "https://i.imgur.com/nNpzsAY.png");
        builder.addField("ID:", ID, false);
        builder.addField("Prize:", prize, false);
        builder.addField("Joined:", joined, false);
        builder.setColor(Color.decode("#5e9cab"));
        builder.setFooter(References.h + "Helper • Info", "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue();
    }

}
