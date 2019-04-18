package me.mrcookies.helper.listeners.events;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DontTagEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (e.getChannel().getIdLong() == References.idCountGame) return;

        Message msg = e.getMessage();
        TextChannel channel = e.getChannel();

        if (msg.getContentRaw().startsWith(References.prefix + "coins")) return;
        if (msg.getContentRaw().startsWith(References.prefix + "profile")) return;
        if (msg.getContentRaw().startsWith(References.prefix + "pay")) return;
        if (msg.getContentRaw().startsWith(References.prefix + "rob")) return;
        if (msg.getContentRaw().startsWith(References.prefix + "oracle")) return;
        if (msg.getContentRaw().startsWith(References.prefix + "warn")) return;
        if (msg.getContentRaw().startsWith(References.prefix + "mute")) return;
        if (msg.getContentRaw().startsWith(References.prefix + "unmute")) return;

        blockMentionUser(msg, e.getAuthor(), e.getMember(), channel);
    }

    private void blockMentionUser(Message msg, User usr, Member mem, TextChannel channel) {

        if (msg.getMentionedMembers().stream().anyMatch(member -> member.getRoles().contains(Core.getJDA().getRoleById(Core.getConfig().getYml().getLong("Roles.no-tag"))))) {

            if (mem.getRoles().stream().anyMatch(role -> role.getIdLong() == Core.getConfig().getYml().getLong("Roles.utility"))) {
                return;
            }

            msg.delete().queue();
            Methods.sendSimpleEmbed(channel, "System", usr.getAsMention() + " please do **NOT** tag him, join the support channel if you need help.");
            channel.sendMessage("**" + usr.getName() + ":** *" + msg.getContentDisplay().replace("@", "") + "*").queue();
        }

    }

}
