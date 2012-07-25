package fr.kissy.droidarena.java.enums;

/**
 * The time field list.
 *
 * @author Guillaume Le Biller <lebiller@ekino.com>
 * @id $id$
 */
public enum EnumTimeField {
    ROUND("r"),
    UPDATE("u"),
    CURRENT("c");

    private String column;

    /**
     * Default constructor.
     *
     * @param column The column name.
     */
    EnumTimeField(String column) {
        this.column = column;
    }

    /**
     * Get the column name.
     *
     * @return The column name.
     */
    public String getColumn() {
        return column;
    }
}
