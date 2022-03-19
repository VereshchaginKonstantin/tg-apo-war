package ru.verekonn.telegram.appowar.telegram.keyboards;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.verekonn.telegram.appowar.model.Battle;
import ru.verekonn.telegram.appowar.model.UserAction;
import ru.verekonn.telegram.appowar.telegram.constants.bot.ButtonNameEnum;
import ru.verekonn.telegram.appowar.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Основная клавиатура, расположенная под строкой ввода текста в Telegram
 */
@Component
public class ReplyKeyboardMaker {

    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ButtonNameEnum.START_FIGHT_TASKS_BUTTON.getButtonName()));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }

    public InlineKeyboardMarkup getStartFightMenu(Battle battle) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(getButton(UserAction.ATTACK.getButtonName(),
                UserAction.ATTACK + battle.getId()));
        rowList.add(getButton(UserAction.DEFENSE.getButtonName(),
                UserAction.DEFENSE + battle.getId()));
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getUsersKeyboardForStart(Iterable<User> userList, String userName) {

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        userList.forEach(u -> {
            if (u != null && !u.getUserName().equals(userName)) {
                rowList.add(getButton(
                        ButtonNameEnum.START_FIGHT_USER_TASKS_BUTTON.getButtonName()
                                + u.getUserName(),
                        UserAction.START + u.getUserName()));
            }
        });

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private List<InlineKeyboardButton> getButton(String buttonName,
                                                 String buttonCallBackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonName);
        button.setCallbackData(buttonCallBackData);

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(button);
        return keyboardButtonsRow;
    }
}