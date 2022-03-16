package ru.taksebe.telegram.writeRead.quartz;

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
import ru.taksebe.telegram.writeRead.api.dictionaries.UserRepository;
import ru.taksebe.telegram.writeRead.telegram.TelegramApiClient;
import ru.taksebe.telegram.writeRead.telegram.WriteReadBot;


@Component
public class BattleJob implements Job {

    @Autowired
    WriteReadBot writeReadBot;

    @Autowired
    UserRepository userRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        int i = 0;
        userRepository.findAll().forEach(u -> {
            SendMessage message =
                    new SendMessage (u.getChatId(), "tst");
            try {
                writeReadBot
                        .execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        });
    }
}
