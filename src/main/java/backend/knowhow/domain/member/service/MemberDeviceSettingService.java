package backend.knowhow.domain.member.service;

import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.member.domain.MemberDeviceSetting;
import backend.knowhow.domain.member.dto.request.DeviceSettingRequest;
import backend.knowhow.domain.member.dto.response.DeviceSettingResponse;
import backend.knowhow.domain.member.repository.MemberDeviceSettingRepository;
import backend.knowhow.domain.member.repository.MemberRepository;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberDeviceSettingService {

    private final MemberDeviceSettingRepository settingRepository;
    private final MemberRepository memberRepository;
    
    @Transactional
    public DeviceSettingResponse saveOrUpdateSetting(Long memberId, DeviceSettingRequest request) {
        int fontLevel = request.fontLevel() == null ? 2 : request.fontLevel();  // null값이면 2로 기본 설정
        int volumeLevel = request.volumeLevel() == null ? 2 : request.volumeLevel();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));

        // 설정이 없으면 새로 생성
        MemberDeviceSetting setting = settingRepository
                .findByMember(member)
                .orElseGet(() ->
                        MemberDeviceSetting.builder()
                                .member(member)
                                .volumeLevel(volumeLevel)
                                .fontLevel(fontLevel)
                                .build()
                );

        // 설정이 이미 있으면 덮어쓰기 (fontLevel, volumeLevel 모두 교체)
        setting.changeLevels(fontLevel, volumeLevel);

        // 새로 만든 경우든, 수정한 경우든 save
        MemberDeviceSetting saved = settingRepository.save(setting);

        return DeviceSettingResponse.from(saved);
    }

    // 세팅 조회
    @Transactional(readOnly = true)
    public DeviceSettingResponse getSetting(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));

        return settingRepository.findByMember(member)
                .map(setting -> DeviceSettingResponse.from(setting))
                // DB에 없으면 기본값(2,2)로 응답 (DB에 저장은 안 함)
                .orElseGet(() -> DeviceSettingResponse.from(2, 2));
    }

    // 세팅 저장 유무
    @Transactional(readOnly = true)
    public boolean existsSettingByMember(Member member){
        return settingRepository.existsByMember(member);
    }
}