package ru.verekonn.telegram.appowar.engines;

import java.util.List;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.verekonn.telegram.appowar.model.Battle;
import ru.verekonn.telegram.appowar.model.BattleState;
import ru.verekonn.telegram.appowar.model.User;
import ru.verekonn.telegram.appowar.model.UserAction;
import ru.verekonn.telegram.appowar.model.repository.BattleRepository;
import ru.verekonn.telegram.appowar.model.repository.UserRepository;
import ru.verekonn.telegram.appowar.telegram.WriteReadBot;
import ru.verekonn.telegram.appowar.telegram.keyboards.ReplyKeyboardMaker;
import ru.verekonn.telegram.appowar.utils.HistoryItem;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Component
public class ReportEngine {

    WriteReadBot writeReadBot;
    BattleRepository battleRepository;
    UserRepository userRepository;
    ReplyKeyboardMaker replyKeyboardMaker;

    public void sendByTimer(List<Battle> battle) {
        battle.forEach(this::sendByTimer);
    }

    public void sendByTimer(Battle battle) {
        battle
                .getUserFirst()
                .getAction()
                .getNotReported()
                .forEach(action -> {
            reportAction(
                    battle.getUserFirst().getUserName(),
                    battle.getUserSecond().getUserName(),
                    action);
            action.setReported(true);
        });
        battle
                .getUserSecond()
                .getAction()
                .getNotReported()
                .forEach(action -> {
            reportAction(
                    battle.getUserSecond().getUserName(),
                    battle.getUserFirst().getUserName(),
                    action);
            action.setReported(true);
        });
        battle.getState().getNotReported().forEach(status -> {
            reportStatus(battle, status);
            status.setReported(true);
        });
        battleRepository.save(battle);
    }

    private void reportAction(String userName, String otherUserName, HistoryItem<UserAction> action) {
        String text = action.getValue().toString() + " " + action.getTimestamp().toString();
        var user = userRepository
                .findById(userName)
                .get();
        send(userName
                + " message:"
                + text, user);
        text = userName
                + " message:"
                + "Только для отладки(потом скрыто будет)! - "
                + action.getValue().toString()
                + " "
                + action.getTimestamp().toString();
        user = userRepository
                .findById(otherUserName)
                .get();
        send(text, user);
    }

    private void reportStatus(Battle battle, HistoryItem<BattleState> status) {
        if (status
                .getValue()
                .equals(BattleState.END)) {
            sendWin(battle);
        }
        if (status
                .getValue()
                .equals(BattleState.DRAW_BY_TIME)) {
            sendDraw(battle);
        }
        if (status
                .getValue()
                .equals(BattleState.PROCESS) &&
                battle
                        .getState()
                        .getPrev()
                        .getValue()
                        .equals(BattleState.INIT)
        ) {
            sendStart(battle);
        }
    }

    private void sendStart(Battle battle) {
        var userFirst = userRepository
                .findById(battle
                        .getUserFirst()
                        .getUserName())
                .get();

        String text = "Мы на позиции - что будем делать?";
        text += " " + battle.getUserFirst().getUserName();
        text += " -> " + battle.getUserSecond().getUserName();
        send(text, userFirst);
        sendStartFightMenu(battle, text, userFirst);

        var userSecond = userRepository
                .findById(battle
                        .getUserSecond()
                        .getUserName())
                .get();
        text = "Видим неприятеля - что будем делать?";
        text += " " + battle.getUserFirst().getUserName();
        text += " -> " + battle.getUserSecond().getUserName();
        sendStartFightMenu(battle, text, userSecond);
    }

    private void sendDraw(Battle battle) {
        String text = "Разошлись(время) ";
        var userFirst = userRepository
                .findById(battle
                        .getUserFirst()
                        .getUserName())
                .get();
        var userSecond = userRepository
                .findById(battle
                        .getUserSecond()
                        .getUserName())
                .get();
        text += " " + battle.getUserFirst().getUserName();
        text += " -> " + battle.getUserSecond().getUserName();
        send(text, userFirst, userSecond);
    }

    private void sendProceed(Battle battle) {
        String text = "В процессе нападения ";
        var userFirst = userRepository
                .findById(battle
                        .getUserFirst()
                        .getUserName())
                .get();
        var userSecond = userRepository
                .findById(battle
                        .getUserSecond()
                        .getUserName())
                .get();
        send(text, userFirst, userSecond);
    }

    private void sendStartFightMenu(Battle battle, String text, User user) {
        SendMessage message =
                new SendMessage(user.getChatId(), text);
        var menu =
                replyKeyboardMaker.getStartFightMenu(battle);
        message.setReplyMarkup(menu);
        try {
            writeReadBot
                    .execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendWin(Battle battle) {
        String text = "Победил ";
        var userWinner = userRepository
                .findById(battle
                        .getWinnerUserName())
                .get();
        var userLooser = userRepository
                .findById(battle
                        .getLooserUserName())
                .get();
        text += userWinner.getUserName();
        text += userWinner;
        text += userLooser;
        send(text, userWinner, userLooser);
    }

    private void send(String text, User user) {
        SendMessage message =
                new SendMessage(user.getChatId(),
                            text);
        try {
            writeReadBot
                    .execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(String text, User userWinner, User userLooser) {
        SendMessage message =
                new SendMessage(userWinner.getChatId(),
                        text);
        try {
            writeReadBot
                    .execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        message =
                new SendMessage(userLooser.getChatId(),
                        text);
        try {
            writeReadBot
                    .execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
