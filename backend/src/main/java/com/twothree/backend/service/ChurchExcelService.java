package com.twothree.backend.service;

import com.twothree.backend.entity.Church;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ChurchExcelService {

    public ByteArrayOutputStream generateChurchExcel(List<Church> churches) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Churches");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Name", "Address", "Phone", "Email", "Website", "Pastor Name", "Pastor Phone", "Pastor Email", "Status"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Populate data rows
        int rowNum = 1;
        for (Church church : churches) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(church.getId());
            row.createCell(1).setCellValue(church.getName());
            row.createCell(2).setCellValue(church.getAddress());
            row.createCell(3).setCellValue(church.getPhone());
            row.createCell(4).setCellValue(church.getEmail());
            row.createCell(5).setCellValue(church.getWebsite());
            row.createCell(6).setCellValue(church.getPastorName());
            row.createCell(7).setCellValue(church.getPastorPhone());
            row.createCell(8).setCellValue(church.getPastorEmail());
            row.createCell(9).setCellValue(church.getStatus().name());
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream;
    }
}
