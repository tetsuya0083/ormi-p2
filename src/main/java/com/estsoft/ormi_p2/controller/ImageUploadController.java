package com.estsoft.ormi_p2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ImageUploadController {
    private static final String UPLOAD_DIR = "/uploads/posts";

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/image/upload")
    public Map<String, String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueName = UUID.randomUUID() + extension;

        File savePath = new File(uploadDir, uniqueName);
        file.transferTo(savePath);

        String imageUrl = "/uploads/posts/" + uniqueName;

        Map<String, String> response = new HashMap<>();
        response.put("imageUrl", imageUrl);
        return response;
    }
}