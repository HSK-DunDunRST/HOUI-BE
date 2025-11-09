package com.gate.houi.GoogleBackEnd.service;

import com.gate.houi.GoogleBackEnd.dto.res.SignatureResDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SignatureService {

    SignatureResDto uploadSignature(Long userId, MultipartFile file) throws IOException;
}
