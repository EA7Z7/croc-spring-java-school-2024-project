package data.dto;

/**
 * Параметры для соединения с БД
 *
 * @param pathToDatabase путь к БД
 * @param login          логин
 * @param password       пароль
 */
public record DbProperties(String pathToDatabase, String login, String password) {
}
