package backend.knowhow.domain.vehicle.service;

import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.member.repository.MemberRepository;
import backend.knowhow.domain.vehicle.domain.Vehicle;
import backend.knowhow.domain.vehicle.dto.request.VehicleCreateRequest;
import backend.knowhow.domain.vehicle.dto.response.VehicleResponse;
import backend.knowhow.domain.vehicle.dto.request.VehicleUpdateRequest;
import backend.knowhow.domain.vehicle.repository.VehicleRepository;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final MemberRepository memberRepository;

    // 내 차량 목록
    @Transactional(readOnly = true)
    public List<VehicleResponse> getMyVehicles(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));

        return vehicleRepository.findByOwner(member).stream()
                .map(VehicleResponse::from)
                .toList();
    }

    // 차량 등록
    @Transactional
    public VehicleResponse registerVehicle(Long memberId, VehicleCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));

        boolean makeActive = !vehicleRepository.existsByOwner(member);
        Vehicle vehicle = Vehicle.create(
                member,
                request.name(),
                request.carNumber(),
              null,
                makeActive
        );

        vehicleRepository.save(vehicle);
        return VehicleResponse.from(vehicle);
    }

    // 차량 정보 수정
    @Transactional
    public VehicleResponse updateVehicle(Long memberId, Long vehicleId, VehicleUpdateRequest request) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new BaseException(ErrorType.VEHICLE_NOT_FOUND));

        if (!vehicle.getOwner().getId().equals(memberId)) {
            throw new BaseException(ErrorType.VEHICLE_ACCESS_DENIED);
        }

        vehicle.updateInfo(request.name(), request.carNumber());
        return VehicleResponse.from(vehicle);
    }

    // 선택 차량 변경
    @Transactional
    public void changeActiveVehicle(Long memberId, Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new BaseException(ErrorType.VEHICLE_NOT_FOUND));
        if (!vehicle.getOwner().getId().equals(memberId)) {
            throw new BaseException(ErrorType.VEHICLE_ACCESS_DENIED);
        }
        if (vehicle.isActive()) {
            return;
        }
        vehicleRepository.deactivateAllActiveByOwner(vehicle.getOwner());
        vehicle.activate();
    }

    // BLE 연동
    @Transactional
    public void bindBleDevice(Long memberId, Long vehicleId, String bleDeviceId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new BaseException(ErrorType.VEHICLE_NOT_FOUND));

        if (!vehicle.getOwner().getId().equals(memberId)) {
            throw new BaseException(ErrorType.VEHICLE_ACCESS_DENIED);
        }

        vehicle.bindBle(bleDeviceId);
    }

    // BLE 연동 해제
    @Transactional
    public void unbindBleDevice(Long memberId, Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new BaseException(ErrorType.VEHICLE_NOT_FOUND));

        if (!vehicle.getOwner().getId().equals(memberId)) {
            throw new BaseException(ErrorType.VEHICLE_ACCESS_DENIED);
        }

        vehicle.unbindBle();
    }
}
