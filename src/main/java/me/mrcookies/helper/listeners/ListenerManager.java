package me.mrcookies.helper.listeners;

import me.mrcookies.helper.listeners.commands.*;
import me.mrcookies.helper.listeners.events.*;
import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.minigames.StartGameCommand;
import me.mrcookies.helper.minigames.StopGameCommand;
import me.mrcookies.helper.minigames.countgame.CountGameEvent;
import me.mrcookies.helper.minigames.higherlower.HigherLowerEvent;
import me.mrcookies.helper.minigames.quickmath.QuickMathEvent;
import me.mrcookies.helper.redeem.RedeemEvent;
import me.mrcookies.helper.requests.AddReactionEvent;
import me.mrcookies.helper.requests.RequestSendEvent;
import me.mrcookies.helper.security.MessageEditBadWordEvent;
import me.mrcookies.helper.security.NoBadWords;
import me.mrcookies.helper.tickets.SolvedCommand;
import me.mrcookies.helper.tickets.TicketCreateChannelEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ListenerManager {

    public void initialize() {

        addEventListeners(new RandomCommand(), new ClearCommand(), new MemberJoinEvent(),
                new DontTagEvent(), new AboutCommand(), new AddReactionEvent(), new RequestSendEvent(),
                new SayCommand(), new AnnounceCommand(), new HelpCommand(), new CancelCommandsEvent(),
                new LinkCommand(), new CalculatorCommand(), new RoleCommand(), new CountGameEvent(),
                new FileCommand(), new StartGameCommand(), new StopGameCommand(), new WordAssistantEvent(),
                new MemberLeftEvent(), new LicenseCreateCommand(), new RedeemEvent(),
                new BotStopEvent(), new CoinsCommand(), new EconomyCommand(), new LeaderBoardCommand(),
                new ProfileCommand(), new PayCommand(), new PingCommand(), new DailyCommand(),
                new RobCommand(), new AccountResetCommand(), new OracleCommand(), new SolvedCommand(),
                new SupportChannelJoinEvent(), new SupportChannelMoveEvent(), new ShutdownCommand(),
                new NoBadWords(), new CommandsChannelEvents(), new MuteCommand(), new UnmuteCommand(),
                new WarnCommand(), new GetIDCommand(), new TicketCreateChannelEvent(),
                new QuickMathEvent(), new HigherLowerEvent(), new MessageEditBadWordEvent());
        System.out.println("Helper > Events ready.");
    }

    private void addEventListeners(ListenerAdapter... listeners) {
        for (ListenerAdapter listener : listeners) {
            Core.getJDA().addEventListener(listener);
        }
    }

}
