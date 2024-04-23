package console;

import data.dto.CitizenContacts;
import data.dto.ResponseInfo;
import data.entity.Appeal;
import data.entity.Citizen;
import data.entity.Employee;
import data.type.AppealStatus;
import data.type.UserType;
import service.AuthService;
import service.CitizenService;
import service.EmployeeService;

import java.sql.Connection;
import java.util.Scanner;
import java.util.regex.Pattern;

import static config.TableNames.APPEAL_TABLE_NAME;
import static config.TableNames.EMPLOYEE_TABLE_NAME;

/**
 * Консоль
 */
public class ConsoleHandler {
    private static final Pattern IS_CORRECT_PHONE_NUMBER = Pattern.compile("[0-9]{8,14}");
    private static final Pattern IS_CORRECT_EMAIL = Pattern.compile("^[-A-Za-z0-9._%]+@[-A-Za-z0-9.]+[.][A-Za-z]+$");
    private final AuthService authService;
    private final CitizenService citizenService;
    private final EmployeeService employeeService;
    private final Scanner scanner;

    /**
     * Конструктор
     *
     * @param connection соединение
     */
    public ConsoleHandler(Connection connection) {
        authService = new AuthService(connection);
        citizenService = new CitizenService(connection);
        employeeService = new EmployeeService(connection);
        scanner = new Scanner(System.in);
    }

    /**
     * Запускает консоль
     */
    public void start() {
        String command;

        System.out.println("""
                Для того, чтобы войти, как гражданин, введите "citizen"
                Для того, чтобы войти, как сотрудник, введите "employee"
                Для того, чтобы закрыть приложение, введите "stop"
                """);
        UserType userType;
        loop:
        while (true) {
            command = scanner.next();
            switch (command) {
                case "citizen" -> {
                    userType = UserType.CITIZEN;
                    break loop;
                }
                case "employee" -> {
                    userType = UserType.EMPLOYEE;
                    break loop;
                }
                case "stop" -> {
                    return;
                }
                default -> System.out.println("Введена неверная команда, повторите ввод");
            }
        }

        if (userType == UserType.EMPLOYEE) {
            System.out.println("""
                    Чтобы зарегистрироваться, введите "register"
                    Чтобы авторизоваться, введите "login"
                    """);
            loop:
            while (true) {
                command = scanner.next();
                switch (command) {
                    case "register" -> {
                        registerEmployee();
                        break loop;
                    }
                    case "login" -> {
                        loginEmployee();
                        break loop;
                    }
                    default -> System.out.println("Введена неверная команда, повторите ввод");
                }
            }
        }

        if (userType == UserType.CITIZEN) {
            System.out.println("""
                    Чтобы отправить обращение, введите "send"
                    Чтобы узнать статус обращения и текст ответа на обращение по номеру обращения, введите "statId"
                    Чтобы узнать статусы и id обращений по номеру телефона, введите "statPhNumber"
                    """);
            loop:
            while (true) {
                command = scanner.next();
                switch (command) {
                    case "send" -> {
                        sendAppeal();
                        break loop;
                    }
                    case "statId" -> {
                        getResponseInfoByAppealId();
                        break loop;
                    }
                    case "statPhNumber" -> {
                        getAllAppealStatusesByPhoneNumber();
                        break loop;
                    }
                    default -> System.out.println("Введена неверная команда, повторите ввод");
                }
            }
        }

        if (userType == UserType.EMPLOYEE) {
            System.out.println("""
                    Чтобы получить список не рассмотренных обращений, введите "getAllNotReviewed"
                    Чтобы получить список рассмотренных обращений за последние n дней, введите "getLastReviewed"
                    Чтобы получить информацию об обращении, введите "getAppeal"
                    Чтобы получить контакты автора обращения, введите "getContacts"
                    Чтобы рассмотреть обращение, введите "review"
                    """);
            loop:
            while (true) {
                command = scanner.next();
                switch (command) {
                    case "getAllNotReviewed" -> {
                        getAllNotReviewedAppeals();
                        break loop;
                    }
                    case "getLastReviewed" -> {
                        getLastReviewedAppeals();
                        break loop;
                    }
                    case "getAppeal" -> {
                        getAppealByAppealId();
                        break loop;
                    }
                    case "getContacts" -> {
                        getCitizenContactsByAppealId();
                        break loop;
                    }
                    case "review" -> {
                        reviewAppeal();
                        break loop;
                    }
                    default -> System.out.println("Введена неверная команда, повторите ввод");
                }
            }
        }
    }

