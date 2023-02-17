package com.flow.fileextensioncheck.repository;

import com.flow.fileextensioncheck.entity.FileExtension;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface FileExtensionRepository extends JpaRepository<FileExtension, Long> {

    @Transactional
    void deleteByFileExtension(String fileExtension);

    List<FileExtension> findAllByFixedFalse();

/*    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<FileExtension> findByFixedFalse();*/

    int countByFixedFalse();

    @Modifying
    @Query("DELETE FROM FileExtension f WHERE f.fixed = false ")
    void deleteAll();
}
