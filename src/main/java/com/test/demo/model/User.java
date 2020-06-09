package com.test.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String password;
    private LocalDateTime createdDate;  // 생성일
    private LocalDateTime modifiedDate; // 수정일
}
