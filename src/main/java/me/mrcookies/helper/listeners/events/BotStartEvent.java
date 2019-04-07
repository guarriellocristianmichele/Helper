package me.mrcookies.helper.listeners.events;

import me.mrcookies.helper.main.Core;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.GuildReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BotStartEvent extends ListenerAdapter {

    @Override
    public void onGuildReady(GuildReadyEvent e) {

        System.out.println("Helper > Checking users...");
        int cont = 0;
        int roles = 0;

        for (User usr : e.getJDA().getUsers()) {

            if (usr.isBot()) {
                continue;
            }

            Core.getMySQL().addMember(usr.getName(), usr.getIdLong());

            if (e.getGuild().getMember(usr).getRoles().isEmpty()) {
                e.getGuild().getController().addRolesToMember(e.getGuild().getMember(usr), e.getGuild().getRoleById(Core.getConfig().getYml().getLong("Roles.player"))).queue();
                e.getGuild().getController().addRolesToMember(e.getGuild().getMember(usr), e.getGuild().getRoleById(Core.getConfig().getYml().getLong("Roles.support"))).queue();
                roles++;
            }

            cont++;
        }

        System.out.println("Helper > Checked " + cont + " users.");
        System.out.println("Helper > Added " + roles + " roles to users.");
    }

}
