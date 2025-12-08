package backend.knowhow.domain.member.dto.response;

import backend.knowhow.domain.member.domain.MemberDeviceSetting;

public record DeviceSettingResponse(
        int fontLevel,
        int volumeLevel
) {
    public static DeviceSettingResponse from(MemberDeviceSetting setting) {
        return new DeviceSettingResponse(setting.getFontLevel(), setting.getVolumeLevel());
    }

    public static DeviceSettingResponse from(int fontLevel, int volumeLevel) {
        return new DeviceSettingResponse(fontLevel, volumeLevel);
    }
}
