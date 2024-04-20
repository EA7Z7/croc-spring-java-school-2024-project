package service;

import data.entity.Employee;
import data.repository.AppealDAO;
import data.repository.CitizenDAO;
import data.repository.EmployeeDAO;

import java.sql.Connection;

public class Service {
    private final AppealDAO appealDAO;
    private final CitizenDAO citizenDAO;
    private final EmployeeDAO employeeDAO;

    public Service(Connection connection) {
        this.appealDAO = new AppealDAO(connection);
        this.citizenDAO = new CitizenDAO(connection);
        this.employeeDAO = new EmployeeDAO(connection);
    }

    public boolean registerEmployee(Employee employee) {
        if (employeeDAO.checkEmployeeExists(employee.login())) {
            return false;
        }
        employeeDAO.createEmployee(employee);
        return true;
    }
}
