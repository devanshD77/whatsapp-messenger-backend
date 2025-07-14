package com.whatsapp.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class FileUtil {

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );

    private static final List<String> ALLOWED_VIDEO_TYPES = Arrays.asList(
            "video/mp4", "video/avi", "video/mov", "video/wmv", "video/flv", "video/webm"
    );

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    public static boolean isValidImageFile(MultipartFile file) {
        return file != null && 
               !file.isEmpty() && 
               ALLOWED_IMAGE_TYPES.contains(file.getContentType()) &&
               file.getSize() <= MAX_FILE_SIZE;
    }

    public static boolean isValidVideoFile(MultipartFile file) {
        return file != null && 
               !file.isEmpty() && 
               ALLOWED_VIDEO_TYPES.contains(file.getContentType()) &&
               file.getSize() <= MAX_FILE_SIZE;
    }

    public static boolean isValidFile(MultipartFile file) {
        return isValidImageFile(file) || isValidVideoFile(file);
    }

    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static String generateUniqueFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        return java.util.UUID.randomUUID().toString() + extension;
    }

    public static boolean isImageFile(String contentType) {
        return contentType != null && contentType.startsWith("image/");
    }

    public static boolean isVideoFile(String contentType) {
        return contentType != null && contentType.startsWith("video/");
    }
} 