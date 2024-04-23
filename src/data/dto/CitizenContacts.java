package data.dto;

/**
 * Контакты гражданина
 *
 * @param phoneNumber номер телефона
 * @param email       электронная почта
 */
public record CitizenContacts(String phoneNumber, String email) {
}
