package com.fledge.fledgeserver.common.aop;

import com.fledge.fledgeserver.common.Interface.PresignedUrlApplicable;
import com.fledge.fledgeserver.file.FileService;
import com.fledge.fledgeserver.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class PresignedUrlAspect {

    private final FileService fileService;

    @AfterReturning(
            pointcut = "execution(* com.fledge.fledgeserver..controller.*.*(..))" +
                    " && (@annotation(org.springframework.web.bind.annotation.ResponseBody)" +
                    " || @target(org.springframework.web.bind.annotation.RestController))",
            returning = "result"
    )
    public void applyPresignedUrls(Object result) {
        if (result instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
            Object body = responseEntity.getBody();
            if (body instanceof ApiResponse) {
                ApiResponse<?> apiResponse = (ApiResponse<?>) body;
                Object data = apiResponse.getData();
                if (data != null) {
                    processObject(data);
                }
            }
        } else if (result instanceof ApiResponse) {
            ApiResponse<?> apiResponse = (ApiResponse<?>) result;
            Object data = apiResponse.getData();
            if (data != null) {
                processObject(data);
            }
        } else {
            processObject(result);
        }
    }

    private void processObject(Object obj) {
        if (obj == null) return;

        if (obj instanceof Collection<?>) {
            for (Object item : (Collection<?>) obj) {
                processObject(item);
            }
        } else if (obj instanceof PresignedUrlApplicable) {
            ((PresignedUrlApplicable) obj).applyPresignedUrls(fileService);
        } else {
            if (obj instanceof Map<?, ?>) {
                processMap((Map<?, ?>) obj);
            } else {
            }
        }
    }

    private void processMap(Map<?, ?> map) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof PresignedUrlApplicable) {
                ((PresignedUrlApplicable) value).applyPresignedUrls(fileService);
            } else if (value instanceof Collection<?>) {
                processObject(value);
            } else if (value instanceof Map<?, ?>) {
                processMap((Map<?, ?>) value);
            }
        }
    }
}