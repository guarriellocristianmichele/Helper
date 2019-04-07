package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CalculatorCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "calc")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();

            if (!Methods.hasPermission(e, channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length != 4) {
                Methods.sendErrorMessage(channel, "Use • `calc [number] [+ | - | * | /] [number]`");
                return;
            }

            if (!Methods.isNumeric(msg[1])) {
                Methods.sendErrorMessage(channel, "Invalid number `" + msg[1] + "`");
                return;
            }

            if (!Methods.isNumeric(msg[3])) {
                Methods.sendErrorMessage(channel, "Invalid number `" + msg[3] + "`");
                return;
            }

            double n1 = Double.parseDouble(msg[1]);
            double n2 = Double.parseDouble(msg[3]);

            User usr = e.getAuthor();

            switch (msg[2]) {

                case "+": {
                    double result = n1 + n2;
                    Methods.sendSENT(channel, "Calculator", usr.getAsMention() + " `" + n1 + " " + msg[2] + " " + n2 + " = " + result + "`");
                    break;
                }

                case "-": {
                    double result = n1 - n2;
                    Methods.sendSENT(channel, "Calculator", usr.getAsMention() + " `" + n1 + " " + msg[2] + " " + n2 + " = " + result + "`");
                    break;
                }

                case "*": {
                    double result = n1 * n2;
                    Methods.sendSENT(channel, "Calculator", usr.getAsMention() + " `" + n1 + " " + msg[2] + " " + n2 + " = " + result + "`");
                    break;
                }

                case "/": {
                    double result = n1 / n2;
                    Methods.sendSENT(channel, "Calculator", usr.getAsMention() + " `" + n1 + " " + msg[2] + " " + n2 + " = " + result + "`");
                    break;
                }

                default: {
                    Methods.sendErrorMessage(channel, "Use • `calc [number] [+ | - | * | /] [number]`");
                    break;
                }

            }

        }

    }

}
