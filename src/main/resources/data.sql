INSERT INTO mission (code, title, reward_point, type, detail)
VALUES
    (
        'LINK_GUARDIAN',
        '보호자 연동하기',
        100,
        'ACHIEVEMENT',
        '1. 보호자와 연동을 완료하세요.\n2. 연동이 완료되면 즉시 미션이 달성됩니다.'
    ),
    (
        'DRIVE_10KM_SAFE',
        '10km 안전운전',
        50,
        'CHALLENGE',
        '1. 한 번의 운전에서 총 주행 거리 10km 이상\n2. 운전 종료 시 안전 점수 80점 이상'
    ),
    (
        'DRIVE_30KM_SAFE',
        '30km 안전운전',
        100,
        'CHALLENGE',
        '1. 누적 주행 거리 30km 이상\n2. 모든 운전 기록이 정상 종료되어야 합니다.'
    )
    ON DUPLICATE KEY UPDATE code = code;
