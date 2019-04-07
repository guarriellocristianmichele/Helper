package me.mrcookies.helper.minigames.countgame;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.List;

public class CountGameEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (e.getChannel().getIdLong() != References.idCountGame) return;

        TextChannel channel = e.getChannel();
        Message msg = e.getMessage();
        User usr = e.getAuthor();
        int num;
        Long lastAuthorId = this.getLastAuthor(channel);
        int lastNumber = this.getLastNumber(channel);

        if (usr.getIdLong() == lastAuthorId) {
            msg.delete().queue();
            usr.openPrivateChannel().queue((ch) -> {
                Methods.sendSENT(ch, "Count Game", "You just send a message. Let someone else take the turn first.");
                ch.close().queue();
            });
            return;
        }

        if (lastNumber == 0) {
            msg.delete().queue();
            usr.openPrivateChannel().queue((ch) -> {
                Methods.sendSENT(ch, "Count Game", "Minigame not started.");
                ch.close().queue();
            });
            return;
        }

        if (!Methods.isNumeric(msg.getContentRaw())) {
            msg.delete().queue();
            usr.openPrivateChannel().queue((ch) -> {
                Methods.sendSENT(ch, "Count Game", "You can't send text messages here, just numbers.");
                ch.close().queue();
            });
            return;
        }

        try {

            num = Integer.parseInt(msg.getContentRaw());

            if (num != lastNumber + 1) {
                msg.delete().queue();
                usr.openPrivateChannel().queue((ch) -> {
                    Methods.sendSENT(ch, "Count Game", "This is not the next number.\nThe next number is `" + (lastNumber + 1) + "`.");
                    ch.close().queue();
                });
                return;
            }

            if (Core.getConfig().getYml().getInt("Games.Countgame.surprise") == num) {
                int currentCoins = Core.getMySQL().getCoins(usr.getIdLong());
                int coins = Methods.getRandom(100, 1);
                int sum = currentCoins + coins;
                Methods.sendSENT(channel, "Count Game", usr.getAsMention() + " won `" + coins + "` coins.");
                Core.getMySQL().setInt("members", "coins", sum, "id_long", String.valueOf(usr.getIdLong()));
                Core.getConfig().getYml().set("Games.Countgame.surprise", lastNumber + Methods.getRandom(20, 1));
            }

            Core.getConfig().getYml().set("Games.Countgame.reference", num);
            Core.getConfig().getYml().set("Games.Countgame.last-author", usr.getIdLong());
            Core.getConfig().save();

        } catch (NumberFormatException ex) {
            usr.openPrivateChannel().queue((ch) -> {
                Methods.sendSENT(ch, "Count Game", "Invalid number.");
                ch.close().queue();
            });
            msg.delete().queue();
        }

    }

    private int getLastNumber(TextChannel channel) {

        List<Message> msgs = channel.getHistory().retrievePast(2).complete();

        if (msgs.size() < 1) return 0;

        if (!Methods.isNumeric(msgs.get(1).getContentRaw())) {
            if (msgs.get(1).getAuthor().isBot()) {
                return Core.getConfig().getYml().getInt("Games.Countgame.reference");
            } else {
                return 0;
            }
        }

        return Integer.parseInt(msgs.get(1).getContentRaw());
    }

    private Long getLastAuthor(TextChannel channel) {

        List<Message> msgs = channel.getHistory().retrievePast(2).complete();

        if (msgs.size() < 1) return 0L;

        if (msgs.get(1).getAuthor().isBot()) {
            return Core.getConfig().getYml().getLong("Games.Countgame.last-author");
        }

        return msgs.get(1).getAuthor().getIdLong();
    }

}
