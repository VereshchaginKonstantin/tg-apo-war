package ru.verekonn.telegram.appowar.quartz;

import java.util.Date;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.verekonn.telegram.appowar.model.repository.UserRepository;

import static org.quartz.DateBuilder.evenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Component
public class JobBattleService  implements InitializingBean {

    UserRepository userRepository;
    ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        // First we must get a reference to a scheduler
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);

        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        sched.setJobFactory(jobFactory);

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
