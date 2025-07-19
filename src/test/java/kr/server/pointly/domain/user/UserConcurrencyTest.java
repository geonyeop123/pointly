package kr.server.pointly.domain.user;

import kr.server.pointly.infrastructure.user.JpaUserRepository;
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
public class UserConcurrencyTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @AfterEach
    void tearDown() {
        jpaUserRepository.deleteAllInBatch();
    }

    @DisplayName("10명의 유저가 동시에 조회수 0인 유저를 조회하면 해당 유저의 조회수는 10이다.")
    @Test
    void viewCount() throws InterruptedException {
        // given
        User user = jpaUserRepository.save(User.create("이건엽"));

        AtomicInteger successCnt = new AtomicInteger();
        Long userId = user.getId();

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try{
                    userService.addView(new UserCommand.AddView(userId));
                    successCnt.getAndIncrement();
                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        assertThat(successCnt.get()).isEqualTo(threadCount);
        jpaUserRepository.findById(userId)
                .ifPresent(finalUser -> assertThat(finalUser.getViewCount()).isEqualTo(threadCount));
    }

}
