package com.whatsapp.dto;

import com.whatsapp.model.Attachment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AttachmentDto {

    private Long id;
    
    @NotBlank(message = "File name is required")
    private String fileName;
    
    @NotBlank(message = "File path is required")
    private String filePath;
    
    @NotBlank(message = "File type is required")
    private String fileType;
    
    private Long fileSize;
    
    @NotNull(message = "Attachment type is required")
    private Attachment.AttachmentType attachmentType;
    
    private Long messageId;
    private LocalDateTime createdAt;

    // Constructors
    public AttachmentDto() {}

    public AttachmentDto(Long id, String fileName, String filePath, String fileType, 
                        Long fileSize, Attachment.AttachmentType attachmentType, 
                        Long messageId, LocalDateTime createdAt) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.attachmentType = attachmentType;
        this.messageId = messageId;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Attachment.AttachmentType getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(Attachment.AttachmentType attachmentType) {
        this.attachmentType = attachmentType;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AttachmentDto{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                ", attachmentType=" + attachmentType +
                ", messageId=" + messageId +
                ", createdAt=" + createdAt +
                '}';
    }
} 