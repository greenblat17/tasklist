package com.greenblat.tasklist.repository;


import java.io.InputStream;

public interface MinioRepository {

    void saveFile(String filename, InputStream inputStream);

    void createBucket();

    boolean existsBucket();

}
