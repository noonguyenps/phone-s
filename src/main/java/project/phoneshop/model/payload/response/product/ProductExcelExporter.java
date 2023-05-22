package project.phoneshop.model.payload.response.product;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import project.phoneshop.model.entity.ProductEntity;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class ProductExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<ProductEntity> listProducts;

    public ProductExcelExporter(List<ProductEntity> listProducts) {
        this.listProducts = listProducts;
        workbook = new XSSFWorkbook();
    }
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Products");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Product ID", style);
        createCell(row, 1, "Product Name", style);
        createCell(row, 2, "Description", style);
        createCell(row, 3, "Image", style);
        createCell(row, 4, "Price", style);
        createCell(row, 5, "Brand", style);
        createCell(row, 6, "Category", style);
        createCell(row, 7, "Status", style);
        createCell(row, 8, "Date Create", style);

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
        }else if (value instanceof Double) {
            cell.setCellValue((Double) value);
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

        for (ProductEntity product : listProducts) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, product.getId(), style);
            createCell(row, columnCount++, product.getName(), style);
            createCell(row, columnCount++, product.getDescription(), style);
            createCell(row, columnCount++, product.getImageProductEntityList().get(0).getUrl(), style);
            createCell(row, columnCount++, product.getPrice(), style);
            createCell(row, columnCount++, product.getProductBrand().getName(), style);
            createCell(row, columnCount++, product.getProductCategory().getName(), style);
            createCell(row, columnCount++, product.getStatus()==1?("Đang bán"):("Đang ẩn"), style);
            createCell(row, columnCount++, product.getCreate().toString(),style);

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
