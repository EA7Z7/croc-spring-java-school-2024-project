package util;

import exception.CreateTableException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static config.TableNames.*;

/**
 * Создаёт таблицы в БД
 */
public class PrepareDatabaseUtil {
    private static final String CREATE_CITIZEN_TABLE = "CREATE TABLE IF NOT EXISTS " +
            CITIZEN_TABLE_NAME +
            "(id IDENTITY PRIMARY KEY, " +
            "surname VARCHAR(100), " +
            "name VARCHAR(100), " +
            "phone_number VARCHAR(14) UNIQUE, " +
            "email VARCHAR(100))";
    private static final String CREATE_APPEAL_TABLE = "CREATE TABLE IF NOT EXISTS " +
            APPEAL_TABLE_NAME +
            "(id IDENTITY PRIMARY KEY, " +
            "citizen_id BIGINT, " +
            "FOREIGN KEY (citizen_id) REFERENCES " + CITIZEN_TABLE_NAME + " (id) ON DELETE CASCADE, " +
            "status VARCHAR(10) CHECK (status in ('CREATED', 'PROCESSED', 'REJECTED')), " +
            "request_text VARCHAR, " +
            "response_text VARCHAR, " +
            "created_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "updated_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
    private static final String CREATE_EMPLOYEE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            EMPLOYEE_TABLE_NAME +
            "(login VARCHAR(100) PRIMARY KEY, " +
            "password VARCHAR(100))";

    private final Connection connection;

    /**
     * Конструктор
     *
     * @param connection соединение
     */
    public PrepareDatabaseUtil(Connection connection) {
        this.connection = connection;
    }

    /**
     * Создаёт таблицу
     *
     * @param createTable запрос на создание таблицы
     * @param tableName название таблицы
     */
    private void createTable(String createTable, String tableName) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTable);
        } catch (SQLException e) {
            throw new CreateTableException(tableName, e);
        }
    }

    /**
     * Создаёт все таблицы
     */
    public void createDbTables() {
        createTable(CREATE_CITIZEN_TABLE, CITIZEN_TABLE_NAME);
        createTable(CREATE_APPEAL_TABLE, APPEAL_TABLE_NAME);
        createTable(CREATE_EMPLOYEE_TABLE, EMPLOYEE_TABLE_NAME);
    }
}
