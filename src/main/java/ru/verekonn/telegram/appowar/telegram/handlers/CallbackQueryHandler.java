package ru.verekonn.telegram.appowar.telegram.handlers;

import java.io.IOException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.verekonn.telegram.appowar.engines.BattleEngine;
import ru.verekonn.telegram.appowar.model.User;
import ru.verekonn.telegram.appowar.model.UserAction;
import ru.verekonn.telegram.appowar.model.repository.UserRepository;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CallbackQueryHandler {
    BattleEngine battleEngine;
    UserRepository userRepository;

    public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) throws IOException {
        final String chatId = buttonQuery.getMessage().getChatId().toString();
        final String userName = buttonQuery.getFrom().getUserName();
        try {
            if (buttonQuery.getData().startsWith(
                    UserAction.START.toString())) {
                return start(chatId, buttonQuery, userName);
            }
            if (buttonQuery.getData().startsWith(
                    UserAction.ATTACK.toString())) {
                return attack(chatId, userName, buttonQuery.getData());
            }
            if (buttonQuery.getData().startsWith(
                    UserAction.DEFENSE.toString())) {
                return defence(chatId, userName, buttonQuery.getData());
            }
            return new SendMessage(chatId,
                    "OK ???");
        } catch (Exception e) {
            return new SendMessage(chatId,
                    e.getMessage());
        }
    }

    private SendMessage defence(String chatId,
                               String userName,
                               String data) {
        String battleId = data.replace(UserAction.DEFENSE.toString(), "");
        if (battleEngine.defence(battleId, userName)) {
            return new SendMessage(chatId,
                    "OK DEFENSE");
        } else {
            return new SendMessage(chatId,
                    "NOT OK");
        }
    }

    private SendMessage attack(String chatId,
                        String userName,
                        String data) {
        String battleId = data.replace(UserAction.ATTACK.toString(), "");
        if (battleEngine.attack(battleId, userName)) {
            return new SendMessage(chatId,
                    "OK ATTACK");
        } else {
            return new SendMessage(chatId,
                    "NOT OK");
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
        battleEngine.createBattle(user, userSecond);
        return new SendMessage(chatId,
                "OK start");
    }

}