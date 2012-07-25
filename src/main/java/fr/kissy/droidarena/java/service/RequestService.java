package fr.kissy.droidarena.java.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.kissy.droidarena.java.Main;
import fr.kissy.droidarena.java.dto.PlayerDTO;
import fr.kissy.droidarena.java.dto.StatusDTO;
import fr.kissy.droidarena.java.dto.TimeDTO;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * The request service create easy methods
 * to retrieve player list and give orders.
 *
 * @author Guillaume Le Biller <lebiller@ekino.com>
 * @id $id$
 */
@SuppressWarnings("FieldCanBeLocal")
public class RequestService {

    public static RequestService INSTANCE;

    private static final String BASE_URL = "http://api-droid-arena.kissy.fr/";
    private static final String TIME = BASE_URL + "time";
    private static final String LIST = BASE_URL + "list";
    private static final String HISTORY = BASE_URL + "history";
    private static final String PLUS = BASE_URL + "plus/%s";
    private static final String MINUS = BASE_URL + "minus/%s";
    private static final String INVERSE = BASE_URL + "inverse/%s";
    private static final String DOUBLE = BASE_URL + "double/%s";

    private final Gson gson = new Gson();
    private final Type objectMapType = new TypeToken<Map<String, Object>>(){}.getType();
    private final HttpClient HTTP_CLIENT = new HttpClient(new MultiThreadedHttpConnectionManager());
    private final HttpMethod GET_METHOD = new GetMethod();

    /**
     * Default constructor.
     */
    public RequestService() {
        INSTANCE = this;
        GET_METHOD.addRequestHeader("X-CSRF-User", Main.CSRF_USER);
        GET_METHOD.addRequestHeader("X-CSRF-Token", Main.CSRF_TOKEN);
    }

    /**
     * Retrieve the time infos.
     *
     * @return The time infos.
     * @throws java.io.IOException The exception if any.
     */
    public TimeDTO getTime() throws IOException {
        Calendar now = Calendar.getInstance();
        GET_METHOD.setURI(new URI(TIME, false));
        HTTP_CLIENT.executeMethod(GET_METHOD);

        Map<String, Object> response = gson.fromJson(GET_METHOD.getResponseBodyAsString(), objectMapType);
        GET_METHOD.releaseConnection();

        return new TimeDTO(now, response);
    }

    /**
     * Retrieve the player list.
     *
     * @return The list of players.
     * @throws java.io.IOException The exception if any.
     */
    public List<PlayerDTO> getList() throws IOException {
        GET_METHOD.setURI(new URI(LIST, false));
        HTTP_CLIENT.executeMethod(GET_METHOD);

        Map<String, List<Map<String, Object>>> response = gson.fromJson(GET_METHOD.getResponseBodyAsString(), objectMapType);
        GET_METHOD.releaseConnection();

        List<PlayerDTO> DTOs = new ArrayList<PlayerDTO>();
        for (Map<String, Object> player : response.get("p")) {
            DTOs.add(new PlayerDTO(player));
        }
        return DTOs;
    }

    /**
     * Make a plus order.
     *
     * @param targetId The target id.
     * @return The status of the order.
     * @throws java.io.IOException The exception if any.
     */
    public StatusDTO setPlus(String targetId) throws IOException {
        return new StatusDTO(getOrderResponse(PLUS, targetId));
    }

    /**
     * Make a minus order.
     *
     * @param targetId The target id.
     * @return The status of the order.
     * @throws java.io.IOException The exception if any.
     */
    public StatusDTO setMinus(String targetId) throws IOException {
        return new StatusDTO(getOrderResponse(MINUS, targetId));
    }

    /**
     * Make a inverse order.
     *
     * @param targetId The target id.
     * @return The status of the order.
     * @throws java.io.IOException The exception if any.
     */
    public StatusDTO setInverse(String targetId) throws IOException {
        return new StatusDTO(getOrderResponse(INVERSE, targetId));
    }

    /**
     * Make a double order.
     *
     * @param targetId The target id.
     * @return The status of the order.
     * @throws java.io.IOException The exception if any.
     */
    public StatusDTO setDouble(String targetId) throws IOException {
        return new StatusDTO(getOrderResponse(DOUBLE, targetId));
    }

    /**
     * Send and get the response for an order.
     *
     * @param baseUrl The order url.
     * @param targetId The target id.
     * @return The order status.
     * @throws java.io.IOException Exception if any.
     */
    private Map<String, String> getOrderResponse(String baseUrl, String targetId) throws IOException {
        String url = String.format(baseUrl, targetId);
        GET_METHOD.setURI(new URI(url, false));
        HTTP_CLIENT.executeMethod(GET_METHOD);

        Map<String, String> response = gson.fromJson(GET_METHOD.getResponseBodyAsString(), objectMapType);
        GET_METHOD.releaseConnection();

        return response;
    }

}
