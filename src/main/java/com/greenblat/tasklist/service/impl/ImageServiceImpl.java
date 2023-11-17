package com.greenblat.tasklist.service.impl;

import com.greenblat.tasklist.domain.exception.ImageUploadException;
import com.greenblat.tasklist.domain.task.TaskImage;
import com.greenblat.tasklist.repository.MinioRepository;
import com.greenblat.tasklist.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final MinioRepository minioRepository;

    public String upload(TaskImage image) {
        createBucket();

        var file = image.getFile();
        checkEmptyFile(file);

        var fileName = generateFileName(file);
        var inputStream = extractInputStream(file);
        minioRepository.saveFile(fileName, inputStream);

        return fileName;
    }

    private void checkEmptyFile(MultipartFile file) {
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new ImageUploadException("File must have the name");
        }
    }


    private InputStream extractInputStream(MultipartFile file) {
        try {
            return file.getInputStream();
        } catch (IOException e) {
            throw new ImageUploadException("File upload failed: " + e.getMessage());
        }
    }

    private void createBucket() {
        var found = minioRepository.existsBucket();

        if (!found) {
            minioRepository.createBucket();
        }
    }

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID() +  "." + getExtension(file);
    }

    private String getExtension(MultipartFile file) {
        return getExtensionFromFileName(Objects.requireNonNull(file.getOriginalFilename()));
    }

    public String getExtensionFromFileName(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

}
