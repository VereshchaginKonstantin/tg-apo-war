package ru.verekonn.telegram.appowar.telegram.handlers;

import java.io.IOException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.hpsf.GUID;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.verekonn.telegram.appowar.model.BattleState;
import ru.verekonn.telegram.appowar.model.repository.BattleRepository;
import ru.verekonn.telegram.appowar.model.repository.UserRepository;
import ru.verekonn.telegram.appowar.model.Battle;
import ru.verekonn.telegram.appowar.model.User;
import ru.verekonn.telegram.appowar.model.UserBattleState;

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
            User user = userRepository
                    .findById(userName)
                    .get();
            String userSecondName = buttonQuery.getData();
            User userSecond = userRepository
                    .findById(userSecondName)
                    .get();
            battleRepository.save(
                    new Battle(
                            (new GUID()).toString(),
                    0L,
                         BattleState.START,
                        "",
                        "",
                        new UserBattleState(
                                user.getUserName(),
                                0L,
                                0L),
                        new UserBattleState(
                                userSecond.getUserName(),
                                0L,
                                0L)));
            return new SendMessage(chatId,
                    "OK");
        } catch (Exception e) {
            return new SendMessage(chatId,
                    e.getMessage());
        }
    }
}