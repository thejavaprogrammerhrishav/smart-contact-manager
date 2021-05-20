package com.smart.contact.util;

import com.smart.contact.contacts.model.Contact;
import com.smart.contact.user.model.User;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

public class Excel {

    private static final String[] NAMES = {"ID", "Name", "NickName", "Contact 1", "Contact 2", "Contact 3",
            "Email ID", "Work", "About", "Date of Entry"};

    private static CellStyle rowStyle = null;
    private static CellStyle cellStyle = null;

    private static CellStyle rowStyle(Workbook workbook) {
        if (rowStyle == null) {
            rowStyle = workbook.createCellStyle();

            Font rowFont = workbook.createFont();
            rowFont.setFontName("Tahoma");
            rowFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            rowFont.setFontHeightInPoints(((short) 12));

            rowStyle.setFont(rowFont);
        }
        return rowStyle;
    }

    private static CellStyle cellStyle(Workbook workbook) {
        if (cellStyle == null) {
            cellStyle = workbook.createCellStyle();

            Font cellFont = workbook.createFont();
            cellFont.setFontName("Tahoma");
            cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
            cellFont.setFontHeightInPoints(((short) 11));

            cellStyle.setFont(cellFont);
        }
        return cellStyle;
    }

    private static Cell getCell(Workbook workbook, Row r, int col) {
        Cell cell = r.createCell(col);
        cell.setCellStyle(cellStyle(workbook));
        return cell;
    }

    private static int doWork(Workbook workbook, Sheet sheet, int row, Contact contact) {
        Row r;
        int col = 0;
        r = sheet.createRow(row++);
        r.setHeight(((short) 550));

        Cell cell = getCell(workbook, r, col++);
        cell.setCellValue(contact.getId());

        cell = getCell(workbook, r, col++);
        cell.setCellValue(contact.getName());

        cell = getCell(workbook, r, col++);
        cell.setCellValue(contact.getNickname());

        cell = getCell(workbook, r, col++);
        cell.setCellValue(contact.getContact1());

        cell = getCell(workbook, r, col++);
        cell.setCellValue(contact.getContact2());

        cell = getCell(workbook, r, col++);
        cell.setCellValue(contact.getContact3());

        cell = getCell(workbook, r, col++);
        cell.setCellValue(contact.getEmail());

        cell = getCell(workbook, r, col++);
        cell.setCellValue(contact.getWork());

        cell = getCell(workbook, r, col++);
        cell.setCellValue(contact.getAbout());

        cell = getCell(workbook, r, col);
        cell.setCellValue(contact.getDate());

        return row;
    }

    private static void row(Workbook ssf, Row r, int c) {
        Cell cell = getCell(ssf, r, c);
        cell.setCellStyle(rowStyle(ssf));
        cell.setCellValue(NAMES[c]);
        cell.getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cell.getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);
    }

    public static void excelXls(User user, ServletOutputStream outputStream) throws IOException {
        HSSFWorkbook hssf = new HSSFWorkbook();
        HSSFSheet sheet = hssf.createSheet("Contact Data");
        int row = 0;

        HSSFRow r = sheet.createRow(row++);
        r.setRowStyle(rowStyle(hssf));
        r.setHeight(((short) 900));
        IntStream.range(0, NAMES.length)
                .forEach(c -> {
                    row(hssf, r, c);
                });

        List<Contact> list = user.getContactList();
        for (Contact contact : list) {
            row = doWork(hssf, sheet, row, contact);
        }

        IntStream.range(0, NAMES.length)
                .forEach(sheet::autoSizeColumn);

        hssf.write(outputStream);
    }


    public static void excelXlsx(User user, ServletOutputStream outputStream) throws IOException {
        XSSFWorkbook xssf = new XSSFWorkbook();
        XSSFSheet sheet = xssf.createSheet("Contact Data");
        int row = 0;

        XSSFRow r = sheet.createRow(row++);
        r.setRowStyle(rowStyle(xssf));
        r.setHeight(((short) 900));
        IntStream.range(0, NAMES.length)
                .forEach(c -> {
                    row(xssf, r, c);
                });

        List<Contact> list = user.getContactList();
        for (Contact contact : list) {
            row = doWork(xssf, sheet, row, contact);
        }

        IntStream.range(0, NAMES.length)
                .forEach(sheet::autoSizeColumn);

        xssf.write(outputStream);
    }
}
