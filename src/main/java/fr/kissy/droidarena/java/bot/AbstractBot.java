package fr.kissy.droidarena.java.bot;

import fr.kissy.droidarena.java.dto.PlayerDTO;
import fr.kissy.droidarena.java.service.RequestService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract bot used to implement the bot logic.
 *
 * @author Guillaume Le Biller <lebiller@ekino.com>
 * @version $Id$
 */
public abstract class AbstractBot {

    public static AbstractBot INSTANCE;

    protected RequestService requestService;
    protected final AtomicInteger currentStep = new AtomicInteger(0);
    protected final List<PlayerDTO> players = new ArrayList<>();

    /**
     * Init the bot if needed.
     */
    public AbstractBot(RequestService requestService) {
        INSTANCE = this;
        this.requestService = requestService;
    }

    /**
     * Refresh the player list.
     *
     * @param response The response from server.
     */
    public void refreshPlayers(List<PlayerDTO> response) {
        synchronized (players) {
            players.clear();
            players.addAll(response);
        }
    }

    /**
     * Increment the step counter.
     */
    public void incrementStep() {
        currentStep.incrementAndGet();
    }

    /**
     * Set the step counter.
     */
    public void setStep(int value) {
        currentStep.set(value);
    }

    /**
     * Reset the step counter.
     */
    public void resetStep() {
        setStep(0);
    }

    /**
     * Method called after players have been updated.
     */
    public abstract void afterPlayersUpdate(Calendar scheduledFireTime);

    /**
     * Method called when the order has to be placed.
     *
     * @param scheduledFireTime The schedule fire time.
     */
    public abstract void makeOrder(Calendar scheduledFireTime);

    /**
     * Method called when the round is up and need to be reset.
     *
     * @param scheduledFireTime The schedule fire time.
     */
    public abstract void resetRound(Calendar scheduledFireTime);

}
