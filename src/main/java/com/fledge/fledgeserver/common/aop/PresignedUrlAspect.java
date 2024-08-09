package com.fledge.fledgeserver.common.aop;

import com.fledge.fledgeserver.file.FileService;
import com.fledge.fledgeserver.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.util.Collection;
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
                    try {
                        processObject(data);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void processObject(Object obj) throws IllegalAccessException {
        if (obj == null) return;

        if (obj instanceof Collection<?>) {
            for (Object item : (Collection<?>) obj) {
                processObject(item);
            }
        } else {
            processFields(obj);
        }
    }

    private void processFields(Object obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 필드가 String 타입이고 @ApplyPresignedUrl 어노테이션이 적용된 경우에만 접근 설정
            if (field.getType().equals(String.class) && field.isAnnotationPresent(ApplyPresignedUrl.class)) {
                field.setAccessible(true);
                Object value = field.get(obj);

                if (value != null) {
                    String presignedUrl = fileService.getDownloadPresignedUrl((String) value);
                    field.set(obj, presignedUrl);
                }
            } else if (field.getType().equals(Collection.class) && field.isAnnotationPresent(ApplyPresignedUrl.class)) {
                // List나 Set 같은 컬렉션 타입 필드가 @ApplyPresignedUrl로 지정된 경우
                Collection<?> collection = (Collection<?>) field.get(obj);
                if (collection != null) {
                    for (Object item : collection) {
                        processObject(item);
                    }
                }
            }
//            else if (!field.getType().isPrimitive() && !field.getType().getPackageName().startsWith("java.")) {
//                // Java 표준 라이브러리 클래스의 필드 -> 재귀적으로 처리하지 않음
//                processFields(value);
//            }
        }
    }

}
