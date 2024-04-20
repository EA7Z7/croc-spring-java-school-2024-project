package data.repository;

import data.dto.CitizenContacts;
import data.entity.Citizen;

import java.sql.*;

import static data.repository.constants.TableNames.CITIZEN_TABLE_NAME;

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

    public Long createCitizen(Citizen citizen) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BY_PHONE_NUMBER)) {
            preparedStatement.setString(1, citizen.getPhoneNumber());
            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    throw new IllegalArgumentException("Уже есть гражданин с таким номером");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
            throw new RuntimeException(e);
        }
    }

    public Long findCitizenIdByPhoneNumber(String phoneNumber) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID_BY_PHONE_NUMBER)) {
            preparedStatement.setString(1, phoneNumber);
            try (ResultSet result = preparedStatement.executeQuery()) {
                result.next();
                return result.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CitizenContacts getCitizenContacts(long citizenId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CONTACTS_BY_ID)) {
            preparedStatement.setLong(1, citizenId);
            try (ResultSet result = preparedStatement.executeQuery()) {
                result.next();
                return new CitizenContacts(result.getString("phone_number"), result.getString("email"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
