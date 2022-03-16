package ru.verekonn.telegram.appowar.quartz;

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
import ru.verekonn.telegram.appowar.engines.BattleEngine;
import ru.verekonn.telegram.appowar.engines.ReportEngine;
import ru.verekonn.telegram.appowar.engines.RewardEngine;
import ru.verekonn.telegram.appowar.model.repository.BattleRepository;
import ru.verekonn.telegram.appowar.model.repository.UserRepository;
import ru.verekonn.telegram.appowar.model.Battle;
import ru.verekonn.telegram.appowar.telegram.WriteReadBot;


@Component
public class BattleJob implements Job {

    static Object lock =  new Object();
    static Random random = new Random(2333);



    @Autowired
    BattleEngine battleEngine;

    @Autowired
    RewardEngine rewardEngine;

    @Autowired
    ReportEngine reportEngine;

    @Autowired
    WriteReadBot writeReadBot;


    @Autowired
    UserRepository userRepository;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        synchronized (lock) {
            List<Battle> procceded = battleEngine.step();
            rewardEngine.proceedReward(procceded);
            battleEngine.clean();
        }
    }
}
