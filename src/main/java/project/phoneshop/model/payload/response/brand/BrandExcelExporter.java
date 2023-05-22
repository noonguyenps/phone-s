package project.phoneshop.model.payload.response.brand;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import project.phoneshop.model.entity.BrandEntity;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class BrandExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<BrandEntity> listBrands;

    public BrandExcelExporter(List<BrandEntity> listBrands) {
        this.listBrands = listBrands;
        workbook = new XSSFWorkbook();
    }
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Brands");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Brand ID", style);
        createCell(row, 1, "Brand Name", style);
        createCell(row, 2, "Description", style);
        createCell(row, 3, "Phone", style);
        createCell(row, 4, "Image", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else if (value instanceof UUID) {
            cell.setCellValue(String.valueOf((UUID) value));
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (BrandEntity brand : listBrands) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, brand.getId(), style);
            createCell(row, columnCount++, brand.getName(), style);
            createCell(row, columnCount++, brand.getDescription(), style);
            createCell(row, columnCount++, brand.getPhone(), style);
            createCell(row, columnCount++, brand.getImg(), style);

        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }
}
