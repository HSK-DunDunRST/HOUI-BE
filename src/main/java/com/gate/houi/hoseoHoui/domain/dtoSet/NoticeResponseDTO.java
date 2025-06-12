package com.gate.houi.hoseoHoui.domain.dtoSet;

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
