package com.rbros.oauth2.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * API응답을 반환하기 위한 DTO 클래스
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class ApiResponse {
    private boolean success;
    private String message;
}