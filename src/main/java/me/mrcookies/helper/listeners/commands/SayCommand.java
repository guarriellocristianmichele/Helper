package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class SayCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "send")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();
            User usr = e.getAuthor();

            if (!Methods.hasPermission(e.getMember(), channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length < 4) {
                Methods.sendErrorMessage(channel, "Use • `send [#channel] say [testo]`");
                return;
            }

            if (e.getMessage().getMentionedChannels().isEmpty()) {
                Methods.sendErrorMessage(channel, "Use • `send [#channel] say [testo]`");
                return;
            }

            TextChannel target = e.getMessage().getMentionedChannels().get(0);

            target.sendMessage(e.getMessage().getContentRaw().split(" say ")[1]).queue();
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "System", "Message sent successfully."));
        }

    }

}
