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
        '1. 10km 이상 안전하게 주행하세요.\n2. 운전 종료 시 안전 점수 80점 이상을 달성하세요.'
    ),
    (
        'DRIVE_20KM_SAFE',
        '20km 안전운전',
        75,
        'CHALLENGE',
        '1. 20km 이상 안전하게 주행하세요.\n2. 운전 종료 시 안전 점수 80점 이상을 달성하세요.'
    ),
    (
        'DRIVE_30KM_SAFE',
        '30km 안전운전',
        100,
        'CHALLENGE',
        '1. 30km 이상 안전하게 주행하세요.\n2. 운전 종료 시 안전 점수 80점 이상을 달성하세요.'
    )
    ON DUPLICATE KEY UPDATE code = code;
