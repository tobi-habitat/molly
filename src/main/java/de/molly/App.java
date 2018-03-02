package de.molly;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException {

        Files
                .walk(Paths.get("excel/rohdaten"))
                .filter(path -> path.toAbsolutePath().toString().endsWith(".xlsx"))
                .forEach(file -> {

                    System.out.println(file.toAbsolutePath());
                    List<Value> values = ReadRohdaten(file.toAbsolutePath().toString());
                    DatenImTemplateEinfuegen(values, file.getFileName().toString());
                });
    }

    private static void DatenImTemplateEinfuegen(List<Value> values, String filename) {

        try {

            FileInputStream excelFile = new FileInputStream(new File("excel/template/Datenauswertung_Schlaghammer.xlsx"));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(0);

            for(int i = 0; i < values.size(); i++) {
                Row row = sheet.getRow(i + 1);
                getOrCreate(row, 0).setCellValue(values.get(i).kraft);
                getOrCreate(row, 1).setCellValue(values.get(i).zeit);
                getOrCreate(row, 2).setCellValue(values.get(i).winkel);
                getOrCreate(row, 3).setCellValue(values.get(i).beschleunigung);
            }

            FileOutputStream out = new FileOutputStream("excel/ergebnis/" + filename);
            workbook.write(out);
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Cell getOrCreate(Row row, int cellNumber) {
        Cell cell = row.getCell(cellNumber);
        if(cell == null) {
            cell = row.createCell(cellNumber);
        }
        return cell;
    }


    private static List<Value> ReadRohdaten(String filename) {

        List<Value> result = new ArrayList<>();

        try {

            FileInputStream excelFile = new FileInputStream(new File(filename));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row currentRow : sheet) {

                int rowNum = currentRow.getRowNum();

                //Überspringe die ersten 3
                if(rowNum <= 3) continue;

                Value value = new Value();
                value.kraft = Double.parseDouble(currentRow.getCell(0).getStringCellValue());
                value.zeit = Double.parseDouble(currentRow.getCell(1).getStringCellValue());
                value.winkel = Double.parseDouble(currentRow.getCell(2).getStringCellValue());
                value.beschleunigung = Double.parseDouble(currentRow.getCell(3).getStringCellValue());

                result.add(value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
