package fr.kissy.droidarena.java;

import fr.kissy.droidarena.java.bot.AbstractBot;
import fr.kissy.droidarena.java.bot.BotFactory;
import fr.kissy.droidarena.java.service.RoundService;
import fr.kissy.droidarena.java.service.RequestService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Guillaume Le Biller <lebiller@ekino.com>
 * @version $Id$
 */
public class Main {

    public static String CSRF_USER;
    public static String CSRF_TOKEN;

    /**
     * Main method.
     *
     * @param args The args to start the bot.
     */
    public static void main (String[] args) {
        // Parse args
        if (args.length < 3) {
            System.out.println("Usage \"droid-arena-java BOT_CLASS_NAME CSRF_USER CSRF_TOKEN\"");
            return;
        }

        CSRF_USER = args[1];
        CSRF_TOKEN = args[2];

        // Init bot
        String botName = args[0];
        RequestService requestService = new RequestService();
        AbstractBot bot = BotFactory.createInstance(botName, requestService);
        if (bot == null) {
            System.out.println("Cannot create the bot with class name \"" + botName + "\"");
            return;
        }
        new RoundService(requestService);


        // Read the command
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println("\u001B[33m======>\u001B[0m\"" + line + "\"");
                System.out.flush();

                if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
