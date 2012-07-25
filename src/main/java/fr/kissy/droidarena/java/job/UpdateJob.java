package fr.kissy.droidarena.java.job;

import fr.kissy.droidarena.java.bot.AbstractBot;
import fr.kissy.droidarena.java.dto.PlayerDTO;
import fr.kissy.droidarena.java.service.RequestService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * The order job is triggered everytime the player list is updated.
 * The schedule time is every 2s (starting from odd second plus 500ms).
 * There is a 1 minute break every 10 minute (the last minute, ie the 9nth minute).
 *
 * <b>Note : </b>The job's execution time cannot exceed 2 seconds.
 *
 * @author Guillaume Le Biller <lebiller@ekino.com>
 * @id $id$
 */
@DisallowConcurrentExecution
public class UpdateJob implements Job {

    private RequestService requestService;
    private AbstractBot abstractBot;

    /**
     * Default constructor.
     */
    public UpdateJob() {
        requestService = RequestService.INSTANCE;
        abstractBot = AbstractBot.INSTANCE;
    }

    /**
     * @inheritDoc
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Calendar scheduledFireTime = Calendar.getInstance();
        scheduledFireTime.setTime(context.getScheduledFireTime());

        try {
            List<PlayerDTO> response = requestService.getList();
            abstractBot.refreshPlayers(response);
            abstractBot.afterPlayersUpdate(scheduledFireTime);
        } catch (IOException e) {
            throw new JobExecutionException();
        }
    }
}
