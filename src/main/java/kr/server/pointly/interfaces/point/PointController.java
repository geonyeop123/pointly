package kr.server.pointly.interfaces.point;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class PointController implements PointDocs{


    @Override
    @PostMapping("/api/v1/users/{userId}/points/charge")
    public ResponseEntity<PointResponse.ChargeResponse> requestCharge(
            @PathVariable Long userId
            , @RequestBody PointRequest.RequestCharge request) {
        return ResponseEntity.ok(new PointResponse.ChargeResponse(userId, 1L, 1000L, "TOSS", LocalDateTime.of(2025, 7, 18, 0, 0, 0)));
    }

    @Override
    @PatchMapping("/api/v1/users/{userId}/points/charge/approve")
    public ResponseEntity<PointResponse.ChargeCompletedResponse> completeCharge(
            @PathVariable Long userId
            , @RequestBody PointRequest.CompleteCharge request) {
        return ResponseEntity.ok(new PointResponse.ChargeCompletedResponse(1L, 10000L,1L, 5000L, "TOSS", LocalDateTime.of(2025, 7, 18, 0, 0, 0)));
    }

    @Override
    @PatchMapping("/api/v1/users/{userId}/points/charge/cancel")
    public ResponseEntity<PointResponse.ChargeCanceledResponse> cancelCharge(
            @PathVariable Long userId
            , @RequestBody PointRequest.CancelCharge request) {
        return ResponseEntity.ok(new PointResponse.ChargeCanceledResponse(1L, 1L, LocalDateTime.of(2025, 7, 18, 0, 0, 0)));
    }
}
