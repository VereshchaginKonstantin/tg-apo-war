package ru.taksebe.telegram.writeRead.telegram.handlers;

import java.io.IOException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.taksebe.telegram.writeRead.constants.bot.BotMessageEnum;
import ru.taksebe.telegram.writeRead.telegram.TelegramApiClient;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CallbackQueryHandler {
    TelegramApiClient telegramApiClient;

    public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) throws IOException {
        final String chatId = buttonQuery.getMessage().getChatId().toString();
        String data = buttonQuery.getData();
        return new SendMessage(chatId,
                BotMessageEnum.EXCEPTION_BAD_BUTTON_NAME_MESSAGE.getMessage());
    }
}