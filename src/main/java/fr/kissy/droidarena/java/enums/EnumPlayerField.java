package fr.kissy.droidarena.java.enums;

/**
 * The player field list.
 *
 * @author Guillaume Le Biller <lebiller@ekino.com>
 * @id $id$
 */
public enum EnumPlayerField {
    ID("_id"),
    OID("$oid"),
    NAME("n"),
    SCORE("s"),
    ORDER("o");

    private String column;

    /**
     * Default constructor.
     *
     * @param column The column name.
     */
    EnumPlayerField(String column) {
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
