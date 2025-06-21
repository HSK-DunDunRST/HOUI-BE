package com.gate.houi.hoseoHoui.dto.notice;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoticeResponseDTO {
    private Long id;
    private String noticeTitle;
    private String noticeContent;
    private String noticeCreatedAt;
}
