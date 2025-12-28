package backend.knowhow.domain.mission.controller;

import backend.knowhow.domain.mission.domain.Mission;
import backend.knowhow.domain.mission.domain.MissionCode;
import backend.knowhow.domain.mission.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MissionInitializer implements CommandLineRunner {

    private final MissionRepository missionRepository;

    @Override
    public void run(String... args) {

        saveIfAbsent(
                MissionCode.LINK_GUARDIAN,
                "보호자 연동하기",
                100,
                """
                1. 보호자와 연동을 완료하세요.
                2. 연동이 완료되면 즉시 미션이 달성됩니다.
                """
        );

        saveIfAbsent(
                MissionCode.DRIVE_10KM,
                "10km 안전운전",
                50,
                """
                1. 한 번의 운전에서 총 주행 거리 10km 이상
                2. 운전 종료 시 안전 점수 80점 이상
                """
        );

        saveIfAbsent(
                MissionCode.DRIVE_30KM,
                "30km 안전운전",
                100,
                """
                1. 누적 주행 거리 30km 이상
                2. 모든 운전 기록이 정상 종료되어야 합니다.
                """
        );
    }

    private void saveIfAbsent(
            MissionCode code,
            String title,
            int reward,
            String description
    ) {
        missionRepository.findByCode(code)
                .orElseGet(() ->
                        missionRepository.save(
                                Mission.createMission(title, reward, code, description)
                        )
                );
    }
}
