package com.flow.fileextensioncheck.service;

import com.flow.fileextensioncheck.common.exception.DuplicateFileExtensionException;
import com.flow.fileextensioncheck.common.exception.ExtensionCountExceededException;
import com.flow.fileextensioncheck.controller.dto.response.ResponseFileExtension;
import com.flow.fileextensioncheck.entity.FileExtension;
import com.flow.fileextensioncheck.repository.FileExtensionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

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
@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultFileExtensionService implements FileExtensionService {
    private final FileExtensionRepository fileExtensionRepository;
    private final Lock lock = new ReentrantLock();
    @Value("${fixed-extension.list}")
    private List<String> allFixedExtensions;
    @Value("${max-extension-count}")
    private Integer MAX_EXTENSION_COUNT;

    @Override
    public void save(String fileExtension) {
        lock.lock();
        try {
            String lowerCaseExtension = fileExtension.toLowerCase();
            int numberOfCurrentExtensions = fileExtensionRepository.countByFixedFalse();
            boolean contains = allFixedExtensions.contains(lowerCaseExtension);
            if (!contains && numberOfCurrentExtensions >= MAX_EXTENSION_COUNT) {
                throw new ExtensionCountExceededException(MAX_EXTENSION_COUNT, "Exceeding the number of extensions");
            }
            fileExtensionRepository.save(FileExtension.from(lowerCaseExtension, contains));
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateFileExtensionException(fileExtension, "Existence of the same file extension");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public ResponseFileExtension findAllFileExtension() {
        Map<Boolean, List<String>> collect = fileExtensionRepository.findAll().stream().collect(Collectors.partitioningBy(FileExtension::isFixed, Collectors.mapping(FileExtension::getFileExtension, Collectors.toList())));
        return ResponseFileExtension.of(collect.get(true), collect.get(false));
    }

    @Override
    public void removeFileExtension(String fileExtension) {
        fileExtensionRepository.deleteByFileExtension(fileExtension.toLowerCase());
    }

    @Override
    public void removeAllFileExtension() {
        fileExtensionRepository.deleteAll();
    }


}
