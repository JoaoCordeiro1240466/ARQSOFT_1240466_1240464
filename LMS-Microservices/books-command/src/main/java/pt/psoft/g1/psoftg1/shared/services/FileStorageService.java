package pt.psoft.g1.psoftg1.shared.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

// This is a mock implementation. In a real scenario, this would
// interact with a file storage system like S3, a local disk, etc.
@Service
public class FileStorageService {

    public String getRequestPhoto(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            // In a real implementation, you would save the file and return its unique identifier/path.
            return "uploads/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        }
        return null;
    }

    public void deleteFile(String filePath) {
        // In a real implementation, you would delete the file from the storage system.
        System.out.println("Deleting file: " + filePath);
    }

    public byte[] getFile(String filePath) {
        // In a real implementation, you would read the file from the storage system.
        System.out.println("Fetching file: " + filePath);
        return new byte[0];
    }

    public Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
