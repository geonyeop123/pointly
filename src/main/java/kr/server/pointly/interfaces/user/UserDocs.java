package kr.server.pointly.interfaces.user;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.server.pointly.interfaces.common.PageResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "user", description = "user API")
public interface UserDocs {

    @Operation(summary = "회원 프로필 목록 조회", description = "회원 프로필 목록을 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "회원 프로필 목록 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PageResponse.class),
                    examples = @ExampleObject(value = """
                        {
                            "content": [
                                {
                                    "userId": 1,
                                    "name": "이건엽",
                                    "viewCount": 5,
                                    "createAt": "2024-07-14"
                                },
                                {
                                    "userId": 2,
                                    "name": "홍길동",
                                    "viewCount": 10,
                                    "createAt": "2024-07-13"
                                },
                                {
                                    "userId": 3,
                                    "name": "정지훈",
                                    "viewCount": 20,
                                    "createAt": "2024-07-12"
                                },
                                {
                                    "userId": 4,
                                    "name": "박재혁",
                                    "viewCount": 30,
                                    "createAt": "2024-07-11"
                                }
                            ],
                            "page": 1,
                            "size": 10,
                            "totalCount": 4,
                            "totalPage": 1
                        }
                    """)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 정렬 요청",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = """
                {
                  "code": 400,
                  "message": "제공하지 않는 정렬 방식입니다."
                }
                """)
            )
    )
    ResponseEntity<PageResponse<UserResponse>> findAll (
            @ParameterObject UserRequest.FindAll request
    );

    @Operation(summary = "회원 프로필 조회수 증가", description = "회원 프로필의 조회수를 증가시킵니다.")
    @ApiResponse(
            responseCode = "200",
            description = "회원 프로필 조회수 증가 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PageResponse.class),
                    examples = @ExampleObject(value = """
                        {\s
                            "userId": 1,
                            "viewCount": 5,
                            "modifiedAt": "2025-07-18T00:00:00"
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
    ResponseEntity<UserResponse.AddView> addView (
            @PathVariable Long userId,
            @ParameterObject UserRequest.AddView request
    );
}
