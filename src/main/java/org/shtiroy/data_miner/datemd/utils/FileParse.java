package org.shtiroy.data_miner.datemd.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.shtiroy.data_miner.datemd.exception.ParseExcelError;
import org.shtiroy.data_miner.datemd.entity.Company;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.*;

public class FileParse {
    private static final Logger LOGGER = LogManager.getLogger(FileParse.class.getName());

    /**
     * Парсер excel список компаний.
     * @param fileName - имя файла.
     * @return - список компаний.
     */
    public List<Company> parseExcel(String fileName) throws ParseExcelError {
        List<Company> result = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(new File(fileName))) {
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = 1;
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                if (rowCount < 3) {
                    rowIterator.next();
                    rowCount++;
                } else {
                    Row row = rowIterator.next();
                    int cellCount = 0;
                    Company company = new Company();
                    for (Cell cell : row) {
                        switch (cell.getCellType()) {
                            case STRING:
                                if (cellCount == 0) company.setIdno(cell.getStringCellValue());
                                if (cellCount == 2) company.setCompanyName(cell.getStringCellValue());
                                if (cellCount == 3) company.setCompanyType(cell.getStringCellValue());
                                if (cellCount == 4) company.setCompanyAddress(cell.getStringCellValue());
                                if (cellCount == 5) company.setCuatm(cell.getStringCellValue());
                                if (cellCount == 6) company.setCompanyLeaders(cell.getStringCellValue());
                                if (cellCount == 7) company.setCompanyFounders(cell.getStringCellValue());
                                if (cellCount == 8) company.setCompanyBeneficiaries(cell.getStringCellValue());
                                if (cellCount == 9) company.setCompanyActivities(cell.getStringCellValue());
                                if (cellCount == 10) company.setCompanyActivitiesLic(cell.getStringCellValue());
                                break;
                            case NUMERIC:
                                if (cellCount == 1) company.setDateStart(new Date(cell.getDateCellValue().getTime()));
                                if (cellCount == 11) company.setDateEnd(new Date(cell.getDateCellValue().getTime()));
                                break;
                        }
                        cellCount++;
                    }
                    rowCount++;
                    result.add(company);
                }
            }
        }catch (IOException exception){
            LOGGER.error(exception.getMessage());
            throw new ParseExcelError("Error parse file: " + fileName + " | " + exception.getMessage());
        }
        return result;
    }
}
