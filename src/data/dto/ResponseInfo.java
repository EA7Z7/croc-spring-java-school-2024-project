package data.dto;

import data.type.AppealStatus;

public record ResponseInfo(AppealStatus status, String responseText) {
}
