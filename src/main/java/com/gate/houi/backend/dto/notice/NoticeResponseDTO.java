package com.gate.houi.backend.dto.notice;

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
