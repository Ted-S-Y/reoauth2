package com.rbros.oauth2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * DB에 존재하지 않는 것을 조회할 때 처리하기 위한 exception 클래스
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private String resourceName;
    private String fieldName;
    private Object fieldValue;
 
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
 
    public String getResourceName() {
        return resourceName;
    }
 
    public String getFieldName() {
        return fieldName;
    }
 
    public Object getFieldValue() {
        return fieldValue;
    }
}