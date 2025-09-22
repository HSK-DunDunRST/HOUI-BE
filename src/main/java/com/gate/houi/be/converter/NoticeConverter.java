package com.gate.houi.be.converter;

import com.gate.houi.be.dto.res.NoticeResDto;
import com.gate.houi.be.entity.NoticeEntity;

public class NoticeConverter {
    public static NoticeResDto toLast(NoticeEntity noticeEntity) {
        return NoticeResDto.builder()
                .noticeId(noticeEntity.getId())
                .noticeTitle(noticeEntity.getNoticeTitle())
                .noticeContent(noticeEntity.getNoticeContent())
                // 생성일자를 문자열로 변환하여 저장
                .noticeCreatedAt(noticeEntity.getCreatedAt().toString())
                .build();
    }



}
