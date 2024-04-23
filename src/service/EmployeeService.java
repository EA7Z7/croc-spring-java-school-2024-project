package service;

import data.dto.CitizenContacts;
import data.entity.Appeal;
import data.repository.AppealDAO;
import data.repository.CitizenDAO;
import data.type.AppealStatus;
import exception.ServiceException;

import java.sql.Connection;
import java.util.List;

/**
 * Сервис для работы с рабочим
 */
public class EmployeeService {
    private final CitizenDAO citizenDAO;
    private final AppealDAO appealDAO;

    /**
     * Конструктор
     *
     * @param connection соединение
     */
    public EmployeeService(Connection connection) {
        this.citizenDAO = new CitizenDAO(connection);
        this.appealDAO = new AppealDAO(connection);
    }

    /**
     * Возвращает все не рассмотренные обращения
     *
     * @return список не рассмотренных обращений
     */
    public List<Appeal> getAllNotReviewedAppeals() {
        return appealDAO.getAllNotReviewedAppeals();
    }

    /**
     * Возвращает рассмотренные обращения за последние n дней
     *
     * @param days количество дней
     * @return список рассмотренных обращений за последние n дней
     */
    public List<Appeal> getLastReviewedAppeals(long days) {
        return appealDAO.getLastReviewedAppeals(days);
    }

    /**
     * Возвращает обращение по id обращения
     *
     * @param appealId id обращения
     * @return обращение
     */
    public Appeal getAppealByAppealId(long appealId) {
        if (!appealDAO.findAppealById(appealId)) {
            throw new ServiceException(String.format("Обращение с id = %d не найдено", appealId));
        }

        return appealDAO.getAppealByAppealId(appealId);
    }

    /**
     * Возвращает контакты автора обращения
     *
     * @param appealId id обращения
     * @return структура вида {номер телефона, электронная почта}
     */
    public CitizenContacts getCitizenContactsByAppealId(long appealId) {
        if (!appealDAO.findAppealById(appealId)) {
            throw new ServiceException(String.format("Обращение с id = %d не найдено", appealId));
        }
        long citizenId = appealDAO.getCitizenIdByAppealId(appealId);

        return citizenDAO.getCitizenContacts(citizenId);
    }

    /**
     * Отвечает на обращение
     *
     * @param appealId     id обращения
     * @param responseText текст ответа
     * @param status       новый статус обращения
     */
    public void reviewAppeal(long appealId, String responseText, AppealStatus status) {
        if (!appealDAO.findAppealById(appealId)) {
            throw new ServiceException(String.format("Обращение с id = %d не найдено", appealId));
        }

        appealDAO.reviewAppeal(appealId, responseText, status);
    }
}
