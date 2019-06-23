package me.mrcookies.helper.minigames.countgame;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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
        Long lastAuthorId = getLastAuthor(channel);
        int lastNumber = getLastNumber(channel);

        if (usr.getIdLong() == lastAuthorId) {
            msg.delete().queue();
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Count Game", "You just send a message. Let someone else take the turn first."));
            return;
        }

        if (lastNumber == 0) {
            msg.delete().queue();
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Count Game", "Minigame not started."));
            return;
        }

        try {
            num = Integer.parseInt(msg.getContentRaw());
        } catch (NumberFormatException ex) {
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Count Game", "Invalid number."));
            msg.delete().queue();
            return;
        }

        if (num != lastNumber + 1) {
            msg.delete().queue();
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Count Game", "This is not the next number.\nThe next number is `" + (lastNumber + 1) + "`."));
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

    }

    private int getLastNumber(TextChannel channel) {

        List<Message> msgs = channel.getHistory().retrievePast(2).complete();

        if (msgs.size() < 1) return 0;

        int n;

        try {
            n = Integer.parseInt(msgs.get(1).getContentRaw());
        } catch (NumberFormatException ex) {

            if (msgs.get(1).getAuthor().isBot()) {
                return Core.getConfig().getYml().getInt("Games.Countgame.reference");
            } else {
                return 0;
            }

        }

        return n;
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
