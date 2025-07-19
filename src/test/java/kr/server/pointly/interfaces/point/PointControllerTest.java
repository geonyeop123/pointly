package kr.server.pointly.interfaces.point;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointController.class)
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("포인트 충전을 위해 결제 요청을 하면 결제 예정 정보를 받는다.")
    @Test
    void requestCharge() throws Exception {
        // given
        Long userId = 1L;
        PointRequest.RequestCharge request = new PointRequest.RequestCharge( 1000L, "TOSS");

        // when then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/{userId}/points/charge", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.paymentId").value(1L))
                .andExpect(jsonPath("$.amount").value(request.amount()))
                .andExpect(jsonPath("$.paymentType").value(request.paymentType()))
                .andExpect(jsonPath("$.paidRequestAt").value("2025-07-18T00:00:00"));
    }

    @DisplayName("포인트 충전을 위한 결제 완료 요청 시 충전된 포인트 정보와 결제 정보를 받는다.")
    @Test
    void completeCharge() throws Exception{
        // given
        Long userId = 1L;
        PointRequest.CompleteCharge request = new PointRequest.CompleteCharge( 1L, 1000L, "TOSS", "keykeykey");

        // when then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/{userId}/points/charge/approve", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.balance").value(10000L))
                .andExpect(jsonPath("$.paymentId").value(1L))
                .andExpect(jsonPath("$.paidAmount").value(5000L))
                .andExpect(jsonPath("$.paymentType").value(request.paymentType()))
                .andExpect(jsonPath("$.paidAt").value("2025-07-18T00:00:00"));
    }

    @DisplayName("포인트 충전 취소 요청 시 취소된 결제 정보를 받는다.")
    @Test
    void cancelCharge() throws Exception{
        // given

        Long userId = 1L;
        PointRequest.CancelCharge request = new PointRequest.CancelCharge(1L);

        // when then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/{userId}/points/charge/cancel", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.paymentId").value(1L))
                .andExpect(jsonPath("$.canceledAt").value("2025-07-18T00:00:00"));
    }
}