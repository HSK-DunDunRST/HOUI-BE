package com.gate.houi.be.converter;

import com.gate.houi.be.dto.res.MainResDto;
import com.gate.houi.be.dto.res.NoticeResDto;
import com.gate.houi.be.entity.NoticeEntity;

public class NoticeConverter {
    public static MainResDto.NoticeSummary toLast(NoticeEntity noticeEntity) {
        return MainResDto.NoticeSummary.builder()
                .noticeTitle(noticeEntity.getNoticeTitle())
                .build();
    }

    public static NoticeResDto toMore(NoticeEntity noticeEntity) {
        return NoticeResDto.builder()
                .noticeId(noticeEntity.getId())
                .noticeTitle(noticeEntity.getNoticeTitle())
                .noticeContent(noticeEntity.getNoticeContent())
                .noticeCreatedAt(noticeEntity.getCreatedAt().toString())
                .build();
    }
}