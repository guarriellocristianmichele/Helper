package me.mrcookies.helper.listeners.events;

import me.mrcookies.helper.main.Core;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.GuildReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Collection;

public class BotStartEvent extends ListenerAdapter {

    @Override
    public void onGuildReady(GuildReadyEvent e) {

        System.out.println("Helper > Checking users...");
        int cont = 0;
        int roles = 0;

        Collection<Role> role = new ArrayList<>();
        role.add(e.getGuild().getRoleById(Core.getConfig().getYml().getLong("Roles.player")));
        role.add(e.getGuild().getRoleById(Core.getConfig().getYml().getLong("Roles.support")));

        for (User usr : e.getJDA().getUsers()) {

            if (usr.isBot()) {
                continue;
            }

            Core.getMySQL().addMember(usr.getName(), usr.getIdLong());

            if (e.getGuild().getMember(usr).getRoles().isEmpty()) {
                e.getGuild().getController().addRolesToMember(e.getGuild().getMember(usr), role).queue();
                roles++;
            }

            cont++;
        }

        System.out.println("Helper > Checked " + cont + " users.");
        System.out.println("Helper > Added " + roles + " roles to users.");
    }

}
