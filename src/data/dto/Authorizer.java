package data.dto;

import data.entity.Citizen;

import java.util.Scanner;

public class Authorizer {
    private static final String CITIZEN = "citizen";
    private static final String EMPLOYEE = "employee";
    private static final Scanner scanner = new Scanner(System.in);
    public static void authorize() {
        System.out.println("""
                Для того, чтобы войти, как гражданин, введите citizen
                Для того, чтобы войти, как сотрудник, введите employee
                """);

        String request = scanner.next();
        if (CITIZEN.equals(request)) {
            while (!authorizeCitizen()) {

            }
        } else if (EMPLOYEE.equals(request)) {

        }
    }

    private static boolean authorizeCitizen() {
        System.out.println("Введите фамилию, имя, номер телефона и электронную почту через пробел");
        Citizen citizen = new Citizen(scanner.next(), scanner.next(), scanner.next(), scanner.next());

        System.out.println("""
                    Если данные были введены корректно, введите YES
                    Иначе введите любую другую последовательность символов и авторизуйтесь заново""");

        if (!"YES".equals(scanner.next())) {
            return false;
        }

        System.out.println("Вы успешно авторизовались, " + citizen.getName());
        return true;
    }
}
