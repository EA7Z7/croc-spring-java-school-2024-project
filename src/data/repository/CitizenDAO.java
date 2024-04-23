package data.repository;

import data.dto.CitizenContacts;
import data.entity.Citizen;
import exception.DAOException;

import java.sql.*;

import static config.TableNames.CITIZEN_TABLE_NAME;

/**
 * DAO для работы с гражданином
 */
public class CitizenDAO {
    private static final String INSERT_CITIZEN = "INSERT INTO " +
            CITIZEN_TABLE_NAME +
            " (surname, name, phone_number, email) VALUES (?,?,?,?)";
    private static final String SELECT_ALL_BY_PHONE_NUMBER = "SELECT * FROM " + CITIZEN_TABLE_NAME + " WHERE phone_number = ?";
    private static final String SELECT_ID_BY_PHONE_NUMBER = "SELECT id FROM " + CITIZEN_TABLE_NAME + " WHERE phone_number = ?";
    private static final String SELECT_CONTACTS_BY_ID = "SELECT phone_number, email FROM " + CITIZEN_TABLE_NAME + " WHERE id = ?";
    private final Connection connection;


    /**
     * Конструктор
     *
     * @param connection связь с БД
     */
    public CitizenDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Создаёт запись с гражданином
     *
     * @param citizen гражданин
     * @return id гражданина
     */
    public Long createCitizen(Citizen citizen) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CITIZEN, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, citizen.getSurname());
            preparedStatement.setString(2, citizen.getName());
            preparedStatement.setString(3, citizen.getPhoneNumber());
            preparedStatement.setString(4, citizen.getEmail());
            preparedStatement.executeUpdate();
            try (ResultSet result = preparedStatement.getGeneratedKeys()) {
                result.next();
                return result.getLong(1);
            }
        } catch (SQLException e) {
            throw new DAOException(INSERT_CITIZEN, e);
        }
    }

    /**
     * Проверяет наличие гражданина в таблице по номеру телефона
     *
     * @param phoneNumber номер телефона
     * @return true если есть, false если нет
     */
    public boolean findCitizenByPhoneNumber(String phoneNumber) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BY_PHONE_NUMBER)) {
            preparedStatement.setString(1, phoneNumber);
            try (ResultSet result = preparedStatement.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            throw new DAOException(SELECT_ALL_BY_PHONE_NUMBER, e);
        }
    }

    /**
     * Получает гражданина по номеру телефона
     *
     * @param phoneNumber номер телефона
     * @return гражданин
     */
    public Citizen getCitizenByPhoneNumber(String phoneNumber) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BY_PHONE_NUMBER)) {
            preparedStatement.setString(1, phoneNumber);
            try (ResultSet result = preparedStatement.executeQuery()) {
                result.next();
                return new Citizen(result.getLong("id"), result.getString("surname"),
                        result.getString("name"), result.getString("phone_number"),
                        result.getString("email"));
            }
        } catch (SQLException e) {
            throw new DAOException(SELECT_ALL_BY_PHONE_NUMBER, e);
        }
    }

    /**
     * Получает id гражданина по номеру телефона
     *
     * @param phoneNumber номер телефона
     * @return id гражданина
     */
    public Long getCitizenIdByPhoneNumber(String phoneNumber) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID_BY_PHONE_NUMBER)) {
            preparedStatement.setString(1, phoneNumber);
            try (ResultSet result = preparedStatement.executeQuery()) {
                result.next();
                return result.getLong(1);
            }
        } catch (SQLException e) {
            throw new DAOException(SELECT_ID_BY_PHONE_NUMBER, e);
        }
    }

    /**
     * Получает контакты гражданина
     *
     * @param citizenId id гражданина
     * @return структура вида {номер телефона, электронная почта}
     */
    public CitizenContacts getCitizenContacts(long citizenId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CONTACTS_BY_ID)) {
            preparedStatement.setLong(1, citizenId);
            try (ResultSet result = preparedStatement.executeQuery()) {
                result.next();
                return new CitizenContacts(result.getString("phone_number"), result.getString("email"));
            }
        } catch (SQLException e) {
            throw new DAOException(SELECT_CONTACTS_BY_ID, e);
        }
    }
}
