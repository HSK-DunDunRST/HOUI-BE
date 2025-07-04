package com.gate.houi.hoseoHoui.domain.dtoSet;

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
