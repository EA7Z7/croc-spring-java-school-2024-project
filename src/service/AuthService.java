package service;

import data.entity.Employee;
import data.repository.EmployeeDAO;

import java.sql.Connection;

/**
 * Сервис для регистрации и авторизации рабочих
 */
public class AuthService {
    private final EmployeeDAO employeeDAO;

    /**
     * Конструктор
     *
     * @param connection соединение
     */
    public AuthService(Connection connection) {
        this.employeeDAO = new EmployeeDAO(connection);
    }

    /**
     * Регистрирует работника
     *
     * @param employee работник
     * @return true, если с таким логином ещё нет пользователей, иначе false
     */
    public boolean registerEmployee(Employee employee) {
        if (employeeDAO.checkEmployeeExists(employee.login())) {
            return false;
        }
        employeeDAO.createEmployee(employee);
        return true;
    }

    /**
     * Авторизует работника
     *
     * @param employee работник
     * @return true, если с таким логином есть и пароль совпал, иначе false
     */
    public boolean loginEmployee(Employee employee) {
        return employeeDAO.checkEmployeeExists(employee.login()) &&
                employeeDAO.getEmployeePassword(employee.login()).equals(employee.password());
    }
}
