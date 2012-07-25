package fr.kissy.droidarena.java.bot.impl;

import fr.kissy.droidarena.java.Main;
import fr.kissy.droidarena.java.bot.AbstractBot;
import fr.kissy.droidarena.java.dto.PlayerDTO;
import fr.kissy.droidarena.java.dto.StatusDTO;
import fr.kissy.droidarena.java.service.RequestService;

import java.io.IOException;
import java.util.Calendar;

/**
 * Simple bot implementation that made PLUS order on him
 * and MINUS order on the first ennemy player.
 *
 * @author Guillaume Le Biller <lebiller@ekino.com>
 * @version $Id$
 */
public class SimpleBotImpl extends AbstractBot {

    String nextTargetId = Main.CSRF_USER;

    /**
     * Init the bot if needed.
     */
    public SimpleBotImpl(RequestService requestService) {
        // We need super() call to init the INSTANCE variable !
        super(requestService);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void afterPlayersUpdate(Calendar scheduledFireTime) {
        // Choose the first player that is not me !
        for (PlayerDTO playerDTO : players) {
            if (Main.CSRF_USER.equals(playerDTO.getId())) {
                continue;
            }
            nextTargetId = playerDTO.getId();
            break;
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void makeOrder(Calendar scheduledFireTime) {
        try {
            StatusDTO statusDTO;
            if (scheduledFireTime.get(Calendar.SECOND) % 4 == 0 || Main.CSRF_USER.equals(nextTargetId)) {
                statusDTO = requestService.setPlus(Main.CSRF_USER);
            } else {
                statusDTO = requestService.setMinus(nextTargetId);
            }
            System.out.println(statusDTO.getStatus());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void resetRound(Calendar scheduledFireTime) {

    }
}
