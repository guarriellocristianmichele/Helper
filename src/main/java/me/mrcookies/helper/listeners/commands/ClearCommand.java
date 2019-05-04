package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ClearCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "clear")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();

            if (!Methods.hasPermission(e.getMember(), channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length != 3) {
                Methods.sendErrorMessage(channel, "Use • `clear [#channel] [amount]`");
                return;
            }

            if (e.getMessage().getMentionedChannels().isEmpty()) {
                Methods.sendErrorMessage(channel, "Use • `clear [#channel] [amount]`");
                return;
            }

            if (!Methods.isNumeric(msg[2])) {
                Methods.sendErrorMessage(channel, "Invalid number `" + msg[2] + "`");
                return;
            }

            TextChannel target = e.getMessage().getMentionedChannels().get(0);
            purgeMessages(target, Integer.parseInt(msg[2]), channel, e.getAuthor());
        }

    }

    private void purgeMessages(TextChannel channel, int num, TextChannel chan, User usr) {

        if (num > 100 || num < 1) {
            Methods.sendErrorMessage(chan, "Invalid number `" + num + "`");
            return;
        }

        channel.getHistory().retrievePast(num).queue(msgs -> {

            if (msgs.size() < 2) {
                usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Clear", "Minimum `2` messages in the channel."));
                return;
            }

            for (Message msg : msgs) {
                msg.delete().queue();
            }

        });

        Methods.sendSimpleEmbed(chan, "Messages", "Deleted `" + num + "` messages.");
    }

}
