package com.smart.contact.util;

import com.smart.contact.contacts.model.Contact;
import com.smart.contact.user.model.User;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Base64;
import java.util.List;

public class Csv {
    public static void getCsv(User user, ServletOutputStream outputStream) throws IOException {
        Appendable out = new OutputStreamWriter(outputStream);
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);
        List<Contact> contactList = user.getContactList();
        csvPrinter.printRecord("ID", "Name", "Nick Name", "Contact 1", "Contact 2", "Contact 3", "Email ID",
                "Work", "About", "Date of Entry");
        contactList.forEach(c -> {
            doCsv(csvPrinter, c);
        });
        csvPrinter.close();
    }

    private static void doCsv(CSVPrinter csvPrinter, Contact c) {
        try {
            csvPrinter.printRecord(c.getId(), c.getName(), c.getNickname(),
                    c.getContact1(), c.getContact2(), c.getContact3(), c.getEmail(),
                    c.getWork(), c.getAbout(), c.getDate());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
