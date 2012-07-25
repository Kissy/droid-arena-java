package fr.kissy.droidarena.java.bot;

import fr.kissy.droidarena.java.service.RequestService;

import java.lang.reflect.InvocationTargetException;

/**
 * The bot factory create implemented instance of AbstractBot.
 *
 * @author Guillaume Le Biller <lebiller@ekino.com>
 * @version $Id$
 */
public class BotFactory {

    /**
     * Get the instance.
     *
     * @param botClassName The class name of the bot implementation.
     *                     The package should be ommited and the class must be
     *                     in the package <code>fr.kissy.droidarena.java.bot.impl</code>
     * @param requestService The request service instance to inject.
     * @return The bot implementation instance created.
     */
    public static AbstractBot createInstance(String botClassName, RequestService requestService) {
        String fullBotClassName = BotFactory.class.getPackage().getName() + ".impl." + botClassName;
        try {
            Class botClass = Class.forName(fullBotClassName);
            return (AbstractBot) botClass.getDeclaredConstructor(RequestService.class).newInstance(requestService);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
