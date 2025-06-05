package org.example.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.constants.TaskStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@DynamicInsert //created_at 등을 자동으로 관리하기 위한 어노테이션
@DynamicUpdate
@Entity(name = "TASK")
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(value = EnumType.STRING)
    private TaskStatus status;

    private Date dueDate;

    @CreationTimestamp
    @Column(insertable=false, updatable = false)
    private Timestamp createdAt;

    @CreationTimestamp
    @Column(insertable=false, updatable = false)
    private Timestamp updatedAt;
}