package ru.taksebe.telegram.writeRead.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.taksebe.telegram.writeRead.api.dictionaries.BattleRepository;
import ru.taksebe.telegram.writeRead.api.dictionaries.UserRepository;
import ru.taksebe.telegram.writeRead.model.Battle;
import ru.taksebe.telegram.writeRead.telegram.TelegramApiClient;
import ru.taksebe.telegram.writeRead.telegram.WriteReadBot;


@Component
public class BattleJob implements Job {

    @Autowired
    WriteReadBot writeReadBot;

    @Autowired
    BattleRepository battleRepository;

    @Autowired
    UserRepository userRepository;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Battle> remove = new ArrayList<>();
        battleRepository.findAll().forEach(b -> {

            b.setTime(b.getTime() + 1);
            battleRepository.save(b);
            var userFirst = userRepository
                    .findById(b
                            .getUserFirst()
                            .getUserName())
                    .get();
            var userSecond = userRepository
                    .findById(b
                            .getUserSecond()
                            .getUserName())
                    .get();
            String text = "В процессе нападения ";
            if (b.getTime() > Battle.END) {
                text = "Конец победил ";
                Random r = new Random(2);
                var i = r.nextInt();
                boolean win = i % 2 == 0;
                if (win) {
                    text += userFirst.getUserName();
                    userFirst.setWins(userFirst.getWins() + 1);
                    userFirst.setCash(userFirst.getCash() + 100);
                } else {
                    text += userSecond.getUserName();
                    userSecond.setCash(userSecond.getCash() - 100);
                    userSecond.setLoose(userSecond.getLoose() + 1);
                }
                userRepository.save(userSecond);
                userRepository.save(userFirst);
                remove.add(b);
            }
            SendMessage message =
                    new SendMessage (userFirst.getChatId(),
                            text + userFirst);
            try {
                writeReadBot
                        .execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            message =
                    new SendMessage (userSecond.getChatId(),
                            text + userFirst);
            try {
                writeReadBot
                        .execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        });
        remove.forEach(x ->
                battleRepository.delete(x));
    }
}
