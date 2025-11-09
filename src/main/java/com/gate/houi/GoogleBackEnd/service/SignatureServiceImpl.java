package com.gate.houi.GoogleBackEnd.service;

import com.gate.houi.GoogleBackEnd.apiPayload.code.exception.BaseException;
import com.gate.houi.GoogleBackEnd.apiPayload.code.status.ErrorType;
import com.gate.houi.GoogleBackEnd.converter.SignatureConverter;
import com.gate.houi.GoogleBackEnd.dto.res.SignatureResDto;
import com.gate.houi.GoogleBackEnd.entity.SignatureEntity;
import com.gate.houi.GoogleBackEnd.entity.UserEntity;
import com.gate.houi.GoogleBackEnd.repository.SignatureRepository;
import com.gate.houi.GoogleBackEnd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class SignatureServiceImpl implements SignatureService {

    private final UserRepository userRepository;
    private final SignatureRepository signatureRepository;

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    @Value("${app.file.base-url}")
    private String fileUrl;

    @Override
    public SignatureResDto uploadSignature(Long userId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new BaseException(ErrorType.SIGNATURE_INTERNAL_SERVER_ERROR);
        }

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorType.USER_NOT_FOUND));

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 파일 확장자 추출
        String fileExtension = "";
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        }

        // 현재 시각 (HHmmss)
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
        String timestamp = now.format(formatter);

        // 기존 서명 엔티티 조회 (없으면 새로 생성)
        SignatureEntity signatureEntity = signatureRepository.findByStudentId(userEntity.getStudentId())
                .orElse(SignatureEntity.builder()
                        .studentId(userEntity.getStudentId())
                        .userUuid(userEntity.getUserUuid())
                        .build());

        // 기존 파일이 있으면 안전하게 삭제
        if (signatureEntity.getSignatureUrl() != null) {
            try {
                // URL에서 파일명만 추출
                String existingFileName = Paths.get(signatureEntity.getSignatureUrl().replace(fileUrl, "")).getFileName().toString();
                Path existingFile = uploadPath.resolve(existingFileName).normalize();

                // uploadPath 밖으로 벗어나지 않았는지 확인
                if (existingFile.startsWith(uploadPath) && Files.exists(existingFile)) {
                    Files.delete(existingFile);
                }
            } catch (Exception exception) {
                // 삭제 실패 시 로그만 남기고 진행
                System.err.println("기존 서명 파일 삭제 실패: " + exception.getMessage());
            }
        }

        // 파일명에 타임스탬프 포함
        String uniqueFileName = "Signature_" + userEntity.getStudentId() + "_" + timestamp + fileExtension;
        Path targetLocation = uploadPath.resolve(uniqueFileName).normalize();

        // 파일 저장
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // 실제 접근 가능한 파일 URL
        String uploadFileUrl = fileUrl + uniqueFileName;

        // URL 갱신 (updatedAt은 자동 반영됨)
        signatureEntity.setSignatureUrl(uploadFileUrl);

        SignatureEntity saved = signatureRepository.save(signatureEntity);

        return SignatureConverter.toDto(saved);
    }
}