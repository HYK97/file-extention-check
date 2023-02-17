package com.flow.fileextensioncheck.service;

import com.flow.fileextensioncheck.common.exception.DuplicateFileExtensionException;
import com.flow.fileextensioncheck.common.exception.ExtensionCountExceededException;
import com.flow.fileextensioncheck.controller.dto.response.ResponseFileExtension;
import com.flow.fileextensioncheck.entity.FileExtension;
import com.flow.fileextensioncheck.repository.FileExtensionRepository;
import com.navercorp.fixturemonkey.LabMonkey;
import com.navercorp.fixturemonkey.jackson.introspector.JacksonArbitraryIntrospector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;


@ExtendWith(MockitoExtension.class)
class DefaultFileExtensionServiceTest {

    @InjectMocks
    DefaultFileExtensionService defaultFileExtensionService;
    @Mock
    FileExtensionRepository fileExtensionRepository;

    LabMonkey labMonkey;

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(defaultFileExtensionService, "MAX_EXTENSION_COUNT", 200);
        ReflectionTestUtils.setField(defaultFileExtensionService, "allFixedExtensions", List.of("bat", "cmd", "com", "cpl", "exe", "scr", "js"));
        labMonkey = LabMonkey.labMonkeyBuilder()
                .objectIntrospector(JacksonArbitraryIntrospector.INSTANCE)
                .build();
    }

    @Test
    @DisplayName("save fileExtension 성공")
    void successfulSave() {
        //given
        String fileExtension = "cmd";
        given(fileExtensionRepository.countByFixedFalse())
                .willReturn(200);

        //when
        defaultFileExtensionService.save(fileExtension);

        //then
        then(fileExtensionRepository)
                .should()
                .save(any());
    }

    @Test
    @DisplayName("save fileExtension ExtensionCountExceededException 실패")
    void failBySaveWithExtensionCountExceededException() {
        //given
        String fileExtension = "sh";
        given(fileExtensionRepository.countByFixedFalse())
                .willReturn(200);

        //when
        assertThatThrownBy(() -> defaultFileExtensionService.save(fileExtension))
                .isInstanceOf(ExtensionCountExceededException.class);

        //then
        then(fileExtensionRepository)
                .should(never())
                .save(any());
    }

    @Test
    @DisplayName("save fileExtension DuplicateFileExtensionException 실패")
    void failSaveWithDuplicateFileExtensionException() {
        //given
        String fileExtension = "sh";
        given(fileExtensionRepository.countByFixedFalse())
                .willReturn(100);
        given(fileExtensionRepository.save(any()))
                .willThrow(DataIntegrityViolationException.class);

        //when
        assertThatThrownBy(() -> defaultFileExtensionService.save(fileExtension))
                .isInstanceOf(DuplicateFileExtensionException.class);

        //then
        then(fileExtensionRepository)
                .should()
                .save(any());
    }

    @Test
    @DisplayName("findAllFileExtension 성공")
    void successfulFindFileExtension() {
        //given
        given(fileExtensionRepository.findAll())
                .willReturn(List.of(labMonkey.giveMeBuilder(FileExtension.class)
                                .set("fixed", false)
                                .set("fileExtension", "sh").sample(),
                        labMonkey.giveMeBuilder(FileExtension.class)
                                .set("fixed", true)
                                .set("fileExtension", "cmd").sample(),
                        labMonkey.giveMeBuilder(FileExtension.class)
                                .set("fixed", true)
                                .set("fileExtension", "js").sample(),
                        labMonkey.giveMeBuilder(FileExtension.class)
                                .set("fixed", false)
                                .set("fileExtension", "jpg").sample(),
                        labMonkey.giveMeBuilder(FileExtension.class)
                                .set("fixed", false)
                                .set("fileExtension", "png").sample()));

        //when
        ResponseFileExtension allFileExtension = defaultFileExtensionService.findAllFileExtension();

        //then
        assertThat(allFileExtension.getCustomExtensions()).contains("sh", "jpg", "png");
        assertThat(allFileExtension.getFixedExtensions()).contains("cmd", "js");
    }

    @Test
    @DisplayName("removeFileExtension 성공")
    void successfulRemoveFileExtension() {
        //given
        String fileExtension = "sh";

        //when
        defaultFileExtensionService.removeFileExtension(fileExtension);

        //then
        then(fileExtensionRepository)
                .should()
                .deleteByFileExtension(any());
    }

    @Test
    @DisplayName("removeAllFileExtension 성공")
    void successfulRemoveAllFileExtension() {
        //given
        //when
        defaultFileExtensionService.removeAllFileExtension();

        //then
        then(fileExtensionRepository)
                .should()
                .deleteAll();
    }


}
