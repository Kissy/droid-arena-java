package fr.kissy.droidarena.java.enums;

/**
 * The order field list.
 *
 * @author Guillaume Le Biller <lebiller@ekino.com>
 * @id $id$
 */
public enum EnumOrderField {
    OID("$oid"),
    COMMAND("c"),
    TARGET("t");

    private String column;

    /**
     * Default constructor.
     *
     * @param column The column name.
     */
    EnumOrderField(String column) {
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
