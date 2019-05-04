package me.mrcookies.helper.minigames;

import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.ChannelManager;

public class StopGameCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "stop")) {

            TextChannel channel = e.getChannel();

            if (!Methods.hasPermission(e.getMember(), channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length != 2) {
                Methods.sendErrorMessage(channel, "Use • `stop countgame | quickmath | higherlower`");
                return;
            }

            switch (msg[1].toLowerCase()) {

                case "countgame": {

                    TextChannel countGameChannel = e.getGuild().getTextChannelById(References.idCountGame);
                    ChannelManager man = new ChannelManager(countGameChannel);

                    if (countGameChannel.getTopic().toLowerCase().startsWith("[maintenance]")) {
                        Methods.sendErrorMessage(channel, "Minigame already in maintenance.");
                        return;
                    }

                    man.setTopic("[Maintenance] Minigame **Count Game**, write the next number and win coins.").queue();
                    man.putPermissionOverride(e.getGuild().getPublicRole(), 0, 2048).queue();
                    Methods.sendSENT(countGameChannel, "Count Game", "Minigame in maintenance..");
                    Methods.sendSimpleEmbed(channel, "Count Game", "Minigame set in maintenance.");
                    break;
                }

                case "quickmath": {

                    TextChannel quickMathChannel = e.getGuild().getTextChannelById(References.idQuickMath);
                    ChannelManager man = new ChannelManager(quickMathChannel);

                    if (quickMathChannel.getTopic().toLowerCase().startsWith("[maintenance]")) {
                        Methods.sendErrorMessage(channel, "Minigame already in maintenance.");
                        return;
                    }

                    man.setTopic("[Maintenance] Minigame **Quick Math**, write the correct answer.").queue();
                    man.putPermissionOverride(e.getGuild().getPublicRole(), 0, 2048).queue();
                    Methods.sendSENT(quickMathChannel, "Quick Math", "Minigame in maintenance..");
                    Methods.sendSimpleEmbed(channel, "Quick Math", "Minigame set in maintenance.");
                    break;
                }

                case "higherlower": {

                    TextChannel quickMathChannel = e.getGuild().getTextChannelById(References.idQuickMath);
                    ChannelManager man = new ChannelManager(quickMathChannel);

                    if (quickMathChannel.getTopic().toLowerCase().startsWith("[maintenance]")) {
                        Methods.sendErrorMessage(channel, "Minigame already in maintenance.");
                        return;
                    }

                    man.setTopic("[Maintenance] Minigame **Higher Lower**, write the number that i'm thinking.").queue();
                    man.putPermissionOverride(e.getGuild().getPublicRole(), 0, 2048).queue();
                    Methods.sendSENT(quickMathChannel, "Higher Lower", "Minigame in maintenance..");
                    Methods.sendSimpleEmbed(channel, "Higher Lower", "Minigame set in maintenance.");
                    break;
                }

                default: {
                    Methods.sendErrorMessage(channel, "Use • `stop countgame | quickmath | higherlower`");
                    break;
                }

            }

        }

    }

}
