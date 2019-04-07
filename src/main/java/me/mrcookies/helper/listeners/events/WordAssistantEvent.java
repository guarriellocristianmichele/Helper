package me.mrcookies.helper.listeners.events;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class WordAssistantEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (e.getChannel().getIdLong() == References.idCountGame) return;

        String msg = e.getMessage().getContentRaw();
        Member mem = e.getMember();
        Message msge = e.getMessage();
        MessageChannel channel = e.getChannel();

        if (msg.toLowerCase().equalsIgnoreCase("help")) {

            if (mem.getRoles().stream().anyMatch(role -> role.getIdLong() == Core.getConfig().getYml().getLong("Roles.utility"))) {
                return;
            }

            msge.addReaction(e.getGuild().getEmoteById(References.like)).queue();
            channel.sendMessage("⚠️ **HELP** ⚠️\n\n*Soon someone will help you* (️" +
                    e.getJDA().getRoleById(Core.getConfig().getYml().getLong("Roles.helper")).getAsMention() + ")\n\n⚠️ **HELP** ⚠️").queue();
        }

    }

}
