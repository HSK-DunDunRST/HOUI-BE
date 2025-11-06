package com.gate.houi.GoogleBackEnd.dto.res;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResDto {
    private Long noticeId;
    private String noticeTitle;
    private String noticeContent;
    private String noticeCreatedAt;
}
