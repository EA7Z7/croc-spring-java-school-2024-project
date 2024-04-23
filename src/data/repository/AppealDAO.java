package data.repository;

import data.dto.UserRequestsStatuses;
import data.entity.Appeal;
import data.type.AppealStatus;
import exception.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static config.TableNames.APPEAL_TABLE_NAME;

/**
 * DAO для работы с обращениями
 */
public class AppealDAO {
    private static final String INSERT_APPEAL = "INSERT INTO " + APPEAL_TABLE_NAME +
            " (citizen_id, status, request_text) VALUES (?,?,?)";
    private static final String SELECT_APPEAL_STATUS_BY_ID = "SELECT status FROM " + APPEAL_TABLE_NAME + " WHERE id = ?";
    private static final String SELECT_APPEAL_STATUS_BY_PHONE_NUMBER = "SELECT id, status FROM " +
            APPEAL_TABLE_NAME +
            " WHERE citizen_id = ?";
    private static final String SELECT_REQUEST_INFORMATION =
            "SELECT id, citizen_id, status, created_date_time, updated_date_time FROM " +
                    APPEAL_TABLE_NAME +
                    " WHERE status = 'CREATED'";
    private static final String SELECT_LAST_REVIEWED_APPEALS =
            "SELECT id, citizen_id, status, created_date_time, updated_date_time FROM " +
                    APPEAL_TABLE_NAME +
                    " WHERE updated_date_time > DATEADD('DAY',-?, CURRENT_TIMESTAMP) AND status <> 'CREATED'";
    private static final String SELECT_ALL_BY_ID = "SELECT * FROM " + APPEAL_TABLE_NAME + " WHERE id = ?";
    private static final String SELECT_ALL_BY_CITIZEN_ID = "SELECT * FROM " + APPEAL_TABLE_NAME + " WHERE citizen_id = ?";
    private static final String SELECT_CITIZEN_ID_BY_ID = "SELECT citizen_id FROM " + APPEAL_TABLE_NAME + " WHERE id = ?";
    private static final String UPDATE_APPEAL_RESPONSE_TEXT = "UPDATE " + APPEAL_TABLE_NAME +
            " SET response_text = ?, status = ?, updated_date_time = CURRENT_TIMESTAMP WHERE id = ?";
    private final Connection connection;

