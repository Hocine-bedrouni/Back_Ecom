package fr.insy2s.commerce.shoponlineback.services;
import java.io.IOException;
import java.util.UUID;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class FirebaseFileService {

	private static final String BUCKET_NAME = "ecom-be518.appspot.com";
	
	private static final String PROJECT_ID = "ecom-be518";
	
    private Storage storage;

    @EventListener
    public void init(ApplicationReadyEvent event) {
        try {
            ClassPathResource serviceAccount = new ClassPathResource("firebase.json");
            storage = StorageOptions.newBuilder().
                    setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream())).
                    setProjectId(PROJECT_ID).build().getService();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String upload(MultipartFile file) throws IOException{
        String filename = generateFileName(file.getOriginalFilename());
        BlobId blobId = BlobId.of(BUCKET_NAME, filename);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();
        storage.create(blobInfo, file.getInputStream().readAllBytes()).getMediaLink();
        return String.format("https://storage.googleapis.com/%s/%s", BUCKET_NAME, filename);
    }
    
    private String generateFileName(String originalFileName) {
        return  "images/" + UUID.randomUUID().toString() + "." + getExtension(originalFileName);
    }

    private String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }
}