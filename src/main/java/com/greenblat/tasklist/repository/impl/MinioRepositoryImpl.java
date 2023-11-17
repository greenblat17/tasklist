package com.greenblat.tasklist.repository.impl;

import com.greenblat.tasklist.domain.exception.BucketException;
import com.greenblat.tasklist.domain.exception.ImageUploadException;
import com.greenblat.tasklist.repository.MinioRepository;
import com.greenblat.tasklist.service.props.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Repository
@RequiredArgsConstructor
public class MinioRepositoryImpl implements MinioRepository {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Override
    public void saveFile(String filename, InputStream inputStream) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .stream(inputStream, inputStream.available(), -1)
                            .bucket(minioProperties.getBucket())
                            .object(filename)
                            .build()
            );
            inputStream.close();
        } catch (Exception e) {
            throw new ImageUploadException("Image upload failed" + e.getMessage());
        }
    }

    @Override
    public void createBucket() {
        try {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .build()
            );
        } catch (ServerException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException |
                 XmlParserException e) {
            throw new BucketException("Create bucket failed: " + e.getMessage());
        }
    }

    @Override
    public boolean existsBucket() {
        try {
            return minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .build()
            );
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new BucketException("Exists bucket failed: " + e.getMessage());
        }
    }
}
