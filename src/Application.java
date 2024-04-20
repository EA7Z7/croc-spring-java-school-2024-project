import data.entity.Appeal;
import data.entity.Citizen;
import data.entity.Employee;
import data.repository.AppealDAO;
import data.repository.CitizenDAO;
import data.repository.EmployeeDAO;
import data.type.AppealStatus;
import data.type.UserType;
import service.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import static data.repository.constants.TableNames.*;

public class Application {
    private static final String PATH_TO_DATABASE = "./resources/db/database";
    private static final String CREATE_CITIZEN_TABLE = "CREATE TABLE IF NOT EXISTS " +
            CITIZEN_TABLE_NAME +
            "(id IDENTITY PRIMARY KEY, " +
            "surname VARCHAR(100), " +
            "name VARCHAR(100), " +
            "phone_number VARCHAR(14), " +
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

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:" + PATH_TO_DATABASE, "egor", "")) {
            createDatabase(connection);
            String command;
            Service service = new Service(connection);

            System.out.println("""
                    Для того, чтобы войти, как гражданин, введите "citizen"
                    Для того, чтобы войти, как сотрудник, введите "employee"
                    Для того, чтобы закрыть приложение, введите "stop"
                    """);
            UserType userType;
            loop: while (true) {
                command = scanner.next();
                switch (command) {
                    case "citizen":
                        userType = UserType.CITIZEN;
                        break loop;
                    case "employee":
                        userType = UserType.EMPLOYEE;
                        break loop;
                    case "stop":
                        return;
                    default:
                        System.out.println("Введена неверная команда, повторите ввод");
                        break;
                }
            }

            if (userType == UserType.EMPLOYEE) {
                System.out.println("""
                        Чтобы зарегистрироваться, введите "register"
                        Чтобы авторизоваться, введите "login"
                        """);
                loop: while (true) {
                    command = scanner.next();
                    switch (command) {
                        case "register":
                            register(service);
                            break loop;
                        case "login":
                            //todo
                            break;
                        default:
                            System.out.println("Введена неверная команда, повторите ввод");
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createDatabase(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_CITIZEN_TABLE);
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при создании таблицы " + CITIZEN_TABLE_NAME + "\n" + e.getMessage());
            throw new RuntimeException(e);
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_APPEAL_TABLE);
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при создании таблицы " + APPEAL_TABLE_NAME + "\n" + e.getMessage());
            throw new RuntimeException(e);
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_EMPLOYEE_TABLE);
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при создании таблицы " + EMPLOYEE_TABLE_NAME + "\n" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void register(Service service) {
        boolean isRegister = false;
        while (!isRegister) {
            System.out.println("Введите желаемый логин и пароль");
            String login = scanner.next();
            String password = scanner.next();
            try {
                isRegister = service.registerEmployee(new Employee(login, password));
            } catch (Exception e) {
                System.out.println("Произошла ошибка при регистрации " + EMPLOYEE_TABLE_NAME + "\n" + e.getMessage());
            }
            if (!isRegister) {
                System.out.println("Пользователь с таким логином уже есть");
            }
        }
    }
}
