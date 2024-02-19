package org.ut.server.userservice.utils;

import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Base64;

@Component
public class FileUtils {
    public static Long getFileSizeKB(String base64) {
        return (long) (base64.length() / 1024);
    }

    // convert base64 to Blob
    public static byte[] base64ToBlob(String base64) throws SQLException {
        if (base64 == null) {
            return null;
        }
        // convert to byte
        return base64.getBytes();
    }

    // convert Blob to base64
    public static String blobToBase64(byte[] bytes) throws SQLException {
        if (bytes != null && bytes.length > 0) {
            // to bytes
            return Base64.getEncoder().encodeToString(bytes);

        }
        return null;
    }
}
