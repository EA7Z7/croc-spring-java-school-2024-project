package data.entity;

import data.type.AppealStatus;

import java.time.LocalDateTime;

/**
 * Обращение
 */
public final class Appeal {
    private final Long citizenId;
    private Long id;
    private final AppealStatus status;
    private String requestText;
    private String responseText;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;

    /**
     * Конструктор
     *
     * @param citizenId   id гражданина
     * @param requestText текст обращения
     */
    public Appeal(Long citizenId, String requestText) {
        this.citizenId = citizenId;
        this.requestText = requestText;
        this.status = AppealStatus.CREATED;
    }

    /**
     * Конструктор
     *
     * @param id              id обращения
     * @param citizenId       id гражданина
     * @param status          статус обращения
     * @param createdDateTime дата создания обращения
     * @param updatedDateTime дата обновления обращения
     */
    public Appeal(Long id, Long citizenId, AppealStatus status, LocalDateTime createdDateTime, LocalDateTime updatedDateTime) {
        this.id = id;
        this.citizenId = citizenId;
        this.status = status;
        this.createdDateTime = createdDateTime;
        this.updatedDateTime = updatedDateTime;
    }

    /**
     * Конструктор
     *
     * @param id              id обращения
     * @param citizenId       id гражданина
     * @param status          статус обращения
     * @param requestText     текст обращения
     * @param responseText    текст ответа
     * @param createdDateTime дата создания обращения
     * @param updatedDateTime дата обновления обращения
     */
    public Appeal(Long id, Long citizenId, AppealStatus status,
                  String requestText, String responseText,
                  LocalDateTime createdDateTime, LocalDateTime updatedDateTime) {
        this.id = id;
        this.citizenId = citizenId;
        this.requestText = requestText;
        this.responseText = responseText;
        this.status = status;
        this.createdDateTime = createdDateTime;
        this.updatedDateTime = updatedDateTime;
    }

    public Long getId() {
        return id;
    }

    public Long getCitizenId() {
        return citizenId;
    }

    public String getRequestText() {
        return requestText;
    }

    public String getResponseText() {
        return responseText;
    }

    public AppealStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public LocalDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    @Override
    public String toString() {
        return "Appeal{" +
                "id=" + id +
                ", citizenId=" + citizenId +
                ", requestText='" + requestText + '\'' +
                ", responseText='" + responseText + '\'' +
                ", status=" + status +
                ", createdDateTime=" + createdDateTime +
                ", updatedDateTime=" + updatedDateTime +
                '}';
    }
}