    /**
     * Регистрирует рабочего
     */
    private void registerEmployee() {
        boolean isRegister = false;
        while (!isRegister) {
            System.out.println("Введите желаемый логин и пароль");
            String login = scanner.next();
            String password = scanner.next();
            try {
                isRegister = authService.registerEmployee(new Employee(login, password));
            } catch (Exception e) {
                System.out.println("Произошла ошибка при регистрации в таблице " + EMPLOYEE_TABLE_NAME + "\n" + e.getMessage());
            }
            if (!isRegister) {
                System.out.println("Пользователь с таким логином уже есть");
            }
        }
    }

    /**
     * Авторизует рабочего
     */
    private void loginEmployee() {
        boolean isLogin = false;
        while (!isLogin) {
            System.out.println("Введите логин и пароль");
            String login = scanner.next();
            String password = scanner.next();
            try {
                isLogin = authService.loginEmployee(new Employee(login, password));
            } catch (Exception e) {
                System.out.println("Произошла ошибка при авторизации в таблице " + EMPLOYEE_TABLE_NAME + "\n" + e.getMessage());
            }
            if (!isLogin) {
                System.out.println("Введен неверный логин или пароль");
            }
        }
    }

    /**
     * Отправляет сообщение гражданина
     */
    private void sendAppeal() {
        boolean isSendAppeal = false;
        while (!isSendAppeal) {
            System.out.println("Введите фамилию");
            String surname;
            while ((surname = scanner.next()).isBlank()) {
                System.out.println("Фамилия не может быть пустой");
            }

            System.out.println("Введите имя");
            String name;
            while ((name = scanner.next()).isBlank()) {
                System.out.println("Имя не может быть пустым");
            }

            System.out.println("Введите номер телефона");
            String phoneNumber;
            while (!IS_CORRECT_PHONE_NUMBER.matcher((phoneNumber = scanner.next())).find()) {
                System.out.println("Некорректный номер телефона");
            }

            System.out.println("Введите электронную почту");
            String email;
            while (!IS_CORRECT_EMAIL.matcher((email = scanner.next())).find()) {
                System.out.println("Некорректная почта");
            }

            scanner.nextLine();
            System.out.println("Введите текст обращения на следующей строке");
            String requestText = scanner.nextLine();
            try {
                System.out.println("Ваше обращение отправлено, номер обращения = " +
                        citizenService.sendAppeal(new Citizen(surname, name, phoneNumber, email), requestText));
                isSendAppeal = true;
            } catch (Exception e) {
                System.out.println("Произошла ошибка при отправке обращения в БД " + "\n" + e.getMessage());
            }
        }
    }

    /**
     * Выводит статус обращения и текст ответа на обращение по id обращения
     */
    private void getResponseInfoByAppealId() {
        ResponseInfo responseInfo = null;
        while (responseInfo == null) {
            long appealId = parseAppealId();

            try {
                responseInfo = citizenService.getResponseInfoByAppealId(appealId);
            } catch (Exception e) {
                System.out.println("Произошла ошибка при получении статуса обращения и текста ответа в таблице " +
                        APPEAL_TABLE_NAME + "\n" + e.getMessage());
            }
        }

        switch (responseInfo.status()) {
            case AppealStatus.CREATED -> System.out.println("Обращение ещё не рассмотрено");
            case AppealStatus.PROCESSED -> System.out.printf("""
                    Обращение рассмотрено,
                    Текст ответа: %s
                    """, responseInfo.responseText());
            case AppealStatus.REJECTED -> System.out.println("Обращение отклонено");
        }
    }

    /**
     * Выводит все статусы и id обращений по номеру телефона гражданина
     */
    private void getAllAppealStatusesByPhoneNumber() {
        boolean isGot = false;
        while (!isGot) {
            System.out.println("Введите номер телефона");
            String phoneNumber;
            while (!IS_CORRECT_PHONE_NUMBER.matcher((phoneNumber = scanner.next())).find()) {
                System.out.println("Некорректный номер телефона");
            }
            try {
                citizenService.getAllAppealStatusesByPhoneNumber(phoneNumber).forEach(userRequestStatus -> {
                    System.out.println("Номер обращения: " + userRequestStatus.appealId());
                    switch (userRequestStatus.status()) {
                        case AppealStatus.CREATED -> System.out.println("Обращение ещё не рассмотрено");
                        case AppealStatus.PROCESSED -> System.out.println("Обращение рассмотрено");
                        case AppealStatus.REJECTED -> System.out.println("Обращение отклонено");
                    }
                });
                isGot = true;
            } catch (Exception e) {
                System.out.println("Произошла ошибка при получении статусов обращений по номеру телефона в БД " + "\n" + e.getMessage());
            }
        }
    }

