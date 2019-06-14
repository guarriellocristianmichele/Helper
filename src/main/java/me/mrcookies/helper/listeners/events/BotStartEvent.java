package me.mrcookies.helper.listeners.events;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.GuildReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BotStartEvent extends ListenerAdapter {

    @Override
    public void onGuildReady(GuildReadyEvent e) {

        System.out.println("Helper > Checking users...");
        Role New = e.getGuild().getRoleById(References.newRole);
        int cont = 0;
        int role = 0;

        for (User usr : e.getJDA().getUsers()) {

            if (usr.isBot()) {
                continue;
            }

            if (e.getGuild().getMember(usr).getRoles().isEmpty()) {
                e.getGuild().getController().addRolesToMember(e.getGuild().getMember(usr), New).queue();
                sendWelcome(e.getGuild(), usr);
                role++;
            }

            Core.getMySQL().addMember(usr.getName(), usr.getIdLong());
            cont++;
        }

        System.out.println("Helper > Checked " + cont + " users.");
        System.out.println("Helper > Added " + role + " roles.");
    }

    private void sendWelcome(Guild guild, User usr) {
        TextChannel rules = guild.getTextChannelById(References.idRules);
        TextChannel commands = guild.getTextChannelById(References.idCommands);

        usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Welcome " + usr.getName(),
                "Please read and accept the rules by adding the reaction " + getCheck(guild).getAsMention() + " in the channel " + rules.getAsMention() +
                        ", have a good permanence.\nFor bot's commands type `" + References.prefix + "help` in the channel " + commands.getAsMention()));
    }

    private Emote getCheck(Guild guild) {
        return guild.getEmoteById(References.check);
    }

}
