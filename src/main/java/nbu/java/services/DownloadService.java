package nbu.java.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nbu.java.model.pojo.Contact;
import nbu.java.repositories.ContactRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
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

    public ResponseEntity exportToCsv(int userId)  {
        List<Contact> contacts = contactRepository.findByUserId(userId);

        InputStreamResource resource = null;
        try {
            File temp = File.createTempFile("contacts", ".csv");
            FileWriter writer = new FileWriter(temp);
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            for (Contact contact : contacts) {
                csvPrinter.printRecord(contact.getFirstName(), contact.getLastName(), contact.getCompanyName(),
                        contact.getAddress(), contact.getPhoneNumber(), contact.getEmail(), contact.getFaxNumber(),
                        contact.getMobilePhoneNumber(),contact.getComment());
            }

            writer.close();

            resource = new InputStreamResource(new FileInputStream(temp));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=contacts.csv")
                .contentType(MediaType.valueOf("text/csv"))
                .body(resource);

    }
}
