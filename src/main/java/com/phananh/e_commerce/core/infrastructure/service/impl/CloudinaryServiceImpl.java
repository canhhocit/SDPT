package com.phananh.e_commerce.core.infrastructure.service.impl;

import com.cloudinary.Cloudinary;
import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.core.infrastructure.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    // Inject shared Cloudinary bean for server-side operations (destroy etc.)
    private final Cloudinary cloudinary;


    /**
     * Generate signature for a specific folder using Cloudinary SDK helper.
     * This follows the pattern you suggested: builds params (timestamp + folder),
     * then uses Cloudinary.apiSignRequest to compute signature.
     */
    @Override
    public Map<String, Object> generateUploadSignature(String folder) {
        long timestamp = System.currentTimeMillis() / 1000L;

        // generate a suggested public id the client can use when uploading
        String generatedId = "upload-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String publicId = (folder != null && !folder.isBlank()) ? (folder + "/" + generatedId) : generatedId;

        Map<String, Object> params = new HashMap<>();
        params.put("timestamp", timestamp);
        params.put("public_id", publicId);

        // Use SDK to sign request
        String signature = this.cloudinary.apiSignRequest(params, apiSecret);

        Map<String, Object> response = new HashMap<>();
        response.put("signature", signature);
        response.put("timestamp", timestamp);
        response.put("apiKey", apiKey);
        response.put("cloudName", cloudName);
        response.put("publicId", publicId);

        return response;
    }

    @Override
    public void deleteFile(String publicId) {
        try {
            Map<String, Object> deleteParams = new HashMap<>();
            deleteParams.put("invalidate", true);
            Map<?, ?> result = this.cloudinary.uploader().destroy(publicId, deleteParams);
            Object r = result.get("result");
            if (r != null) {
                String rs = r.toString();
                if ("ok".equals(rs) || "not found".equals(rs)) {
                    log.info("Cloudinary deleted {} -> {}", publicId, rs);
                    return;
                }
                log.warn("Unexpected Cloudinary delete result for {}: {}", publicId, rs);
            }
        } catch (IOException e) {
            log.error("Error deleting Cloudinary resource {}", publicId, e);
            throw new AppException(ErrorCode.FILE_DELETE_ERROR);
        }
    }

    @Override
    public void deleteFileByUrl(String url) {
        if (url == null || url.isBlank()) {
            throw new AppException(ErrorCode.FILE_DELETE_ERROR);
        }
        String publicId = extractPublicIdFromUrl(url);
        deleteFile(publicId);
    }

    private String extractPublicIdFromUrl(String url) {
        try {
            URI u = URI.create(url);
            String path = u.getPath();
            int idx = path.indexOf("/upload/");
            String after = (idx >= 0) ? path.substring(idx + "/upload/".length()) : path;
            // remove version prefix v12345/
            after = after.replaceFirst("^v\\d+/", "");
            // remove extension
            int dot = after.lastIndexOf('.');
            if (dot > 0) after = after.substring(0, dot);
            return after;
        } catch (Exception e) {
            log.error("Failed to extract publicId from url {}", url, e);
            throw new AppException(ErrorCode.FILE_DELETE_ERROR);
        }
    }
}
