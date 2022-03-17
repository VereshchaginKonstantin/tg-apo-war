package ru.verekonn.telegram.appowar.telegram.handlers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.verekonn.telegram.appowar.model.repository.UserRepository;
import ru.verekonn.telegram.appowar.telegram.constants.bot.BotMessageEnum;
import ru.verekonn.telegram.appowar.telegram.constants.bot.ButtonNameEnum;
import ru.verekonn.telegram.appowar.model.User;
import ru.verekonn.telegram.appowar.telegram.keyboards.ReplyKeyboardMaker;

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
        User user = getUser(userName, chatId);

        if (inputText == null) {
            throw new IllegalArgumentException();
        } else if (inputText.equals("/start")) {
            return getStartMessage(chatId, user);
        } else if (inputText.equals(ButtonNameEnum.START_FIGHT_TASKS_BUTTON.getButtonName())) {
            return getStartFightMessage(chatId, userName);
        } else {
            return new SendMessage(chatId, BotMessageEnum.NON_COMMAND_MESSAGE.getMessage());
        }
    }

    private User getUser(String userName, String chatId) {
       var user = userRepository.findById(userName);
       if (!user.isPresent()) {
           userRepository.save(new User(
                   userName,
                   chatId,
                   100L,
                   100L,
                   100L,
                   100L,
                   0L,
                   0L));
       } else {
           user.get().setChatId(chatId);
           userRepository.save(user.get());
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