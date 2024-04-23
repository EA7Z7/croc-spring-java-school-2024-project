package data.repository;

import data.entity.Employee;
import exception.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static config.TableNames.EMPLOYEE_TABLE_NAME;

/**
 * DAO для работы с работниками
 */
public class EmployeeDAO {
    private static final String INSERT_EMPLOYEE = "INSERT INTO " + EMPLOYEE_TABLE_NAME + " VALUES (?,?)";
    private static final String SELECT_ALL_BY_LOGIN = "SELECT * FROM " + EMPLOYEE_TABLE_NAME + " WHERE login = ?";
    private static final String SELECT_PASSWORD_BY_LOGIN = "SELECT password FROM " + EMPLOYEE_TABLE_NAME + " WHERE login = ?";
    private final Connection connection;

    /**
     * Конструктор
     *
     * @param connection соединение
     */
    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Создаёт запись с работником
     *
     * @param employee работник
     */
    public void createEmployee(Employee employee) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EMPLOYEE)) {
            preparedStatement.setString(1, employee.login());
            preparedStatement.setString(2, employee.password());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DAOException(INSERT_EMPLOYEE, e);
        }
    }

    /**
     * Проверяет наличие работника в таблице по логину
     *
     * @param login логин
     * @return true если есть, false если нет
     */
    public boolean checkEmployeeExists(String login) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            try (ResultSet result = preparedStatement.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            throw new DAOException(SELECT_ALL_BY_LOGIN, e);
        }
    }

    /**
     * Получает пароль работника по логину
     *
     * @param login логин
     * @return пароль
     */
    public String getEmployeePassword(String login) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PASSWORD_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            try (ResultSet result = preparedStatement.executeQuery()) {
                result.next();
                return result.getString("password");
            }
        } catch (SQLException e) {
            throw new DAOException(SELECT_PASSWORD_BY_LOGIN, e);
        }
    }
}
