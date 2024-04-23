import console.ConsoleHandler;
import data.dto.DbProperties;
import exception.ReadException;
import util.PrepareDatabaseUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Application {
    private static final String PATH_TO_DATABASE_PROPERTIES = "./resources/dbProperties.txt";

    public static void main(String[] args) {
        DbProperties dbProperties = readDbProperties();
        try (Connection connection = DriverManager.getConnection(dbProperties.pathToDatabase(),
                dbProperties.login(), dbProperties.password())) {
            PrepareDatabaseUtil prepareDatabaseService = new PrepareDatabaseUtil(connection);
            prepareDatabaseService.createDbTables();
            ConsoleHandler console = new ConsoleHandler(connection);
            console.start();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка " + e);
        }
    }

    /**
     * Читает параметры для соединения с БД
     *
     * @return параметры для соединения с БД
     */
    private static DbProperties readDbProperties() {
        String pathToDatabase;
        String login;
        String password;
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_TO_DATABASE_PROPERTIES))) {
            pathToDatabase = reader.readLine();
            login = reader.readLine();
            password = reader.readLine();
        } catch (IOException e) {
            throw new ReadException(PATH_TO_DATABASE_PROPERTIES, e);
        }
        return new DbProperties(pathToDatabase, login, password);
    }
}