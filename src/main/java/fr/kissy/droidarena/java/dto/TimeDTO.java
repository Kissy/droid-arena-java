package fr.kissy.droidarena.java.dto;

import fr.kissy.droidarena.java.enums.EnumTimeField;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Time DTO.
 * Map the informations from the servers about different times.
 *
 * @author Guillaume Le Biller <lebiller@ekino.com>
 * @id $id$
 */
public class TimeDTO {

    private Date now;
    private Long nextRound;
    private Long nextUpdate;
    private Long currentTime;

    /**
     * Default constructor.
     *
     * @param now The request start time. Used to calculate offset.
     * @param time The JSON representation of the response.
     */
    public TimeDTO(Calendar now, Map<String, Object> time) {
        this.now = now.getTime();
        nextRound = ((Double) time.get(EnumTimeField.ROUND.getColumn())).longValue();
        nextUpdate = ((Double) time.get(EnumTimeField.UPDATE.getColumn())).longValue();
        currentTime = ((Double) time.get(EnumTimeField.CURRENT.getColumn())).longValue();
    }

    /**
     * Get the current date (start request time).
     *
     * @return The current date (start request time).
     */
    public Date getNow() {
        return now;
    }

    /**
     * Get the next round time.
     *
     * @return The next round time.
     */
    public Long getNextRound() {
        return nextRound;
    }

    /**
     * Get the next update time.
     *
     * @return The next update time.
     */
    public Long getNextUpdate() {
        return nextUpdate;
    }

    /**
     * Get the current server time.
     *
     * @return The current server time.
     */
    public Long getCurrentTime() {
        return currentTime;
    }

    /**
     * Get the offset between you and the server.
     *
     * @return The offset between you and the server.
     */
    public Long getOffset() {
        return now.getTime() - currentTime;
    }
}
