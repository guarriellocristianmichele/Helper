package me.mrcookies.helper.listeners;

import me.mrcookies.helper.giveaway.GiveawayCommand;
import me.mrcookies.helper.giveaway.GiveawayEvent;
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
import me.mrcookies.helper.rules.RulesAddReactionEvent;
import me.mrcookies.helper.rules.RulesRemoveReactionEvent;
import me.mrcookies.helper.security.MessageEditBadWordEvent;
import me.mrcookies.helper.security.NoBadWords;
import me.mrcookies.helper.tickets.SolvedCommand;
import me.mrcookies.helper.tickets.TicketCreateChannelEvent;

public class ListenerManager {

    public ListenerManager() {
        addEventListeners();
        System.out.println("Helper > Events ready.");
    }

    private void addEventListeners() {
        Core.getJDA().addEventListener(new RandomCommand(), new ClearCommand(), new MemberJoinEvent(),
                new DontTagEvent(), new AboutCommand(), new AddReactionEvent(), new RequestSendEvent(),
                new SayCommand(), new AnnounceCommand(), new HelpCommand(), new CancelCommandsEvent(),
                new RoleCommand(), new CountGameEvent(), new UserInfoCommand(),
                new ImageCommand(), new StartGameCommand(), new StopGameCommand(), new GiveawayCommand(),
                new MemberLeftEvent(), new LicenseCommand(), new RedeemEvent(), new PresenceCommand(),
                new BotStopEvent(), new CoinsCommand(), new EconomyCommand(), new LeaderBoardCommand(),
                new ProfileCommand(), new PayCommand(), new PingCommand(), new DailyCommand(),
                new RobCommand(), new AccountResetCommand(), new OracleCommand(), new SolvedCommand(),
                new SupportChannelJoinEvent(), new SupportChannelMoveEvent(), new ShutdownCommand(),
                new NoBadWords(), new CommandsChannelEvents(), new MuteCommand(), new UnmuteCommand(),
                new WarnCommand(), new IDCommand(), new TicketCreateChannelEvent(), new GiveawayEvent(),
                new QuickMathEvent(), new HigherLowerEvent(), new MessageEditBadWordEvent(),
                new ChatLogCommand(), new RulesAddReactionEvent(), new RulesRemoveReactionEvent());
    }

}
