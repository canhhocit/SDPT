package com.phananh.e_commerce.core.infrastructure.service;

import java.util.Map;

/**
 * Cloudinary-related operations exposed to application code.
 *
 * NOTE: this service now focuses on generating signatures for client-side
 * direct uploads. Upload/delete operations should be handled by dedicated
 * server-side helpers or by the client using the generated signature.
 */
public interface CloudinaryService {


    /**
     * Convenience helper to generate an upload signature for a given folder.
     * Returns a map containing signature, timestamp, apiKey, cloudName and folder.
     */
    Map<String, Object> generateUploadSignature(String folder);

    /**
     * Delete a resource on Cloudinary by its public id.
     * publicId should be the path-like id used when uploading (folder/sub/name)
     */
    void deleteFile(String publicId);

    /**
     * Convenience: delete resource by URL (extracts publicId then deletes).
     */
    void deleteFileByUrl(String url);
}
