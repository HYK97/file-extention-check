package com.flow.fileextensioncheck.service;

import com.flow.fileextensioncheck.repository.FileExtensionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.SecureRandom;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 동시성처리
 * <p>
 * - ReentrantLock 이용하기
 * 테스트 결과 -> lock 의 범위가 작고 빠름
 * 서버 확장시 추가적인 방안필요
 * 2000개 기준  5sec 882ms ~ 7s 2ms
 * <p>
 * - 비관적 Lock 이용하기
 * 테스트 결과 -> lock 의 범위가 너무넓음 (false 컬럼전체)
 * 서버 확장시에도 사용가능
 * 2000개 기준  15sec 992ms ~ 17s 102ms
 */
@SpringBootTest
class DefaultFileExtensionServiceConcurrencyTest {

    @Autowired
    FileExtensionRepository fileExtensionRepository;
    @Autowired
    DefaultFileExtensionService defaultFileExtensionService;
    ExecutorService executorService;
    CountDownLatch countDownLatch;

    @BeforeEach
    void beforeEach() {
        executorService = Executors.newFixedThreadPool(1000);
        countDownLatch = new CountDownLatch(1000);
    }

    @Test
    @DisplayName("동시성 테스트")
    void save() throws InterruptedException {
        //given
        SecureRandom random = new SecureRandom();

        // when
        IntStream.range(0, 1000).forEach(e -> executorService.submit(() -> {
                    try {
                        defaultFileExtensionService.save(String.valueOf(random.nextInt()));
                    } finally {
                        countDownLatch.countDown();
                    }
                }
        ));
        countDownLatch.await();

        //then
        assertThat(fileExtensionRepository.countByFixedFalse()).isEqualTo(200);

    }


}