package com.gate.houi.hoseoHoui.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeRequestDTO {
    private String noticeTitle;
    private String noticeContent;
}
