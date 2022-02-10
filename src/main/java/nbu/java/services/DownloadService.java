package nbu.java.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nbu.java.model.pojo.Contact;
import nbu.java.repositories.ContactRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

    public ResponseEntity exportToExcel(int userId)  {
        List<Contact> contacts = contactRepository.findByUserId(userId);

        XSSFWorkbook workbook = new XSSFWorkbook();


        XSSFSheet spreadsheet = workbook.createSheet("contacts");

        int rownum = 0;
        for (Contact contact : contacts)
        {
            Row row = spreadsheet.createRow(rownum++);
            createRow(contact, row);
        }
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=contacts.xlsx")
                .contentType(MediaType.valueOf("text/xlsx"))
                .body(new InputStreamResource(in));

    }

    private void createRow(Contact contact, Row row) {
        Cell cell = row.createCell(0);
        cell.setCellValue(contact.getFirstName());

        cell = row.createCell(1);
        cell.setCellValue(contact.getLastName());

        cell = row.createCell(2);
        cell.setCellValue(contact.getCompanyName());

        cell = row.createCell(3);
        cell.setCellValue(contact.getAddress());

        cell = row.createCell(4);
        cell.setCellValue(contact.getPhoneNumber());

        cell = row.createCell(5);
        cell.setCellValue(contact.getEmail());

        cell = row.createCell(6);
        cell.setCellValue(contact.getFaxNumber());

        cell = row.createCell(7);
        cell.setCellValue(contact.getMobilePhoneNumber());

        cell = row.createCell(8);
        cell.setCellValue(contact.getComment());

        cell = row.createCell(9);
        cell.setCellValue(contact.getLabel().toString());
    }
}
