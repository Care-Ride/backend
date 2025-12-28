package backend.knowhow.domain.mission.controller;

import backend.knowhow.domain.mission.domain.MissionCode;
import backend.knowhow.domain.mission.dto.MissionResponse;
import backend.knowhow.domain.mission.dto.PointBalanceResponse;
import backend.knowhow.domain.mission.dto.PointHistoryResponse;
import backend.knowhow.domain.mission.service.MissionService;
import backend.knowhow.domain.mission.service.PointService;
import backend.knowhow.global.common.response.ApiResponse;
import backend.knowhow.global.security.CurrentUser;
import backend.knowhow.global.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/missions")
public class MissionController {

    private final MissionService missionService;
    private final PointService pointService;

    // 미션 목록 조회
    @GetMapping
    public ApiResponse<List<MissionResponse>> getMyMissions(@CurrentUser MemberPrincipal memberPrincipal) {
        return ApiResponse.success(missionService.getMyMissions(memberPrincipal.getId()));
    }

    // 미션 포인트 수령
    @PostMapping("/{missionCode}/claim")
    public ApiResponse<Void> claimMissionPoint(@CurrentUser MemberPrincipal memberPrincipal, @PathVariable MissionCode missionCode) {
        missionService.claimPoint(memberPrincipal.getId(), missionCode);
        return ApiResponse.success();
    }

    // 포인트 잔액 조회
    @GetMapping("/points/balance")
    public ApiResponse<PointBalanceResponse> getBalance(@CurrentUser MemberPrincipal memberPrincipal) {
        return ApiResponse.success(pointService.getBalance(memberPrincipal.getId()));
    }

    // 포인트 내역 조회
    @GetMapping("/points/history")
    public ApiResponse<Page<PointHistoryResponse>> getHistory(@CurrentUser MemberPrincipal memberPrincipal, @RequestParam int page, @RequestParam int size) {
        return ApiResponse.success((pointService.getPointHistory(memberPrincipal.getId(), page, size)));
    }

    // 포인트 사용하기
    @PostMapping("/points/use")
    public ApiResponse<Void> usePoint(@CurrentUser MemberPrincipal memberPrincipal, @RequestParam int amount) {
        pointService.usePoint(memberPrincipal.getId(), amount, "포인트 사용");
        return ApiResponse.success();
    }
}
