package kr.server.pointly.interfaces.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("회원 목록 조회 요청 시 페이징 처리 된 회원 목록을 받는다.")
    @Test
    void findAll() throws Exception {
        //given
        UserRequest.FindAll request =
                new UserRequest.FindAll(1, 10, null);
        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .queryParam("page", String.valueOf(request.page()))
                        .queryParam("size", String.valueOf(request.size())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.page").value(request.page()))
                .andExpect(jsonPath("$.size").value(request.size()))
                .andExpect(jsonPath("$.totalCount").value(4))
                .andExpect(jsonPath("$.totalPages").value(1))
        ;
    }

    @DisplayName("userId와 함께 조회수 증가를 요청하면 해당되는 유저의 증가된 조회수 정보를 받는다.")
    @Test
    void addView() throws Exception {
        // given
        UserResponse.AddView response = new UserResponse.AddView(1L, 5L, LocalDateTime.of(2025, 7, 18, 0, 0, 0));

        UserRequest.FindAll request =
                new UserRequest.FindAll(1, 10, null);


        // when // then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/{userId}/view", 1L))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(response.userId()))
                    .andExpect(jsonPath("$.viewCount").value(response.viewCount()))
                    .andExpect(jsonPath("$.modifiedAt").value("2025-07-18T00:00:00"))
        ;
    }
}