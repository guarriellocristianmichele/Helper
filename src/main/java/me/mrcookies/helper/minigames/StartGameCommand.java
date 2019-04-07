package me.mrcookies.helper.minigames;

import me.mrcookies.helper.minigames.higherlower.HigherLowerEvent;
import me.mrcookies.helper.minigames.quickmath.QuickMathEvent;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.ChannelManager;

import java.util.List;

public class StartGameCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "start")) {

            TextChannel channel = e.getChannel();

            if (!Methods.hasPermission(e, channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length != 2) {
                Methods.sendErrorMessage(channel, "Use • `start countgame | quickmath | higherlower`");
                return;
            }

            switch (msg[1].toLowerCase()) {

                case "countgame": {

                    TextChannel countGameChannel = e.getGuild().getTextChannelById(References.idCountGame);
                    ChannelManager man = new ChannelManager(countGameChannel);

                    if (countGameChannel.getTopic().toLowerCase().startsWith("minigame")) {
                        Methods.sendErrorMessage(channel, "Minigame already started.");
                        return;
                    }

                    man.setTopic("Minigame **Count Game**, write the next number and win coins.").queue();
                    man.putPermissionOverride(e.getGuild().getPublicRole(), 2048, 0).queue();

                    countGameChannel.getHistory().retrievePast(1).queue(msgs -> {

                        if (msgs.get(0).getAuthor().isBot()) {
                            msgs.get(0).delete().queue();
                        }

                    });

                    Methods.sendSimpleEmbed(channel, "Count Game", "Minigame toggled on.");
                    break;
                }

                case "quickmath": {

                    TextChannel quickMathChannel = e.getGuild().getTextChannelById(References.idQuickMath);
                    ChannelManager man = new ChannelManager(quickMathChannel);
                    List<Message> msgs = quickMathChannel.getHistory().retrievePast(1).complete();

                    if (msgs.size() <= 0) {
                        Methods.sendSimpleEmbed(channel, "Quick Math", "Minigame toggled on.");
                        man.setTopic("Minigame **Quick Math**, write the correct answer.").queue();
                        man.putPermissionOverride(e.getGuild().getPublicRole(), 2048, 0).queue();
                        QuickMathEvent.quickMathCore(quickMathChannel);
                        return;
                    }

                    if (quickMathChannel.getTopic().toLowerCase().startsWith("minigame")) {
                        Methods.sendErrorMessage(channel, "Minigame already started.");
                        return;
                    }

                    man.setTopic("Minigame **Quick Math**, write the correct answer.").queue();
                    man.putPermissionOverride(e.getGuild().getPublicRole(), 2048, 0).queue();

                    quickMathChannel.getHistory().retrievePast(1).queue(msgss -> {

                        if (msgss.get(0).getAuthor().isBot()) {
                            msgss.get(0).delete().queue();
                        }

                    });

                    Methods.sendSimpleEmbed(channel, "Quick Math", "Minigame toggled on.");
                    break;
                }

                case "higherlower": {

                    TextChannel higherLowerChannel = e.getGuild().getTextChannelById(References.idHigherLower);
                    ChannelManager man = new ChannelManager(higherLowerChannel);
                    List<Message> msgs = higherLowerChannel.getHistory().retrievePast(1).complete();

                    if (msgs.size() <= 0) {
                        Methods.sendSimpleEmbed(channel, "Higher Lower", "Minigame toggled on.");
                        man.setTopic("Minigame **Higher Lower**, write the number that i'm thinking.").queue();
                        man.putPermissionOverride(e.getGuild().getPublicRole(), 2048, 0).queue();
                        HigherLowerEvent.higherLowerCore(higherLowerChannel);
                        return;
                    }

                    if (higherLowerChannel.getTopic().toLowerCase().startsWith("minigame")) {
                        Methods.sendErrorMessage(channel, "Minigame already started.");
                        return;
                    }

                    man.setTopic("Minigame **Higher Lower**, write the number that i'm thinking.").queue();
                    man.putPermissionOverride(e.getGuild().getPublicRole(), 2048, 0).queue();

                    higherLowerChannel.getHistory().retrievePast(1).queue(msgss -> {

                        if (msgss.get(0).getAuthor().isBot()) {
                            msgss.get(0).delete().queue();
                        }

                    });

                    Methods.sendSimpleEmbed(channel, "Higher Lower", "Minigame toggled on.");
                    break;
                }

                default: {
                    Methods.sendErrorMessage(channel, "Use • `start countgame | quickmath | higherlower`");
                    break;
                }

            }

        }

    }

}