    /**
     * Конструктор
     *
     * @param connection связь с БД
     */
    public AppealDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Создаёт запись с обращением
     *
     * @param appeal обращение
     * @return id обращения
     */
    public Long createAppeal(Appeal appeal) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_APPEAL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, appeal.getCitizenId());
            preparedStatement.setString(2, appeal.getStatus().toString());
            preparedStatement.setString(3, appeal.getRequestText());
            preparedStatement.executeUpdate();
            try (ResultSet result = preparedStatement.getGeneratedKeys()) {
                result.next();
                return result.getLong(1);
            }
        } catch (SQLException e) {
            throw new DAOException(INSERT_APPEAL, e);
        }
    }

    /**
     * Проверяет наличия обращения по id обращения
     *
     * @param appealId id обращения
     * @return true если есть в БД, false если нет
     */
    public boolean findAppealById(long appealId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BY_ID)) {
            preparedStatement.setLong(1, appealId);
            try (ResultSet result = preparedStatement.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            throw new DAOException(SELECT_ALL_BY_ID, e);
        }
    }

    /**
     * Проверяет, что по id гражданина существуют обращения
     *
     * @param citizenId id гражданина
     * @return true если есть в БД, false если нет
     */
    public boolean findAppealByCitizenId(long citizenId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BY_CITIZEN_ID)) {
            preparedStatement.setLong(1, citizenId);
            try (ResultSet result = preparedStatement.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            throw new DAOException(SELECT_ALL_BY_CITIZEN_ID, e);
        }
    }

    /**
     * Возвращает статус обращения по id обращения
     *
     * @param appealId id обращения
     * @return статус обращения
     */
    public AppealStatus getAppealStatusById(long appealId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_APPEAL_STATUS_BY_ID)) {
            preparedStatement.setLong(1, appealId);
            try (ResultSet result = preparedStatement.executeQuery()) {
                result.next();
                return AppealStatus.valueOf(result.getString("status"));
            }
        } catch (SQLException e) {
            throw new DAOException(SELECT_APPEAL_STATUS_BY_ID, e);
        }
    }

    /**
     * Возвращает все статусы и id обращений по id гражданина
     *
     * @param citizenId id гражданина
     * @return список структур {id обращения, статус}
     */
    public List<UserRequestsStatuses> getAllAppealStatusesByCitizenId(long citizenId) {
        List<UserRequestsStatuses> listUserRequestsStatuses = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_APPEAL_STATUS_BY_PHONE_NUMBER)) {
            preparedStatement.setLong(1, citizenId);
            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    listUserRequestsStatuses.add(new UserRequestsStatuses(result.getLong("id"),
                            AppealStatus.valueOf(result.getString("status"))));
                }
                result.next();
            }
        } catch (SQLException e) {
            throw new DAOException(SELECT_APPEAL_STATUS_BY_PHONE_NUMBER, e);
        }
        return listUserRequestsStatuses;
    }

    /**
     * Возвращает все не рассмотренные обращения
     *
     * @return список не рассмотренных обращений
     */
    public List<Appeal> getAllNotReviewedAppeals() {
        List<Appeal> appeals = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_REQUEST_INFORMATION)) {
            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    appeals.add(new Appeal(result.getLong("id"),
                            result.getLong("citizen_id"),
                            AppealStatus.valueOf(result.getString("status")),
                            result.getTimestamp("created_date_time").toLocalDateTime(),
                            result.getTimestamp("updated_date_time").toLocalDateTime()));
                }
            }
        } catch (SQLException e) {
            throw new DAOException(SELECT_REQUEST_INFORMATION, e);
        }
        return appeals;
    }

    /**
     * Возвращает все рассмотренные обращения за последние n дней
     *
     * @param days количество дней
     * @return список рассмотренных обращений за последние n дней
     */
    public List<Appeal> getLastReviewedAppeals(long days) {
        List<Appeal> lastReviewedAppeals = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LAST_REVIEWED_APPEALS)) {
            preparedStatement.setLong(1, days);
            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    lastReviewedAppeals.add(new Appeal(result.getLong("id"),
                            result.getLong("citizen_id"),
                            AppealStatus.valueOf(result.getString("status")),
                            result.getTimestamp("created_date_time").toLocalDateTime(),
                            result.getTimestamp("updated_date_time").toLocalDateTime()));
                }
            }
        } catch (SQLException e) {
            throw new DAOException(SELECT_LAST_REVIEWED_APPEALS, e);
        }
        return lastReviewedAppeals;
    }

    /**
     * Возвращает обращение по id обращения
     *
     * @param appealId id обращения
     * @return обращение
     */
    public Appeal getAppealByAppealId(long appealId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BY_ID)) {
            preparedStatement.setLong(1, appealId);
            try (ResultSet result = preparedStatement.executeQuery()) {
                result.next();
                return new Appeal(result.getLong("id"),
                        result.getLong("citizen_id"),
                        AppealStatus.valueOf(result.getString("status")),
                        result.getString("request_text"),
                        result.getString("response_text"),
                        result.getTimestamp("created_date_time").toLocalDateTime(),
                        result.getTimestamp("updated_date_time").toLocalDateTime());
            }
        } catch (SQLException e) {
            throw new DAOException(SELECT_ALL_BY_ID, e);
        }
    }

    /**
     * Возвращает id автора обращения по id обращения
     *
     * @param appealId id обращения
     * @return id автора обращения
     */
    public long getCitizenIdByAppealId(long appealId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CITIZEN_ID_BY_ID)) {
            preparedStatement.setLong(1, appealId);
            try (ResultSet result = preparedStatement.executeQuery()) {
                result.next();
                return result.getLong("citizen_id");
            }
        } catch (SQLException e) {
            throw new DAOException(SELECT_CITIZEN_ID_BY_ID, e);
        }
    }

    /**
     * Рассматривает обращение
     *
     * @param appealId     id обращения
     * @param responseText текст ответа
     * @param status       новый статус обращения
     */
    public void reviewAppeal(long appealId, String responseText, AppealStatus status) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_APPEAL_RESPONSE_TEXT)) {
            preparedStatement.setString(1, responseText);
            preparedStatement.setString(2, status.toString());
            preparedStatement.setLong(3, appealId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DAOException(UPDATE_APPEAL_RESPONSE_TEXT, e);
        }
    }
}
