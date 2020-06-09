package com.test.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "USER_SEQ_INDEX_GEN",
        sequenceName = "USER_SEQ",
        initialValue = 1,
        allocationSize = 1
)
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "USER_SEQ_INDEX_GEN"
    )
//    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", length = 20, nullable = false, unique = true)
    private String name;

    @Column(name = "PASSWORD", length = 100, nullable = false)
    private String password;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "CREATED_DATE", updatable = false)
    private LocalDateTime createdDate;  // 생성일

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "MODIFIED_DATE", updatable = true)
    private LocalDateTime modifiedDate; // 수정일


    @Builder
    public User(String name, String password){
        this.name = name;
        this.password = password;
    }
}
