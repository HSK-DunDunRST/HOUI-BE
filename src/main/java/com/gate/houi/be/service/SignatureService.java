package com.gate.houi.be.service;

import com.gate.houi.be.dto.res.SignatureResDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SignatureService {

    SignatureResDto uploadSignature(Long userId, MultipartFile file) throws IOException;
}
