package fr.kissy.droidarena.java.dto;

import fr.kissy.droidarena.java.enums.EnumPlayerField;

import java.util.Map;

/**
 * The player DTO.
 * Used to map JSON response from <code>list</code> api.
 *
 * @author Guillaume Le Biller <lebiller@ekino.com>
 * @id $id$
 */
@SuppressWarnings("unchecked")
public class PlayerDTO {

    private String id;
    private String name;
    private int score;
    private OrderDTO order;

    /**
     * Default constructor.
     *
     * @param player The JSON representation of the player.
     */
    public PlayerDTO(Map<String, Object> player) {
        id = ((Map<String, String>) player.get(EnumPlayerField.ID.getColumn())).get(EnumPlayerField.OID.getColumn());
        name = (String) player.get(EnumPlayerField.NAME.getColumn());
        score = ((Double) player.get(EnumPlayerField.SCORE.getColumn())).intValue();
        order = new OrderDTO((Map<String, Object>) player.get(EnumPlayerField.ORDER.getColumn()));
    }

    /**
     * Get the player id.
     *
     * @return The player id.
     */
    public String getId() {
        return id;
    }

    /**
     * Get the player name.
     *
     * @return The player name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the player score.
     *
     * @return The player score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Get the player Order.
     *
     * @return The player order DTO.
     */
    public OrderDTO getOrder() {
        return order;
    }

}
