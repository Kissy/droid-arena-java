package fr.kissy.droidarena.java.dto;

import fr.kissy.droidarena.java.enums.EnumStatusField;

import java.util.Map;

/**
 * The Status DTO.
 * Used to get the status of the request made to the api.
 * The status contains in the response can be one of the following :
 * <ul>
 *     <li><b>success</b> : The request you made was successful</li>
 *     <li><b>params</b> : The parameters was wrong or missing</li>
 *     <li><b>error</b> : There was an error in your request</li>
 *     <li><b>closed</b> : The round is closed or the system is processing orders</li>
 * </ul>
 *
 * @author Guillaume Le Biller <lebiller@ekino.com>
 * @id $id$
 */
public class StatusDTO {

    private String status;

    /**
     * Default constructor.
     *
     * @param status The JSON representation of the status response.
     */
    public StatusDTO(Map<String, String> status) {
        this.status = status.get(EnumStatusField.STATUS.getColumn());
    }


    /**
     * Get the status.
     *
     * @return The status.
     */
    public String getStatus() {
        return status;
    }

}
