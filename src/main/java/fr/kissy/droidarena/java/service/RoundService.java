package fr.kissy.droidarena.java.service;

import fr.kissy.droidarena.java.dto.TimeDTO;
import fr.kissy.droidarena.java.job.OrderJob;
import fr.kissy.droidarena.java.job.UpdateJob;
import fr.kissy.droidarena.java.job.RoundJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.util.Calendar;

/**
 * The round service handle the job scheduling.
 *
 * @author Guillaume Le Biller <lebiller@ekino.com>
 * @version $Id$
 */
public class RoundService {

    private static final String ROUND_JOB = "RoundJob";
    private static final String ROUND_TRIGGER = "RoundTrigger";
    private static final String ORDER_JOB = "OrderJob";
    private static final String ORDER_TRIGGER = "OrderTrigger";
    private static final String UPDATE_JOB = "UpdateJob";
    private static final String UPDATE_TRIGGER = "UpdateTrigger";

    public static RoundService INSTANCE;

    private Scheduler scheduler;
    private RequestService requestService;

    /**
     * Default constructor.
     *
     * @param requestService The request service to inject.
     */
    public RoundService(RequestService requestService) {
        INSTANCE = this;
        this.requestService = requestService;

        // Scheduler
        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            scheduler = schedulerFactory.getScheduler();
            scheduler.start();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }

        // Jobs
        try {
            scheduleJobs();
        } catch (IOException | SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Schedule the needed jobs.
     *
     * @throws java.io.IOException The exception.
     * @throws SchedulerException The exception.
     */
    private void scheduleJobs() throws IOException, SchedulerException {
        TimeDTO timeDTO = requestService.getTime();
        scheduleOrderJob(timeDTO);
        scheduleUpdateJob(timeDTO);
        scheduleResetJob(timeDTO);
    }

    /**
     * Schedule the order job.
     *
     * @param timeDTO The time to schedule the jobs.
     * @throws java.io.IOException The exception.
     * @throws SchedulerException The exception.
     */
    private void scheduleOrderJob(TimeDTO timeDTO) throws IOException, SchedulerException {
        JobDetail orderJob = JobBuilder.newJob(OrderJob.class).withIdentity(ORDER_JOB).build();
        Trigger orderTrigger = buildTwoSecondsTrigger(timeDTO, 0, 0).withIdentity(ORDER_TRIGGER).build();
        scheduler.scheduleJob(orderJob, orderTrigger);
    }

    /**
     * Create a trigger every two seconds that starts at a given time.
     *
     * @param timeDTO The time to create trigger.
     * @param minuteOffset The minute offset.
     * @param millisecondsOffet The milliseconds offset.
     * @return The created trigger.
     */
    private TriggerBuilder buildTwoSecondsTrigger(TimeDTO timeDTO, int minuteOffset, int millisecondsOffet) {
        Calendar startDate = Calendar.getInstance();

        // If we are at the end of a round, then skip one minute
        if (startDate.get(Calendar.MINUTE) % 9 == 0) {
            ++minuteOffset;
        }

        int secondToAdd = Math.round(timeDTO.getOffset() / 1000);
        if (secondToAdd % 2 != 0) {
            ++secondToAdd;
        }
        int newSeconds = startDate.get(Calendar.SECOND) + startDate.get(Calendar.SECOND) % 2 + secondToAdd;
        startDate.set(Calendar.SECOND, newSeconds);
        startDate.set(Calendar.MILLISECOND, 0);

        // Offsets if needed
        startDate.add(Calendar.MINUTE, minuteOffset);
        startDate.add(Calendar.MILLISECOND, millisecondsOffet);

        return TriggerBuilder.newTrigger()
                .startAt(startDate.getTime())
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(2).repeatForever()
                );
    }

    /**
     * Schedule the player job.
     *
     * @param timeDTO The time to schedule the jobs.
     * @throws java.io.IOException The exception.
     * @throws SchedulerException The exception.
     */
    private void scheduleUpdateJob(TimeDTO timeDTO) throws IOException, SchedulerException {
        JobDetail updateJob = JobBuilder.newJob(UpdateJob.class).withIdentity(UPDATE_JOB).build();
        Trigger updateTrigger = buildTwoSecondsTrigger(timeDTO, 0, 1500).withIdentity(UPDATE_TRIGGER).build();
        scheduler.scheduleJob(updateJob, updateTrigger);
    }

    /**
     * Schedule the reset job.
     *
     * @param timeDTO The time to schedule the jobs.
     * @throws java.io.IOException The exception.
     * @throws SchedulerException The exception.
     */
    private void scheduleResetJob(TimeDTO timeDTO) throws IOException, SchedulerException {
        JobDetail resetJob = JobBuilder.newJob(RoundJob.class).withIdentity(ROUND_JOB).build();

        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(timeDTO.getNextRound());
        Trigger resetTrigger = TriggerBuilder.newTrigger()
                .startAt(startDate.getTime())
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInMinutes(10).repeatForever()
                ).withIdentity(ROUND_TRIGGER).build();

        scheduler.scheduleJob(resetJob, resetTrigger);
    }

    /**
     * Reschedule the Order and Update jobs in order
     * to start be ready for the next round. (now plus one minute)
     */
    public void rescheduleOrderAndUpdateJobs() {
        try {
            TimeDTO timeDTO = requestService.getTime();

            // Order
            Trigger orderTrigger = buildTwoSecondsTrigger(timeDTO, 1, 0).withIdentity(ORDER_TRIGGER).build();
            scheduler.rescheduleJob(new TriggerKey(ORDER_TRIGGER), orderTrigger);

            // Update
            Trigger updateTrigger = buildTwoSecondsTrigger(timeDTO, 1, 1500).withIdentity(UPDATE_TRIGGER).build();
            scheduler.rescheduleJob(new TriggerKey(UPDATE_TRIGGER), updateTrigger);
        } catch (SchedulerException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
