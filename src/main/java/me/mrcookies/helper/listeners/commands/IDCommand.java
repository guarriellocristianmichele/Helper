package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class IDCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "id")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();

            if (!Methods.hasPermission(e.getMember(), channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length != 3) {
                Methods.sendErrorMessage(channel, "Use • `id emote/role [name]`");
                return;
            }

            switch (msg[1].toLowerCase()) {

                case "emote": {

                    if (e.getGuild().getEmotes().stream().noneMatch(emote -> emote.getName().equals(msg[2]))) {
                        Methods.sendErrorMessage(channel, "Invalid emote `" + msg[2] + "`");
                        return;
                    }

                    Methods.sendSENT(channel, "System", "ID of emote `" + msg[2] + "` is `" + e.getGuild().getEmotesByName(msg[2], true).get(0).getIdLong() + "`");
                    break;
                }

                case "role": {

                    if (e.getGuild().getRoles().stream().noneMatch(role -> role.getName().equals(msg[2]))) {
                        Methods.sendErrorMessage(channel, "Invalid role `" + msg[2] + "`");
                        return;
                    }

                    Methods.sendSENT(channel, "System", "ID of role `" + msg[2] + "` is `" + e.getGuild().getRolesByName(msg[2], true).get(0).getIdLong() + "`");
                    break;
                }

                default: {
                    Methods.sendErrorMessage(channel, "Use • `id emote/role [name]`");
                }

            }

        }

    }

}
