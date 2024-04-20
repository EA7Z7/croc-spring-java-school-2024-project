package data.repository;

import data.entity.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static data.repository.constants.TableNames.EMPLOYEE_TABLE_NAME;

public class EmployeeDAO {
    private static final String INSERT_EMPLOYEE = "INSERT INTO " + EMPLOYEE_TABLE_NAME + " VALUES (?,?)";
    private static final String SELECT_ALL_BY_LOGIN = "SELECT * FROM " + EMPLOYEE_TABLE_NAME + " WHERE login = ?";
    private final Connection connection;

    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }

    public void createEmployee(Employee employee) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EMPLOYEE)) {
            preparedStatement.setString(1, employee.login());
            preparedStatement.setString(2, employee.password());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkEmployeeExists(String login) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            try (ResultSet result = preparedStatement.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
