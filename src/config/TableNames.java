package config;

/**
 * Названия таблиц в БД
 */
public class TableNames {
    public static final String CITIZEN_TABLE_NAME = "citizen";
    public static final String APPEAL_TABLE_NAME = "appeal";
    public static final String EMPLOYEE_TABLE_NAME = "employee";

    /**
     * Конструктор.
     */
    private TableNames() {
        // Конструктор задан только для того, чтобы экземпляр класса случайно не создали.
    }
}
