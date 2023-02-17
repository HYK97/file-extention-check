package com.flow.fileextensioncheck.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class FileExtension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_extension_id", nullable = false)
    private Long id;
    @Column(unique = true, length = 20)
    private String fileExtension;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean fixed;

    private FileExtension(String fileExtension, boolean fixed) {
        this.fileExtension = fileExtension;
        this.fixed = fixed;
    }

    public static FileExtension from(String extension, boolean fixed) {
        return new FileExtension(extension, fixed);
    }


}
