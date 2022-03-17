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
import ru.verekonn.telegram.appowar.model.repository.UserRepository;
import ru.verekonn.telegram.appowar.telegram.WriteReadBot;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Component
public class ReportEngine {

    WriteReadBot writeReadBot;
    UserRepository userRepository;

    public void sendByTimer(List<Battle> battle) {
        battle.forEach(this::sendByTimer);
    }

    public void sendByTimer(Battle battle) {
        if (battle.getState().hasChanges(battle.getTimestamp())) {
            if (battle
                    .getState()
                    .getCurrentValue()
                    .equals(BattleState.END)) {
                sendWin(battle);
            } else {
                sendProceed(battle);
            }
        }
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
