package exception;

/**
 * Исключение, выбрасываемое при ошибке в работе DAO
 */
public class DAOException extends RuntimeException {
    private final String sqlQuery;

    public DAOException(String sqlQuery, Exception e) {
        super(String.format("""
                Произошла ошибка при запросе в таблицу %s
                %s""", sqlQuery, e.getMessage()), e);
        this.sqlQuery = sqlQuery;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }
}
