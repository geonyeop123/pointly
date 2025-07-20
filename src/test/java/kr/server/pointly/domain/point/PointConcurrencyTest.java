package kr.server.pointly.domain.point;

import kr.server.pointly.infrastructure.point.JpaPointHistoryRepository;
import kr.server.pointly.infrastructure.point.JpaPointRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PointConcurrencyTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private JpaPointRepository jpaPointRepository;

    @Autowired
    private JpaPointHistoryRepository jpaPointHistoryRepository;

    @AfterEach
    void tearDown() {
        jpaPointRepository.deleteAllInBatch();
        jpaPointHistoryRepository.deleteAllInBatch();
    }

    @DisplayName("한 유저가 포인트 충전을 동시에 10회 시도할 시 성공한 만큼만 포인트가 충전된다.")
    @Test
    void charge() throws InterruptedException {
        // given
        Point point = jpaPointRepository.save(Point.create(1L, 0L));

        AtomicInteger successCnt = new AtomicInteger();
        Long userId = point.getUserId();
        Long amount = 1000L;

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try{
                    pointService.charge(new PointCommand.Charge(userId, amount));
                    successCnt.getAndIncrement();
                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //then
        assertThat(successCnt.get()).isNotZero();
        jpaPointRepository.findById(userId)
                .ifPresent(finalPoint -> assertThat(finalPoint.getBalance()).isEqualTo(successCnt.get() * amount));
    }
}
