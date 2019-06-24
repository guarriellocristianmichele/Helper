package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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
                Methods.sendErrorMessage(channel, "Use • `clear [#channel] [amount] | all`");
                return;
            }

            if (e.getMessage().getMentionedChannels().isEmpty()) {
                Methods.sendErrorMessage(channel, "Use • `clear [#channel] [amount] | all`");
                return;
            }

            TextChannel target = e.getMessage().getMentionedChannels().get(0);

            if (msg[2].equalsIgnoreCase("all")) {
                purgeAllMessages(target, channel);
                return;
            }

            int n;

            try {
                n = Integer.parseInt(msg[2]);
            } catch (NumberFormatException ex) {
                Methods.sendErrorMessage(channel, "Invalid number");
                return;
            }

            purgeMessages(target, n, channel);
        }

    }

    private void purgeMessages(TextChannel target, int num, TextChannel chan) {

        if (num > 100 || num < 1) {
            Methods.sendErrorMessage(chan, "Invalid number `" + num + "`");
            return;
        }

        target.getHistory().retrievePast(num).queue(msgs -> {

            int cont = 0;

            if (msgs.size() < 2) {
                Methods.sendErrorMessage(chan, "Minimum `1` message in the channel.");
                return;
            }

            for (Message msg : msgs) {
                msg.delete().queue();
                cont++;
            }

            Methods.sendSimpleEmbed(chan, "Messages", "Deleted `" + cont + "`/`" + num + "` messages.");

        });

    }

    private void purgeAllMessages(TextChannel target, TextChannel chan) {

        target.getIterableHistory().queue(msgs -> {

            int cont = 0;

            if (msgs.size() < 1) {
                Methods.sendErrorMessage(chan, "Minimum `1` message in the channel.");
                return;
            }

            for (Message msg : msgs) {
                msg.delete().queue();
                cont++;
            }

            Methods.sendSimpleEmbed(chan, "Messages", "Deleting `" + cont + "` messages.");

        });

    }

}
