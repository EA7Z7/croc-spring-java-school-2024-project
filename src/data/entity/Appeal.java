package data.entity;

import data.type.AppealStatus;

import java.time.LocalDateTime;

public final class Appeal {
    private Long id;
    private final Long citizenId;
    private AppealStatus status;
    private String requestText;
    private String responseText;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;

    public Appeal(Long citizenId, String requestText) {
        this.citizenId = citizenId;
        this.requestText = requestText;
        this.status = AppealStatus.CREATED;
    }

    public Appeal(Long id, Long citizenId, AppealStatus status, LocalDateTime createdDateTime, LocalDateTime updatedDateTime) {
        this.id = id;
        this.citizenId = citizenId;
        this.status = status;
        this.createdDateTime = createdDateTime;
        this.updatedDateTime = updatedDateTime;
    }

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

    public void responseAppeal(String responseText, AppealStatus status) {
        this.responseText = responseText;
        this.status = status;
        updatedDateTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
