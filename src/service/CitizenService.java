package service;

import data.dto.UserRequestsStatuses;
import data.entity.Appeal;
import data.entity.Citizen;
import data.repository.AppealDAO;
import data.repository.CitizenDAO;
import data.type.AppealStatus;
import exception.ServiceException;

import java.sql.Connection;
import java.util.List;

/**
 * Сервис для работы с гражданином
 */
public class CitizenService {
    private final CitizenDAO citizenDAO;
    private final AppealDAO appealDAO;

    /**
     * Конструктор
     *
     * @param connection соединение
     */
    public CitizenService(Connection connection) {
        this.citizenDAO = new CitizenDAO(connection);
        this.appealDAO = new AppealDAO(connection);
    }

    /**
     * Отправляет обращение в БД
     *
     * @param citizen     гражданин
     * @param requestText обращение
     * @return id обращения
     */
    public long sendAppeal(Citizen citizen, String requestText) {
        if (!citizenDAO.findCitizenByPhoneNumber(citizen.getPhoneNumber())) {
            citizen.setId(citizenDAO.createCitizen(citizen));
        } else {
            citizen.setId(citizenDAO.getCitizenIdByPhoneNumber(citizen.getPhoneNumber()));
        }

        if (!citizenDAO.getCitizenByPhoneNumber(citizen.getPhoneNumber()).equals(citizen)) {
            throw new ServiceException(String.format("По номеру %s записан другой гражданин", citizen.getPhoneNumber()));
        }

        Appeal appeal = new Appeal(citizen.getId(), requestText);
        return appealDAO.createAppeal(appeal);
    }

    /**
     * Возвращает статус обращения по id обращения
     *
     * @param appealId id обращения
     * @return статус обращения
     */
    public AppealStatus getAppealStatusByAppealId(long appealId) {
        if (!appealDAO.findAppealById(appealId)) {
            throw new ServiceException(String.format("Обращение с id = %d не найдено", appealId));
        }
        return appealDAO.getAppealStatusById(appealId);
    }

    /**
     * Возвращает все статусы и id обращений по номеру телефона
     *
     * @param phoneNumber номер телефона
     * @return список {id обращения, статус}
     */
    public List<UserRequestsStatuses> getAllAppealStatusesByPhoneNumber(String phoneNumber) {
        if (!citizenDAO.findCitizenByPhoneNumber(phoneNumber)) {
            throw new ServiceException(String.format("Гражданин с номером телефона %s не найден", phoneNumber));
        }
        long citizenId = citizenDAO.getCitizenIdByPhoneNumber(phoneNumber);

        if (!appealDAO.findAppealByCitizenId(citizenId)) {
            throw new ServiceException(String.format("Не найдено обращений для гражданина с номером %s", phoneNumber));
        }
        return appealDAO.getAllAppealStatusesByCitizenId(citizenId);
    }
}
