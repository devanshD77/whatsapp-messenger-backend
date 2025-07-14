package com.whatsapp.repository;

import com.whatsapp.model.Attachment;
import com.whatsapp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByMessage(Message message);
    
    @Query("SELECT a FROM Attachment a WHERE a.message = :message AND a.attachmentType = :attachmentType")
    List<Attachment> findByMessageAndAttachmentType(@Param("message") Message message, 
                                                  @Param("attachmentType") Attachment.AttachmentType attachmentType);
    
    @Query("SELECT a FROM Attachment a WHERE a.filePath = :filePath")
    List<Attachment> findByFilePath(@Param("filePath") String filePath);
    
    void deleteByMessage(Message message);
} 