package kr.co.hanhosung.wallbu.controller;


import kr.co.hanhosung.wallbu.dto.EnrollDto;
import kr.co.hanhosung.wallbu.dto.LectureDto;
import kr.co.hanhosung.wallbu.global.annotation.Login;
import kr.co.hanhosung.wallbu.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static kr.co.hanhosung.wallbu.global.validation.ValidationMarker.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class LectureController {

    private final LectureService lectureService;


    @PostMapping("/lectures")
    public ResponseEntity<Boolean> registerLecture(@Validated({OnRegisterLecture.class}) @RequestBody LectureDto lectureDto, @Login Long userId) {
        assert (lectureDto != null);
        assert (userId != null);

        lectureService.registerLecture(lectureDto, userId);
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.CREATED);
    }

    @PostMapping("/lectures/enroll")
    public ResponseEntity<Boolean> enrollmentLecture(@Validated({OnEnrollLecture.class}) @RequestBody EnrollDto enrollDto, @Login Long userId) {
        assert (enrollDto != null);
        assert (userId != null);

        lectureService.enrollmentLecture(enrollDto.getLectureList(), userId);

        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }


}
