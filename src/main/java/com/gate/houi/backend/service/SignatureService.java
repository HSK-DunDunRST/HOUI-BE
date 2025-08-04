package com.gate.houi.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gate.houi.backend.data.entityType.SignatureEntity;
import com.gate.houi.backend.data.entityType.StudentEntity;
import com.gate.houi.backend.data.enumType.ErrorType;
import com.gate.houi.backend.exception.BaseException;
import com.gate.houi.backend.repository.SignatureRepository;
import com.gate.houi.backend.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignatureService {

    private final SignatureRepository signatureRepository;
    private final StudentRepository studentRepository;

    // 서명 이미지가 저장될 디렉토리
    private final String uploadDir = "SignatureUploadImage";
    
    // 허용되는 파일 확장자 (보안)
    private final List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png");
    
    // 최대 파일 크기 (5MB)
    private final long maxFileSize = 5 * 1024 * 1024;

    @Value("${app.base-url:http://9oormthonuniv.iptime.org:2100}")
    private String baseUrl;

    /**
     * 학생의 서명 이미지를 업로드하고 DB에 저장
     */
    @Transactional
    public String uploadSignature(UUID studentUuid, MultipartFile file) throws IOException {
        // 1. 파일 유효성 검증
        validateFile(file);
        
        // 2. studentUuid에 해당하는 학생 엔티티 조회
        StudentEntity student = studentRepository.findByStudentUuid(studentUuid)
                .orElseThrow(() -> new RuntimeException("Student not found with uuid " + studentUuid));

        // 3. 파일 저장 경로 설정 및 디렉토리 생성
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 4. 기존 서명 이미지가 있다면 삭제
        deleteExistingSignature(studentUuid);

        // 5. 안전한 파일명 생성
        String fileExtension = getSecureFileExtension(file.getOriginalFilename());
        String uniqueFileName = java.util.UUID.randomUUID().toString() + fileExtension;

        // 6. 파일 저장
        Path targetLocation = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // 7. 웹 접근 가능한 URL 생성
        String fileUrl = baseUrl + "/SignatureUploadImage/" + uniqueFileName;

        // 8. DB에 서명 정보 저장
        SignatureEntity signature = SignatureEntity.builder()
                .studentUuid(student.getStudentUuid())
                .signatureUrl(fileUrl)
                .build();
        signatureRepository.save(signature);

        return fileUrl;
    }

    /**
     * 특정 학생의 서명 이미지 조회
     */
    public String getSignatureByStudentUuid(UUID studentUuid) {
        SignatureEntity signature = signatureRepository.findByStudentUuid(studentUuid);
        return signature != null ? signature.getSignatureUrl() : null;
    }

    /**
     * 기존 서명 이미지 삭제
     */
    private void deleteExistingSignature(UUID studentUuid) throws IOException {
        SignatureEntity existingSignature = signatureRepository.findByStudentUuid(studentUuid);
        if (existingSignature != null) {
            // 파일 시스템에서 기존 파일 삭제
            String fileUrl = existingSignature.getSignatureUrl();
            String fileName = extractSecureFileName(fileUrl);
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path filePath = uploadPath.resolve(fileName);
            Files.deleteIfExists(filePath);
            
            // DB에서 기존 서명 레코드 삭제
            signatureRepository.delete(existingSignature);
        }
    }

    /**
     * 서명 이미지 삭제
     */
    public void deleteSignature(UUID studentUuid) throws IOException {
        deleteExistingSignature(studentUuid);
    }

    /**
     * 파일 유효성 검증
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BaseException(ErrorType.INVALID_FILE_NAME.getErrorCode(), ErrorType.INVALID_FILE_NAME.getErrorMessage());
        }
        
        if (file.getSize() > maxFileSize) {
            throw new BaseException(ErrorType.INVALID_FILE_SIZE.getErrorCode(), ErrorType.INVALID_FILE_SIZE.getErrorMessage());
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new BaseException(ErrorType.INVALID_FILE_NAME.getErrorCode(), ErrorType.INVALID_FILE_NAME.getErrorMessage());
        }
        
        String fileExtension = getSecureFileExtension(originalFilename);
        if (!allowedExtensions.contains(fileExtension.toLowerCase())) {
            throw new BaseException(ErrorType.INVALID_FILE_FORMAT.getErrorCode(), ErrorType.INVALID_FILE_FORMAT.getErrorMessage());
        }
    }

    /**
     * 안전한 파일 확장자 추출
     */
    private String getSecureFileExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return "";
        }
        
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        // 확장자 길이 제한 (보안)
        if (extension.length() > 10) {
            return "";
        }
        
        return extension;
    }

    /**
     * URL에서 안전한 파일명 추출
     */
    private String extractSecureFileName(String fileUrl) {
        if (fileUrl == null || fileUrl.trim().isEmpty()) {
            throw new RuntimeException("Invalid file URL");
        }
        
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        
        // 파일명 검증 (경로 조작 방지)
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            throw new RuntimeException("Invalid file name detected");
        }
        
        return fileName;
    }
}
