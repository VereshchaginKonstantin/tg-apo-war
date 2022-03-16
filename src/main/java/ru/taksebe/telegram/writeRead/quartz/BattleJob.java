package ru.taksebe.telegram.writeRead.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import ru.taksebe.telegram.writeRead.api.dictionaries.UserRepository;

@Component
public class BattleJob implements Job {

    UserRepository userRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        int i = 0;
    }
}
