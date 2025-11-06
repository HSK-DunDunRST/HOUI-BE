package com.gate.houi.GoogleBackEnd.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeReqDto {
    private String noticeTitle;
    private String noticeContent;
}
