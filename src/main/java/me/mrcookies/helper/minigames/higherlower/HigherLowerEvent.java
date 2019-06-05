package me.mrcookies.helper.minigames.higherlower;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class HigherLowerEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (e.getChannel().getIdLong() != References.idHigherLower) return;

        TextChannel channel = e.getChannel();
        Message msg = e.getMessage();
        User usr = e.getAuthor();
        int number;

        channel.getHistory().retrievePast(1).queue((msgs) -> {

            if (msgs.size() > 0) return;

            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Higher Lower", "Minigame not started."));
        });

        if (!Methods.isNumeric(msg.getContentRaw())) {
            msg.delete().queue();
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Higher Lower", "You can't send text messages here, just numbers."));
            return;
        }

        int reference = Core.getConfig().getYml().getInt("Games.HigherLower.reference");

        try {
            number = Integer.parseInt(msg.getContentRaw());
        } catch (NumberFormatException ex) {
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Higher Lower", "Invalid number."));
            msg.delete().queue();
            return;
        }

        if (number > reference) {
            msg.delete().queue();
            sendError(channel, usr, false, number);
            return;
        }

        if (number < reference) {
            msg.delete().queue();
            sendError(channel, usr, true, number);
            return;
        }

        sendWin(usr, reference, channel);
        higherLowerCore(channel);
        msg.delete().queue();
    }

    public static void higherLowerCore(TextChannel channel) {
        sendThink(channel);
        int n = Methods.getRandom(1000, 1);
        Core.getConfig().getYml().set("Games.HigherLower.reference", n);
        Core.getConfig().save();
    }

    private static void sendError(TextChannel channel, User usr, boolean value, int n) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Higher Lower", null, "https://i.imgur.com/FtoMNlh.png");

        if (value) {
            builder.setDescription(usr.getAsMention() + " higher than `" + n + "`.");
        } else {
            builder.setDescription(usr.getAsMention() + " lower than `" + n + "`.");
        }

        builder.setColor(Color.decode("#e74c3c"));
        builder.setFooter("Helper • Try again!", "https://i.imgur.com/nepS3Lp.jpg");
        channel.sendMessage(builder.build()).queue();
    }

    private void sendWin(User usr, int n, TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Higher Lower", null, "https://i.imgur.com/p3owVKR.png");
        builder.setDescription(usr.getAsMention() + " you guessed!\n**The correct answer was** `" + n + "`.");
        builder.setColor(Color.decode("#2ecc71"));
        builder.setFooter("Helper • Thinking the next number!", "https://i.imgur.com/nepS3Lp.jpg");
        channel.sendMessage(builder.build()).queue();
    }

    private static void sendThink(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Higher Lower", null, "https://i.imgur.com/gNUeslw.png");
        builder.setDescription("I thinked a number, try to guess it.");
        builder.setColor(Color.decode("#f8c291"));
        builder.setFooter("Helper • Guess the number!", "https://i.imgur.com/nepS3Lp.jpg");
        channel.sendMessage(builder.build()).queue();
    }

}
