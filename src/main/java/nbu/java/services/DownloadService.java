package nbu.java.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nbu.java.model.pojo.Contact;
import nbu.java.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DownloadService {
    @Autowired
    private ContactRepository<Contact> contactRepository;

    public ResponseEntity<byte[]> exportToJson(int userId) {
        List<Contact> contacts = contactRepository.findByUserId(userId);
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String contactsJsonString = gson.toJson(contacts);
        byte[] contactsJsonBytes = contactsJsonString.getBytes();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=contacts.json")
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(contactsJsonBytes.length)
                .body(contactsJsonBytes);
    }
}
