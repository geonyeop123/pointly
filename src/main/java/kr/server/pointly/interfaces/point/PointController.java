package kr.server.pointly.interfaces.point;

import kr.server.pointly.application.point.PointFacade;
import kr.server.pointly.application.point.PointResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class PointController implements PointDocs{

    private final PointFacade pointFacade;

    @Override
    @PostMapping("/api/v1/users/{userId}/points/charge")
    public ResponseEntity<PointResponse.RequestCharge> requestCharge(
            @PathVariable("userId") Long userId
            , @RequestBody PointRequest.RequestCharge request) {
        PointResult.RequestCharge result = pointFacade.requestCharge(request.toCriteria(userId));
        return ResponseEntity.ok(PointResponse.RequestCharge.from(result));
    }

    @Override
    @PatchMapping("/api/v1/users/{userId}/points/charge/approve")
    public ResponseEntity<PointResponse.CompletedCharge> completeCharge(
            @PathVariable("userId") Long userId
            , @RequestBody PointRequest.CompleteCharge request) {
        return ResponseEntity.ok(new PointResponse.CompletedCharge(1L, 10000L,1L, 5000L, "TOSS", LocalDateTime.of(2025, 7, 18, 0, 0, 0)));
    }

    @Override
    @PatchMapping("/api/v1/users/{userId}/points/charge/cancel")
    public ResponseEntity<PointResponse.CanceledCharge> cancelCharge(
            @PathVariable("userId") Long userId
            , @RequestBody PointRequest.CancelCharge request) {
        PointResult.CancelCharge result = pointFacade.cancelCharge(request.toCriteria(userId));
        return ResponseEntity.ok(PointResponse.CanceledCharge.from(result));
    }
}
