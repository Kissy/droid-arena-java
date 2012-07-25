package fr.kissy.droidarena.java.job;

import fr.kissy.droidarena.java.bot.AbstractBot;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Calendar;

/**
 * The round job is triggered everytime the round is over.
 * The schedule time is every 10 minutes (starting from the 9nth minute).
 * The round counter is reset at the end of the job.
 *
 * <b>Note : </b>The job's execution time cannot exceed 1 minute.
 *
 * @author Guillaume Le Biller <lebiller@ekino.com>
 * @id $id$
 */
@DisallowConcurrentExecution
public class RoundJob implements Job {

    private AbstractBot abstractBot;

    /**
     * Default constructor.
     */
    public RoundJob() {
        abstractBot = AbstractBot.INSTANCE;
    }

    /**
     * @inheritDoc
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Calendar scheduledFireTime = Calendar.getInstance();
        scheduledFireTime.setTime(context.getScheduledFireTime());

        abstractBot.resetRound(scheduledFireTime);
        abstractBot.resetStep();
    }
}
