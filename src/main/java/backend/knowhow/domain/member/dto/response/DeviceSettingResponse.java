package backend.knowhow.domain.member.dto.response;

import backend.knowhow.domain.member.domain.MemberDeviceSetting;

public record DeviceSettingResponse(
        String deviceId,
        int fontLevel,
        int volumeLevel
) {
    public static DeviceSettingResponse from(MemberDeviceSetting setting) {
        return new DeviceSettingResponse(setting.getDeviceId(), setting.getFontLevel(), setting.getVolumeLevel());
    }

    public static DeviceSettingResponse from(String deviceId, int fontLevel, int volumeLevel) {
        return new DeviceSettingResponse(deviceId, fontLevel, volumeLevel);
    }
}
