package ru.taksebe.telegram.writeRead.telegram.handlers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.taksebe.telegram.writeRead.api.dictionaries.UserRepository;
import ru.taksebe.telegram.writeRead.constants.bot.BotMessageEnum;
import ru.taksebe.telegram.writeRead.constants.bot.ButtonNameEnum;
import ru.taksebe.telegram.writeRead.model.User;
import ru.taksebe.telegram.writeRead.telegram.keyboards.ReplyKeyboardMaker;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MessageHandler {

    ReplyKeyboardMaker replyKeyboardMaker;
    UserRepository userRepository;

    public BotApiMethod<?> answerMessage(Message message) {
        String chatId = message.getChatId().toString();
        String userName = message.getFrom().getUserName();
        String inputText = message.getText();

        if (inputText == null) {
            throw new IllegalArgumentException();
        } else if (inputText.equals("/start")) {
            User user = getUser(userName);
            return getStartMessage(chatId, user);
        } else if (inputText.equals(ButtonNameEnum.START_FIGHT_TASKS_BUTTON.getButtonName())) {
            return getStartFightMessage(chatId, userName);
        } else {
            return new SendMessage(chatId, BotMessageEnum.NON_COMMAND_MESSAGE.getMessage());
        }
    }

    private User getUser(String userName) {
       var user = userRepository.findById(userName);
       if (!user.isPresent()) {
           userRepository.save(new User(userName, 100L,0L,0L));
       }
       return userRepository.findById(userName).get();
    }

    private SendMessage getStartMessage(String chatId, User user) {
        SendMessage sendMessage = new SendMessage(chatId,
                BotMessageEnum.HELP_MESSAGE.getMessage()
                        + user.toString());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());
        return sendMessage;
    }

    private SendMessage getStartFightMessage(String chatId, String userName) {
        SendMessage sendMessage = new SendMessage(chatId,
                ButtonNameEnum.START_FIGHT_USER_TASKS_BUTTON.getButtonName());

        var list = replyKeyboardMaker
                .getUsersKeyboard(
                        userRepository.findAll(),
                        userName
                );
        sendMessage.setReplyMarkup(list);
        return sendMessage;
    }
}