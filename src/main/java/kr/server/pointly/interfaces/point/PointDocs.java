package kr.server.pointly.interfaces.point;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "point", description = "point API")
public interface PointDocs {

    @Operation(summary = "포인트 충전 결제 요청", description = "포인트 충전을 요청합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "포인트 충전 요청 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PointResponse.RequestCharge.class),
                    examples = @ExampleObject(value = """
                        {\s
                            "userId": 1,
                            "paymentId": 1,
                            "amount": 5000,
                            "type": "TOSS",
                            "paidRequestAt": "2025-07-18T00:00:00"
                        }
                   \s""")
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "조회된 유저가 없습니다.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = """
                {
                  "code": 400,
                  "message": "조회된 유저가 없습니다."
                }
                """)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "지원하지 않는 결제 유형입니다.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = """
                {
                  "code": 400,
                  "message": "지원하지 않는 결제 유형입니다."
                }
                """)
            )
    )
    ResponseEntity<PointResponse.RequestCharge> requestCharge (
            @PathVariable Long userId,
            @RequestBody PointRequest.RequestCharge request
    );

    @Operation(summary = "포인트 충전 결제 완료", description = "결제가 완료되어 충전 완료를 요청합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "포인트 충전 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PointResponse.CompletedCharge.class),
                    examples = @ExampleObject(value = """
                        {\s
                            "userId": 1,
                            "balance" : 10000,
                            "paymentId": 1,
                            "paidAmount": 5000,
                            "paymentType": "TOSS",
                            "paidAt": "2025-07-18T00:00:00"
                        }
                   \s""")
            )
    )
    ResponseEntity<PointResponse.CompletedCharge> completeCharge (
            @PathVariable Long userId,
            @RequestBody PointRequest.CompleteCharge request
    );

    @Operation(summary = "포인트 충전 결제 취소", description = "결제에 실패하여 충전 취소를 요청합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "포인트 충전 결제 취소 처리 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PointResponse.CanceledCharge.class),
                    examples = @ExampleObject(value = """
                        {\s
                            "userId": 1,
                            "paymentId": 1,
                            "canceledAt": "2025-07-18T00:00:00"
                        }
                   \s""")
            )
    )
    ResponseEntity<PointResponse.CanceledCharge> cancelCharge (
            @PathVariable Long userId,
            @RequestBody PointRequest.CancelCharge request
    );

}

