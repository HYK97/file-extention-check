package com.flow.fileextensioncheck.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flow.fileextensioncheck.common.exception.DuplicateFileExtensionException;
import com.flow.fileextensioncheck.common.exception.ExtensionCountExceededException;
import com.flow.fileextensioncheck.controller.dto.response.ResponseFileExtension;
import com.flow.fileextensioncheck.service.FileExtensionService;
import com.navercorp.fixturemonkey.LabMonkey;
import com.navercorp.fixturemonkey.jackson.introspector.JacksonArbitraryIntrospector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileExtensionAPIController.class)
@ExtendWith(MockitoExtension.class)
class FileExtensionAPIControllerTest {

    final String basicApiUrl = "/api/extension";
    @MockBean
    FileExtensionService fileExtensionService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    LabMonkey labMonkey;

    private String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    @BeforeEach
    public void init() {
        labMonkey = LabMonkey.labMonkeyBuilder()
                .objectIntrospector(JacksonArbitraryIntrospector.INSTANCE)
                .build();
    }

    @Test
    @DisplayName("addFileExtension 성공")
    void successfulAddFileExtension() throws Exception {
        //given
        //when
        mockMvc.perform(post(basicApiUrl)
                        .param("fileExtension", "sh"))
                .andDo(print())
                .andExpect(status().isOk());

        //then
        then(fileExtensionService)
                .should()
                .save(any());
    }

    @Test
    @DisplayName("addFileExtension ExtensionCountExceededException 실패")
    void failAddFileExtensionWithExtensionCountExceededException() throws Exception {
        //given
        willThrow(ExtensionCountExceededException.class)
                .given(fileExtensionService)
                .save(any());


        //when
        mockMvc.perform(post(basicApiUrl)
                        .param("fileExtension", "sh"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        //then
        then(fileExtensionService)
                .should()
                .save(any());
    }

    @Test
    @DisplayName("addFileExtension DuplicateFileExtensionException 실패")
    void failAddFileExtensionWithDuplicateFileExtensionException() throws Exception {
        //given
        willThrow(DuplicateFileExtensionException.class)
                .given(fileExtensionService)
                .save(any());

        //when
        mockMvc.perform(post(basicApiUrl)
                        .param("fileExtension", "sh"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        //then
        then(fileExtensionService)
                .should()
                .save(any());
    }


    @Test
    @DisplayName("initData 성공")
    void successfulInitData() throws Exception {
        //given
        ResponseFileExtension fixture = labMonkey.giveMeOne(ResponseFileExtension.class);
        given(fileExtensionService.findAllFileExtension())
                .willReturn(fixture);

        //when
        mockMvc.perform(get(basicApiUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fixedExtensions").value(fixture.getFixedExtensions()))
                .andExpect(jsonPath("$.customExtensions").value(fixture.getCustomExtensions()));
    }

    @Test
    @DisplayName("removeFileExtension 성공")
    void successfulRemoveFileExtension() throws Exception {
        //given
        //when
        mockMvc.perform(delete(basicApiUrl)
                        .param("fileExtension", "sh"))
                .andDo(print())
                .andExpect(status().isOk());

        //then
        then(fileExtensionService)
                .should()
                .removeFileExtension(any());
    }

    @Test
    @DisplayName("removeAllFileExtension 성공")
    void successfulRemoveAllFileExtension() throws Exception {
        //given
        //when
        mockMvc.perform(delete(basicApiUrl + "/all"))
                .andDo(print())
                .andExpect(status().isOk());

        //then
        then(fileExtensionService)
                .should()
                .removeAllFileExtension();
    }
}