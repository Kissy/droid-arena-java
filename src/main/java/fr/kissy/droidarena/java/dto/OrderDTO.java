package fr.kissy.droidarena.java.dto;

import fr.kissy.droidarena.java.enums.EnumOrderField;

import java.util.Map;

/**
 * The order DTO.
 * Used to map JSON response from <code>list</code> api.
 * The order is embedded in the PlayerDTO.
 *
 * @author Guillaume Le Biller <lebiller@ekino.com>
 * @version $Id$
*/
class OrderDTO {
    private int command;
    private String target;

    /**
     * Default constructor.
     *
     * @param order The JSON representation of an Order.
     */
    @SuppressWarnings("unchecked")
    public OrderDTO(Map<String, Object> order) {
        command = ((Double) order.get(EnumOrderField.COMMAND.getColumn())).intValue();
        Map<String, String> targetObject = (Map<String, String>) order.get(EnumOrderField.TARGET.getColumn());
        if (targetObject != null) {
            target = targetObject.get(EnumOrderField.OID.getColumn());
        } else {
            target = null;
        }
    }

    /**
     * Get the order command.
     *
     * @return The order command.
     */
    public int getCommand() {
        return command;
    }

    /**
     * Get the order traget.
     *
     * @return The order traget.
     */
    public String getTarget() {
        return target;
    }

}
