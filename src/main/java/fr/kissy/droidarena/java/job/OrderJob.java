package fr.kissy.droidarena.java.job;

import fr.kissy.droidarena.java.bot.AbstractBot;
import fr.kissy.droidarena.java.service.RoundService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Calendar;

/**
 * The order job is triggered everytime the robot has to make an order.
 * The schedule time is every 2s (starting from even second).
 * There is a 1 minute break every 10 minute (the last minute, ie the 9nth minute).
 *
 * <b>Note : </b>The job's execution time cannot exceed 2 seconds.
 *
 * @author Guillaume Le Biller <lebiller@ekino.com>
 * @id $id$
 */
@DisallowConcurrentExecution
public class OrderJob implements Job {

    private RoundService roundService;
    private AbstractBot bot;

    /**
     * Default constructor.
     */
    public OrderJob() {
        roundService = RoundService.INSTANCE;
        bot = AbstractBot.INSTANCE;
    }

    /**
     * @inheritDoc
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Calendar scheduledFireTime = Calendar.getInstance();
        scheduledFireTime.setTime(context.getScheduledFireTime());

        if (scheduledFireTime.get(Calendar.MINUTE) % 10 == 9) {
            // Reschedule job since we are between two rounds
            roundService.rescheduleOrderAndUpdateJobs();
        } else {
            // Normal behavior
            bot.makeOrder(scheduledFireTime);
            bot.incrementStep();
        }
    }

}
