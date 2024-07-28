package com.fledge.fledgeserver.file;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.fledge.fledgeserver.file.dto.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

import java.text.SimpleDateFormat;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${cloud.s3.bucket}")
    private String bucket;

    @Value("${cloud.s3.expTime}")
    private Long expTime;


    private final AmazonS3 amazonS3;

    public PresignedUrlResponse getPresignedUrl(String prefix, String originalFileName) {
        String filePath = createPath(prefix, originalFileName);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedUrlRequest(bucket, filePath, HttpMethod.PUT);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        return new PresignedUrlResponse(url.toString(), filePath);
    }

    public String getFileUrl(String filePath) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedUrlRequest(bucket, filePath, HttpMethod.GET);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();
    }

    private GeneratePresignedUrlRequest getGeneratePresignedUrlRequest(String bucket, String fileName, HttpMethod method) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, fileName)
                .withMethod(method)
                .withExpiration(getPresignedUrlExpiration());

        return generatePresignedUrlRequest;
    }

    private Date getPresignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += expTime;
        expiration.setTime(expTimeMillis);

        return expiration;
    }

    private String createFileId() {
        return UUID.randomUUID().toString();
    }

    private String createPath(String prefix, String fileName) {
        String fileId = createFileId();
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return String.format("%s/%s-%s-%s", prefix, timestamp, fileId, fileName);
    }
}
