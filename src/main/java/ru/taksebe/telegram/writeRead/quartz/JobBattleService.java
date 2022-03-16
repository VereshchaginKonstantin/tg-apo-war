package ru.taksebe.telegram.writeRead.quartz;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import static org.quartz.DateBuilder.evenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Component
public class JobBattleService  implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        // First we must get a reference to a scheduler
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        Date runTime = evenSecondDate(new Date());
        JobDetail job = newJob(BattleJob.class)
                .withIdentity("job1", "group1")
                .build();
        SimpleTrigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .startAt(runTime)
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(1)
                        .repeatForever())
                .build();
        sched.scheduleJob(job, trigger);
        sched.start();
    }
}
