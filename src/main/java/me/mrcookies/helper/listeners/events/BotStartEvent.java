package me.mrcookies.helper.listeners.events;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;

public class BotStartEvent extends ListenerAdapter {

    @Override
    public void onGuildReady(GuildReadyEvent e) {

        System.out.println("Helper > Checking users...");

        Role New = e.getGuild().getRoleById(References.newRole);
        ArrayList<Long> ids = Core.getMySQL().getUsersID();
        int cont = 0;
        int added = 0;
        int removed = 0;

        for (User usr : e.getJDA().getUsers()) {

            if (usr.isBot()) {
                continue;
            }

            if (e.getGuild().getMember(usr).getRoles().isEmpty()) {
                e.getGuild().addRoleToMember(e.getGuild().getMember(usr), New).queue();
                sendWelcome(e.getGuild(), usr);
                added++;
            }

            Core.getMySQL().addMember(usr.getName(), usr.getIdLong());
            cont++;
        }

        for (Long id : ids) {

            if (e.getJDA().getUsers().stream().noneMatch(user -> user.getIdLong() == id)) {
                Core.getMySQL().removeMember(id);
                removed++;
            }

        }

        System.out.println("Helper > Checked " + cont + " users.");
        System.out.println("Helper > Added " + added + " roles.");
        System.out.println("Helper > Removed " + removed + " users.");
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
