package me.mrcookies.helper.security;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageEditBadWordEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent e) {

        if (e.getAuthor().isBot()) return;

        if (e.getChannel().getIdLong() == References.idCountGame) return;

        if (e.getMessage().getMember().getRoles().stream().anyMatch(role -> role.getIdLong() == Core.getConfig().getYml().getLong("Roles.utility")))
            return;

        String input = e.getMessage().getContentRaw();
        User usr = e.getAuthor();
        TextChannel channel = e.getChannel();

        if (BadWordsFilter.containsBadWords(input)) {

            e.getMessage().delete().queue();

            Methods.sendSimpleEmbed(e.getChannel(), "Security", "Please don't use bad words.");
            int warns = Core.getMySQL().getWarns(usr.getIdLong());
            Member mem = e.getGuild().getMember(usr);

            if (warns == 3) {
                e.getGuild().addRoleToMember(mem, e.getGuild().getRoleById(Core.getConfig().getYml().getLong("Roles.muted"))).queue();
                Methods.sendSENT(channel, "Security", usr.getAsMention() + " have been muted.");
                usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Security", "You have been muted, (warns `3/3`)."));
                return;
            }

            usr.openPrivateChannel().queue((ch) -> {

                Core.getMySQL().setInt("members", "warns", warns + 1, "id_long", String.valueOf(usr.getIdLong()));

                if (warns + 1 == 3) {
                    Methods.sendSENT(ch, "Security", "You have been warned (`" + (warns + 1) + "/3`), this is your last chance.");
                } else {
                    Methods.sendSENT(ch, "Security", "You have been warned (`" + (warns + 1) + "/3`)");
                }

            });

        }

    }

}
