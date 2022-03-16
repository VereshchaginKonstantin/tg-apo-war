package ru.taksebe.telegram.writeRead.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.taksebe.telegram.writeRead.model.dictionaries.BattleRepository;
import ru.taksebe.telegram.writeRead.model.dictionaries.UserRepository;
import ru.taksebe.telegram.writeRead.model.Battle;
import ru.taksebe.telegram.writeRead.telegram.WriteReadBot;


@Component
public class BattleJob implements Job {

    static Object lock =  new Object();
    static Random random = new Random(2333);

    @Autowired
    WriteReadBot writeReadBot;

    @Autowired
    BattleRepository battleRepository;

    @Autowired
    UserRepository userRepository;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        synchronized (lock) {
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
                    var i = random.nextInt();
                    boolean win = i % 2 == 0;
                    if (win) {
                        text += userFirst.getUserName();
                        userFirst.setWins(userFirst.getWins() + 1);
                        userFirst.setCash(userFirst.getCash() + 100);
                        userSecond.setCash(userSecond.getCash() - 50);
                        userSecond.setLoose(userSecond.getLoose() + 1);
                    } else {
                        text += userSecond.getUserName();
                        userSecond.setWins(userSecond.getWins() + 1);
                        userSecond.setCash(userSecond.getCash() + 100);
                        userFirst.setCash(userFirst.getCash() - 50);
                        userFirst.setLoose(userFirst.getLoose() + 1);
                    }
                    userRepository.save(userSecond);
                    userRepository.save(userFirst);
                    text += userFirst;
                    text += userSecond;
                    remove.add(b);
                }
                SendMessage message =
                        new SendMessage(userFirst.getChatId(),
                                text);
                try {
                    writeReadBot
                            .execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                message =
                        new SendMessage(userSecond.getChatId(),
                                text);
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
}
