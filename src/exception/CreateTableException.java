package exception;

/**
 * Исключение, выбрасываемое при ошибке создания таблиц
 */
public class CreateTableException extends RuntimeException {
    private final String tableName;

    public CreateTableException(String tableName, Exception e) {
        super(String.format("""
                Произошла ошибка при создании таблицы %s
                %s""", tableName, e.getMessage()), e);
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
