package fr.kissy.droidarena.java.enums;

/**
 * The status field list.
 *
 * @author Guillaume Le Biller <lebiller@ekino.com>
 * @id $id$
 */
public enum EnumStatusField {
    STATUS("s");

    private String column;

    /**
     * Default constructor.
     *
     * @param column The column name.
     */
    EnumStatusField(String column) {
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
