package backend.knowhow.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DeviceSettingRequest(
        // null이면 기본값 2로 처리
        Integer fontLevel,
        Integer volumeLevel
) {
}
