package kr.co.hanhosung.wallbu.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.hanhosung.wallbu.dto.EnrollDto;
import kr.co.hanhosung.wallbu.dto.LectureDto;
import kr.co.hanhosung.wallbu.global.annotation.Login;
import kr.co.hanhosung.wallbu.service.LectureService;
import kr.co.hanhosung.wallbu.service.enumerate.SortingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static kr.co.hanhosung.wallbu.global.validation.ValidationMarker.*;

@Tag(name = "강의 컨트롤러", description = "강의 도메인 관련 API들이 존재합니다. ( 강좌등록(강사), 강좌신청(수강생,강사), 강좌조회)")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class LectureController {

    private final LectureService lectureService;


    @Operation(summary = "강좌 등록 API (강사)")
    @ApiResponse(responseCode = "201", description = "강좌 등록 성공")
    @ApiResponse(responseCode = "401", description = "유저 토큰 유효하지 않음")
    @ApiResponse(responseCode = "400", description = "유저 ROLE 오류")
    @PostMapping("/lectures")
    public ResponseEntity<Boolean> registerLecture(@Validated({OnRegisterLecture.class}) @RequestBody LectureDto lectureDto, @Parameter(hidden = true) @Login Long userId) {
        assert (lectureDto != null);
        assert (userId != null);

        lectureService.registerLecture(lectureDto, userId);
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.CREATED);
    }

    @Operation(summary = "강의 신청 API (강사,수강생)")
    @ApiResponse(responseCode = "200", description = "강의 등록 성공")
    @ApiResponse(responseCode = "401", description = "유저 토큰 유효하지 않음")
    @ApiResponse(responseCode = "400", description = "강좌 만원 or 중복 수강신청 or 강좌 신청 유효성 검사 실패")
    @PostMapping("/lectures/enroll")
    public ResponseEntity<Boolean> enrollmentLecture(@Validated({OnEnrollLecture.class}) @RequestBody EnrollDto enrollDto, @Parameter(hidden = true) @Login Long userId) {
        assert (enrollDto != null);
        assert (userId != null);

        lectureService.enrollmentLecture(enrollDto.getLectureList(), userId);

        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }

    @Operation(summary = "강좌 조회 API ")
    @ApiResponse(responseCode = "200", description = "강좌 조회")
    @ApiResponse(responseCode = "401", description = "유저 토큰 유효하지 않음")
    @GetMapping("/lectures")
    public ResponseEntity<Page<LectureDto>> getLectureList(@Parameter(hidden = true) Pageable pageable, @RequestParam(value = "sort", defaultValue = "RECENTLY_REGISTERED") SortingType sortingType) {

        Page<LectureDto> lectureList = lectureService.getLectureList(pageable, sortingType);
        return new ResponseEntity<>(lectureList, HttpStatus.OK);
    }

}