    /**
     * Выводит все не рассмотренные обращения
     */
    private void getAllNotReviewedAppeals() {
        employeeService.getAllNotReviewedAppeals().forEach(appeal -> System.out.printf("""
                        --------------------------------------------
                        id обращения: %d
                        id гражданина, отправившего обращение: %d
                        статус обращения: %s
                        дата создания обращения: %s
                        дата последнего обновления обращения: %s
                        --------------------------------------------%n""",
                appeal.getId(), appeal.getCitizenId(), appeal.getStatus(),
                appeal.getCreatedDateTime(), appeal.getUpdatedDateTime()));
    }

    /**
     * Выводит рассмотренные обращения за последние n дней
     */
    private void getLastReviewedAppeals() {
        Long days = null;
        while (days == null) {
            System.out.println("Введите количество дней, за которые хотите получить обращения");
            try {
                days = Long.parseLong(scanner.next());
            } catch (Exception e) {
                System.out.println("Ошибка при чтении количества дней" + "\n" + e.getMessage());
            }
        }
        employeeService.getLastReviewedAppeals(days).forEach(appeal -> System.out.printf("""
                        --------------------------------------------
                        id обращения: %d
                        id гражданина, отправившего обращение: %d
                        статус обращения: %s
                        дата создания обращения: %s
                        дата последнего обновления обращения: %s
                        --------------------------------------------%n""",
                appeal.getId(), appeal.getCitizenId(), appeal.getStatus(),
                appeal.getCreatedDateTime(), appeal.getUpdatedDateTime()));
    }

    /**
     * Выводит информацию об обращении
     */
    private void getAppealByAppealId() {
        Appeal appeal = null;
        while (appeal == null) {
            System.out.println("Введите id обращения");
            long appealId = parseAppealId();
            try {
                appeal = employeeService.getAppealByAppealId(appealId);
                System.out.printf("""
                                --------------------------------------------
                                id обращения: %d
                                id гражданина, отправившего обращение: %d
                                статус обращения: %s
                                текст обращения: %s
                                текст ответа: %s
                                дата создания обращения: %s
                                дата последнего обновления обращения: %s
                                --------------------------------------------""",
                        appeal.getId(), appeal.getCitizenId(), appeal.getStatus(),
                        appeal.getRequestText(), appeal.getResponseText() == null ? "ответ отсутствует" : appeal.getResponseText(),
                        appeal.getCreatedDateTime(), appeal.getUpdatedDateTime());
            } catch (Exception e) {
                System.out.println("Произошла ошибка при получении обращения в таблице " +
                        APPEAL_TABLE_NAME + "\n" + e.getMessage());
            }
        }
    }

    /**
     * Выводит контакты автора обращения
     */
    private void getCitizenContactsByAppealId() {
        CitizenContacts citizenContacts = null;
        while (citizenContacts == null) {
            System.out.println("Введите id обращения");
            long appealId = parseAppealId();
            try {
                citizenContacts = employeeService.getCitizenContactsByAppealId(appealId);
                System.out.printf("""
                                --------------------------------------------
                                номер телефона гражданина: %s
                                электронная почта гражданина: %s
                                --------------------------------------------""",
                        citizenContacts.phoneNumber(), citizenContacts.email());
            } catch (Exception e) {
                System.out.println("Произошла ошибка при получении обращения в БД " + "\n" + e.getMessage());
            }
        }
    }

    /**
     * Отвечает на обращение
     */
    private void reviewAppeal() {
        long appealId = parseAppealId();

        AppealStatus appealStatus = null;
        while (appealStatus == null) {
            System.out.println("""
                    Введите новый статус обращения
                    PROCESSED - если одобрено
                    REJECTED - если отклонено
                    """);
            try {
                appealStatus = AppealStatus.valueOf(scanner.next());
            } catch (Exception e) {
                System.out.println("Ошибка при чтении статуса" + "\n" + e.getMessage());
            }
        }

        String responseText = "";
        if (appealStatus == AppealStatus.PROCESSED) {
            System.out.println("Введите текст ответа");
            responseText = scanner.next();
        }
        employeeService.reviewAppeal(appealId, responseText, appealStatus);
    }

    /**
     * Парсит id обращения
     * @return id обращения
     */
    private long parseAppealId() {
        Long appealId = null;
        while (appealId == null) {
            System.out.println("Введите id обращения");
            try {
                appealId = Long.parseLong(scanner.next());
            } catch (Exception e) {
                System.out.println("Ошибка при чтении идентификатора" + "\n" + e.getMessage());
            }
        }
        return appealId;
    }
}
