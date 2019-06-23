package me.mrcookies.helper.minigames;

import me.mrcookies.helper.minigames.higherlower.HigherLowerEvent;
import me.mrcookies.helper.minigames.quickmath.QuickMathEvent;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class StartGameCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "start")) {

            TextChannel channel = e.getChannel();

            if (!Methods.hasPermission(e.getMember(), channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length != 2) {
                Methods.sendErrorMessage(channel, "Use • `start countgame | quickmath | higherlower`");
                return;
            }

            switch (msg[1].toLowerCase()) {

                case "countgame": {

                    TextChannel countGameChannel = e.getGuild().getTextChannelById(References.idCountGame);

                    if (countGameChannel.getTopic().toLowerCase().startsWith("minigame")) {
                        Methods.sendErrorMessage(channel, "Minigame already started.");
                        return;
                    }

                    countGameChannel.getManager().setTopic("Minigame **Count Game**, write the next number and win coins.").queue();
                    countGameChannel.getManager().putPermissionOverride(e.getGuild().getPublicRole(), 2048, 0).queue();

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

                    quickMathChannel.getHistory().retrievePast(1).queue(msgs -> {

                        if (msgs.size() <= 0) {
                            Methods.sendSimpleEmbed(channel, "Quick Math", "Minigame toggled on.");
                            quickMathChannel.getManager().setTopic("Minigame **Quick Math**, write the correct answer.").queue();
                            quickMathChannel.getManager().putPermissionOverride(e.getGuild().getPublicRole(), 2048, 0).queue();
                            QuickMathEvent.quickMathCore(quickMathChannel);
                        } else {

                            if (quickMathChannel.getTopic().toLowerCase().startsWith("minigame")) {
                                Methods.sendErrorMessage(channel, "Minigame already started.");
                                return;
                            }

                            quickMathChannel.getManager().setTopic("Minigame **Quick Math**, write the correct answer.").queue();
                            quickMathChannel.getManager().putPermissionOverride(e.getGuild().getPublicRole(), 2048, 0).queue();

                            quickMathChannel.getHistory().retrievePast(1).queue(ms -> {

                                if (ms.get(0).getAuthor().isBot()) {
                                    ms.get(0).delete().queue();
                                }

                            });

                            Methods.sendSimpleEmbed(channel, "Quick Math", "Minigame toggled on.");
                        }

                    });

                    break;
                }

                case "higherlower": {

                    TextChannel higherLowerChannel = e.getGuild().getTextChannelById(References.idHigherLower);

                    higherLowerChannel.getHistory().retrievePast(1).queue(msgs -> {

                        if (msgs.size() <= 0) {
                            Methods.sendSimpleEmbed(channel, "Higher Lower", "Minigame toggled on.");
                            higherLowerChannel.getManager().setTopic("Minigame **Higher Lower**, write the number that i'm thinking.").queue();
                            higherLowerChannel.getManager().putPermissionOverride(e.getGuild().getPublicRole(), 2048, 0).queue();
                            HigherLowerEvent.higherLowerCore(higherLowerChannel);
                        } else {

                            if (higherLowerChannel.getTopic().toLowerCase().startsWith("minigame")) {
                                Methods.sendErrorMessage(channel, "Minigame already started.");
                                return;
                            }

                            higherLowerChannel.getManager().setTopic("Minigame **Higher Lower**, write the number that i'm thinking.").queue();
                            higherLowerChannel.getManager().putPermissionOverride(e.getGuild().getPublicRole(), 2048, 0).queue();

                            higherLowerChannel.getHistory().retrievePast(1).queue(ms -> {

                                if (ms.get(0).getAuthor().isBot()) {
                                    ms.get(0).delete().queue();
                                }

                            });

                            Methods.sendSimpleEmbed(channel, "Higher Lower", "Minigame toggled on.");
                        }

                    });

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
