package data.dto;

import data.type.AppealStatus;

/**
 * Статус обращения гражданина
 *
 * @param appealId номер обращения
 * @param status   статус обращения
 */
public record UserRequestsStatuses(Long appealId, AppealStatus status) {
}
