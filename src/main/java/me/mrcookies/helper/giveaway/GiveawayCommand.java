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

            if (!Methods.hasPermission(e, channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length < 2) {
                Methods.sendErrorMessage(channel, "Use • `giveaway start | end`");
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
                    Methods.sendSENT(aChannel, "Giveaway", e.getGuild().getPublicRole().getAsMention() + "\nA Giveaway has been started (" + gaChannel.getAsMention() + ")");
                    break;
                }

                case "end": {

                    List<Message> msgs = gaChannel.getHistory().retrievePast(1).complete();

                    if (msgs.isEmpty()) {
                        Methods.sendErrorMessage(channel, "There isn't any giveaway started.");
                        return;
                    }

                    Long id = Core.getMySQL().getGiveawayID();
                    String prize = Core.getMySQL().getString("giveaway", "prize", "id", String.valueOf(id));
                    Message message = gaChannel.getMessageById(id).complete();
                    User winner = getWinner(message, e.getGuild());

                    if (winner == null) {
                        Methods.sendErrorMessage(channel, "No winner found.");
                        return;
                    }

                    message.delete().queue();
                    sendEndGiveaway(gaChannel, prize, winner);
                    Methods.sendSENT(aChannel, "Giveaway", e.getGuild().getPublicRole().getAsMention() + "\nThe Giveaway ended.");
                    Core.getMySQL().dropEntry("giveaway", "id", String.valueOf(id));
                    break;
                }

                default: {
                    Methods.sendErrorMessage(channel, "Use • `giveaway start | end`");
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
        builder.setColor(Color.decode("#5e9cab"));
        builder.setFooter("Helper • See you to the next giveaway!", "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue();
    }

    private User getWinner(Message msg, Guild guild) {

        List<MessageReaction> reactions = msg.getReactions();
        List<User> users = new ArrayList<>();

        for (MessageReaction re : reactions) {

            for (User usr : re.getUsers()) {

                if (usr.isBot()) {
                    continue;
                }

                if (Methods.isStaffer(guild.getMember(usr))) {
                    continue;
                }

                users.add(usr);
            }

        }

        return users.get(Methods.getRandom(users.size(), 0));
    }

}
