package ru.verekonn.telegram.appowar.telegram.handlers;

import java.io.IOException;
import java.util.Date;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.hpsf.GUID;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.verekonn.telegram.appowar.model.BattleState;
import ru.verekonn.telegram.appowar.model.UserAction;
import ru.verekonn.telegram.appowar.model.repository.BattleRepository;
import ru.verekonn.telegram.appowar.model.repository.UserRepository;
import ru.verekonn.telegram.appowar.model.Battle;
import ru.verekonn.telegram.appowar.model.User;
import ru.verekonn.telegram.appowar.model.UserBattleState;
import ru.verekonn.telegram.appowar.utils.HistoryItem;
import ru.verekonn.telegram.appowar.utils.HistoryList;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CallbackQueryHandler {
    BattleRepository battleRepository;
    UserRepository userRepository;

    public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) throws IOException {
        final String chatId = buttonQuery.getMessage().getChatId().toString();
        final String userName = buttonQuery.getFrom().getUserName();
        try {
            if (buttonQuery.getData().startsWith(
                    UserAction.START.toString())) {
                start(buttonQuery, userName);
            }
            if (buttonQuery.getData().startsWith(
                    UserAction.ATTACK.toString())) {
                attack(userName, buttonQuery.getData());
            }


        } catch (Exception e) {
            return new SendMessage(chatId,
                    e.getMessage());
        }
    }

    private SendMessage attack(String chatId,
                        String userName,
                        String data) {
        String battleId = data.replace(UserAction.ATTACK.toString(), "");
        var battle = battleRepository.findById(battleId);
        if (battle.isPresent()) {
            var user = battle.get().getUser(userName);
            //TODO: user
            return new SendMessage(chatId,
                    "OK attack");
        } else {
            return new SendMessage(chatId,
                    "NOT OK !battle.isPresent()");
        }
    }

    private SendMessage start(String chatId,
                              CallbackQuery buttonQuery,
                              String userName) {
        User user = userRepository
                .findById(userName)
                .get();
        String userSecondName = buttonQuery.getData()
                .replace(UserAction.START.toString(), "");
        User userSecond = userRepository
                .findById(userSecondName)
                .get();
        createBattle(user, userSecond);
        return new SendMessage(chatId,
                "OK start");
    }

    private SendMessage createBattle(User user,
                                     User userSecond) {
        battleRepository.save(
                new Battle(
                        (new GUID()).toString(),
                        new HistoryList<>(new HistoryItem<>(BattleState.INIT)),
                        new Date(),
                        "",
                    "",
                        new UserBattleState(
                            user.getUserName(),
                                 UserAction.DEFENSE,
                                new HistoryList<>(new HistoryItem<>(0L)),
                                new HistoryList<>(new HistoryItem<>(0L))),
                        new UserBattleState(
                            userSecond.getUserName(),
                                UserAction.DEFENSE,
                                new HistoryList<>(new HistoryItem<>(0L)),
                                new HistoryList<>(new HistoryItem<>(0L))
                    )));
    }
}