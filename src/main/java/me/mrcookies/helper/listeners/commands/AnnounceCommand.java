package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class AnnounceCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "announce")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();

            if (!Methods.hasPermission(e.getMember(), channel)) return;

            String[] msgs = e.getMessage().getContentRaw().split(" / ");

            if (msgs.length != 4) {
                Methods.sendErrorMessage(channel, "Use • `announce / [title] / [description] / [#channel]`");
                return;
            }

            if (e.getMessage().getMentionedChannels().isEmpty()) {
                Methods.sendErrorMessage(channel, "Use • `announce / [title] / [description] / [#channel]`");
                return;
            }

            TextChannel target = e.getMessage().getMentionedChannels().get(0);

            Methods.sendSENT(target, msgs[1], msgs[2]);
            channel.sendMessage(e.getAuthor().getAsMention() + " message sent.").queue();
        }

    }

}
