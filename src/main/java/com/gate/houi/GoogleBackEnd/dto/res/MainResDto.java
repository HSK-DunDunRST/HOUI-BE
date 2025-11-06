package com.gate.houi.GoogleBackEnd.dto.res;

import lombok.*;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainResDto {
    private NoticeSummary noticeSummary;
    private WaitInformation waitInformation;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoticeSummary{
        private String noticeTitle;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WaitInformation{
        private int waitCount;
    }

}
