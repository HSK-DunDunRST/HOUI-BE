package com.gate.houi.be.service;

import com.gate.houi.be.apiPayload.code.exception.BaseException;
import com.gate.houi.be.apiPayload.code.status.ErrorType;
import com.gate.houi.be.converter.SignatureConverter;
import com.gate.houi.be.dto.res.SignatureResDto;
import com.gate.houi.be.entity.SignatureEntity;
import com.gate.houi.be.entity.UserEntity;
import com.gate.houi.be.repository.SignatureRepository;
import com.gate.houi.be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
@RequiredArgsConstructor
public class SignatureServiceImpl implements SignatureService {

    private final UserRepository userRepository;
    private final SignatureRepository signatureRepository;

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    @Value("${app.file.base-url}")
    private String fileUrl;

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

        // studentId_signature 형식
        String uniqueFileName = userEntity.getStudentId() + "_signature" + fileExtension;

        Path targetLocation = uploadPath.resolve(uniqueFileName);
        // 파일이 이미 존재하면 덮어쓰기
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        String upLoadFileUrl = fileUrl + uniqueFileName;

        SignatureEntity signatureEntity = SignatureEntity.builder()
                .studentId(userEntity.getStudentId())
                .userUuid(userEntity.getUserUuid())
                .signatureUrl(upLoadFileUrl)
                .build();

        SignatureEntity saved = signatureRepository.save(signatureEntity);

        return SignatureConverter.toDto(saved);
    }
}