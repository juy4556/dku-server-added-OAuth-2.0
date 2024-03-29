package com.dku.council.domain.user.model.dto.response;

import com.dku.council.domain.user.model.DkuUserInfo;
import lombok.Getter;

@Getter
public class ResponseScrappedStudentInfoDto {

    private final String studentName;
    private final String studentId;
    private final String age;
    private final String gender;
    private final String major;

    public ResponseScrappedStudentInfoDto(DkuUserInfo info) {
        this.studentName = info.getStudentName();
        this.studentId = info.getStudentId();
        this.age = info.getAge();
        this.gender = info.getGender();
        this.major = info.getMajorName();
    }
}
