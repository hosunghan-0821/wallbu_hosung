package kr.co.hanhosung.wallbu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.co.hanhosung.wallbu.domain.UserRole;
import kr.co.hanhosung.wallbu.dto.EnrollDto;
import kr.co.hanhosung.wallbu.dto.LectureDto;
import kr.co.hanhosung.wallbu.dto.UserDto;
import kr.co.hanhosung.wallbu.global.annotation.LoginUserArgumentResolver;
import kr.co.hanhosung.wallbu.global.filter.JwtAuthFilter;
import kr.co.hanhosung.wallbu.global.util.token.ITokenManager;
import kr.co.hanhosung.wallbu.global.util.token.JwtDecoder;
import kr.co.hanhosung.wallbu.global.util.token.JwtTokenProvider;
import kr.co.hanhosung.wallbu.service.AuthService;
import kr.co.hanhosung.wallbu.service.LectureService;
import kr.co.hanhosung.wallbu.service.enumerate.SortingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LectureControllerTest {

    @InjectMocks
    LectureController lectureController;

    @Mock
    private LectureService lectureService;

    private ITokenManager iTokenManager = new JwtTokenProvider("wallbuhosung", new JwtDecoder("wallbuhosung"));

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    private JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(iTokenManager);

    private String accessToken;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(lectureController)
                .addFilter(jwtAuthFilter)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver(), new LoginUserArgumentResolver(iTokenManager)).build();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        accessToken = iTokenManager.createAccessToken(1L);

    }


    @Test
    @DisplayName("[성공] : 강좌 등록하기 (강사)")
    void registerLecture() throws Exception {
        //given
        LectureDto lectureDto = lectureDtoBuilder().build();
        Mockito.doNothing().when(lectureService).registerLecture(any(LectureDto.class), any(Long.class));
        String request = objectMapper.writeValueAsString(lectureDto);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/lectures")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)

        );
        //then
        resultActions
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("[실패] : 강좌 등록하기 (강사) - 토큰 없음")
    void registerLectureFail() throws Exception {
        //given
        LectureDto lectureDto = lectureDtoBuilder().build();
        String request = objectMapper.writeValueAsString(lectureDto);
        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/lectures")

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)

        );
        //then
        resultActions
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[성공] : 강좌 등록하기 (유저)")
    void enrollmentLectureFail() throws Exception {
        //given
        LectureDto lectureDto = lectureDtoBuilder().id(1L).build();
        Mockito.doNothing().when(lectureService).enrollmentLecture(any(List.class), any(Long.class));
        EnrollDto enrollDto = new EnrollDto(new ArrayList<>());
        enrollDto.getLectureList().add(lectureDto);
        String request = objectMapper.writeValueAsString(enrollDto);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/lectures/enroll")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)

        );

        //then
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("[실패] : 강좌 등록하기 (유저) - 토큰 없음")
    void enrollmentLecture() throws Exception {
        //given
        LectureDto lectureDto = lectureDtoBuilder().id(1L).build();
        EnrollDto enrollDto = new EnrollDto(new ArrayList<>());
        enrollDto.getLectureList().add(lectureDto);
        String request = objectMapper.writeValueAsString(enrollDto);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/lectures/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)

        );

        //then
        resultActions
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[성공] : 강좌 조회")
    void getLectureList() throws Exception {

        //given
        List<LectureDto> lectureDtoList = new ArrayList<>(Arrays.asList(lectureDtoBuilder().build()));
        Mockito.doReturn(new PageImpl<>(lectureDtoList)).when(lectureService).getLectureList(any(Pageable.class), any(SortingType.class));

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/lectures")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)

        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));

    }

    @Test
    @DisplayName("[실패] : 강좌 조회 - 토큰 없음")
    void getLectureListFail() throws Exception {

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/lectures")
                        .contentType(MediaType.APPLICATION_JSON)

        );

        //then
        resultActions
                .andExpect(status().isUnauthorized());

    }


    private LectureDto.LectureDtoBuilder lectureDtoBuilder() {

        return LectureDto.builder()
                .title("하루에 1%씩 부자되기")
                .maxStudentCount(10L)
                .price(1_000L);

    }
}