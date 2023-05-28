package project.phoneshop.model.payload.response.shipping;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import org.json.JSONException;
import org.json.JSONObject;
import project.phoneshop.model.entity.CartEntity;
import project.phoneshop.model.entity.ShippingEntity;

import java.net.URI;
import java.net.http.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Map;

public class ShippingPDFExporter {
    private ShippingEntity shipping;

    public ShippingPDFExporter(ShippingEntity shipping) {
        this.shipping = shipping;
    }
//    private void writeTableHeader(PdfPTable table) {
//        PdfPCell cell = new PdfPCell();
//        cell.setBackgroundColor(Color.BLUE);
//        cell.setPadding(5);
//
//        Font font = FontFactory.getFont(FontFactory.HELVETICA);
//        font.setColor(Color.WHITE);
//
//        cell.setPhrase(new Phrase("User ID", font));
//
//        table.addCell(cell);
//
//        cell.setPhrase(new Phrase("E-mail", font));
//        table.addCell(cell);
//
//        cell.setPhrase(new Phrase("Full Name", font));
//        table.addCell(cell);
//
//        cell.setPhrase(new Phrase("Roles", font));
//        table.addCell(cell);
//
//        cell.setPhrase(new Phrase("Enabled", font));
//        table.addCell(cell);
//    }
//
//    private void writeTableData(PdfPTable table) {
//        for (User user : listUsers) {
//            table.addCell(String.valueOf(user.getId()));
//            table.addCell(user.getEmail());
//            table.addCell(user.getFullName());
//            table.addCell(user.getRoles().toString());
//            table.addCell(String.valueOf(user.isEnabled()));
//        }
//    }


    private void writeTableLogo(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("User ID", font));

        table.addCell(cell);

        cell.setPhrase(new Phrase("E-mail", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Full Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Roles", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Enabled", font));
        table.addCell(cell);
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException, JSONException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("hello.pdf"));

        document.open();

        //Set font PDF
        Font font = FontFactory.getFont("assets/arial.ttf");
        font.setSize(18);
        Font fontBody = FontFactory.getFont("assets/arial.ttf");
        fontBody.setSize(14);
        BaseFont bf = BaseFont.createFont("assets/ArialUnicodeMS.ttf","Identity-H",BaseFont.NOT_EMBEDDED);
        Font font1 = new Font(bf,13,0);
        //First Line
        Paragraph p = new Paragraph("Shipping Request", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        //Stack 1
        PdfPTable tableLogo = new PdfPTable(2);
        tableLogo.setWidthPercentage(100f);
        tableLogo.setWidths(new float[] {1.0f, 2.5f});
        tableLogo.setSpacingBefore(10);

        PdfPCell cellLogo = new PdfPCell();
        URL imageUrlRed = getClass().getClassLoader().getResource("assets/S-Phone_cpfelx.png");
        Image mySmileyRed = com.lowagie.text.Image.getInstance(imageUrlRed);
        mySmileyRed.scalePercent(26);
        Chunk chunk = new Chunk(mySmileyRed,0,0,false);
        cellLogo.setPhrase(new Phrase(chunk));
        tableLogo.addCell(cellLogo);

        PdfPTable tableDescription = new PdfPTable(1);
        tableDescription.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableDescription.setWidthPercentage(100f);
        tableDescription.setWidths(new float[] {1.0f});
        tableDescription.setSpacingBefore(10);
        Barcode128 code128 = new Barcode128();
        String code = String.valueOf(shipping.getOrderShipping().getName());
        code128.setCode(code);
        code128.setCodeType(Barcode128.CODE128);
        Image code128Image = code128.createImageWithBarcode(writer.getDirectContent(), null, null);
        Chunk chunk1 = new Chunk(code128Image,0,0,false);
        PdfPCell cellBarcode = new PdfPCell();
        cellBarcode.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellBarcode.setVerticalAlignment(Element.ALIGN_CENTER);
        cellBarcode.setPhrase(new Phrase(chunk1));
        cellBarcode.setBorder(Rectangle.NO_BORDER);
        tableDescription.addCell(cellBarcode);

        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase("Mã đơn hàng: "+shipping.getOrderShipping().getName(), font1));
        cell.setBorder(Rectangle.NO_BORDER);
        tableDescription.addCell(cell);;
        cell.setPhrase(new Phrase("Mã vận đơn: "+String.valueOf(shipping.getId()), font1));
        cell.setBorder(Rectangle.NO_BORDER);
        tableDescription.addCell(cell);
        cell.setPhrase(new Phrase("Ngày vận đơn: "+shipping.getCreate().toString(), font1));
        cell.setBorder(Rectangle.NO_BORDER);
        tableDescription.addCell(cell);
        tableLogo.addCell(tableDescription);

        //Stack 2
        PdfPTable tableAddress = new PdfPTable(2);
        tableAddress.setWidthPercentage(100f);
        tableAddress.setWidths(new float[] {1.0f, 1.0f});
        tableAddress.setSpacingBefore(10);

        PdfPTable tableAddressFrom = new PdfPTable(1);
        tableAddressFrom.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableAddressFrom.setWidthPercentage(50f);
        tableAddressFrom.setWidths(new float[] {1.0f});
        tableAddressFrom.setSpacingBefore(10);

        cell.setPhrase(new Phrase(" - Nơi gửi : Số 2, Võ Văn Ngân, Linh Chiểu, Thủ Đức, Hồ Chí Minh", font1));
        cell.setBorder(Rectangle.NO_BORDER);
        tableAddressFrom.addCell(cell);;
        cell.setPhrase(new Phrase(" - Người gửi : SPhone Store", font1));
        cell.setBorder(Rectangle.NO_BORDER);
        tableAddressFrom.addCell(cell);
        cell.setPhrase(new Phrase(" - Liên hệ : 0868704516", font1));
        cell.setBorder(Rectangle.NO_BORDER);
        tableAddressFrom.addCell(cell);
        tableAddress.addCell(tableAddressFrom);

        PdfPTable tableAddressTo = new PdfPTable(1);
        tableAddressTo.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableAddressTo.setWidthPercentage(50f);
        tableAddressTo.setWidths(new float[] {1.0f});
        tableAddressTo.setSpacingBefore(10);

        cell.setPhrase(new Phrase(" - Nơi nhận: "+getAddress(shipping.getOrderShipping().getAddressOrder().getProvince(),shipping.getOrderShipping().getAddressOrder().getDistrict(),shipping.getOrderShipping().getAddressOrder().getCommune(), shipping.getOrderShipping().getAddressOrder().getAddressDetail()),font1));
        cell.setBorder(Rectangle.NO_BORDER);
        tableAddressTo.addCell(cell);;
        cell.setPhrase(new Phrase(" - Người nhận: "+shipping.getOrderShipping().getAddressOrder().getFullName(), font1));
        cell.setBorder(Rectangle.NO_BORDER);
        tableAddressTo.addCell(cell);
        cell.setPhrase(new Phrase(" - Liên hệ: "+shipping.getOrderShipping().getAddressOrder().getPhoneNumber(), font1));
        cell.setBorder(Rectangle.NO_BORDER);
        tableAddressTo.addCell(cell);
        tableAddress.addCell(tableAddressTo);

        //Stack 3
        PdfPTable tableOrder = new PdfPTable(1);
        tableOrder.setWidthPercentage(100f);
        tableOrder.setWidths(new float[] {1.0f});
        tableOrder.setSpacingBefore(10);
        cell.setPhrase(new Phrase(" - Danh sách sản phẩm: ", font1));
        cell.setBorder(Rectangle.BOX);
        tableOrder.addCell(cell);
        int i = 1;
        for(CartEntity cart: shipping.getOrderShipping().getCartOrder()){
            cell.setPhrase(new Phrase(" + "+i+". "+cart.getProductCart().getName()+ " - Số lượng: "+cart.getQuantity(), font1));
            cell.setBorder(Rectangle.BOX);
            tableOrder.addCell(cell);
        }
        //Stack 4
        PdfPTable tableTotal = new PdfPTable(1);
        tableTotal.setWidthPercentage(100f);
        tableTotal.setWidths(new float[] {1.0f});
        tableTotal.setSpacingBefore(10);
        DecimalFormat df = new DecimalFormat("###,###,###");
        cell.setPhrase(new Phrase(" Tổng tiền: "+df.format(shipping.getOrderShipping().getTotal()) +" VND", font1));
        cell.setBorder(Rectangle.BOX);
        tableTotal.addCell(cell);
        //Stack 5
        PdfPTable tableSignal = new PdfPTable(2);
        tableSignal.setWidthPercentage(100f);
        tableSignal.setWidths(new float[] {1.5f, 1.0f});
        tableSignal.setSpacingBefore(10);

        PdfPTable tableAddressCaution = new PdfPTable(1);
        tableAddressCaution.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableAddressCaution.setWidthPercentage(60f);
        tableAddressCaution.setWidths(new float[] {1.0f});
        tableAddressCaution.setSpacingBefore(10);

        cell.setPhrase(new Phrase(" - Cho người nhận xem sản phẩm", font1));
        cell.setBorder(Rectangle.NO_BORDER);
        tableAddressCaution.addCell(cell);
        cell.setPhrase(new Phrase(" - Liên hệ với SPhone nếu hàng bị bong tróc seal", font1));
        cell.setBorder(Rectangle.NO_BORDER);
        tableAddressCaution.addCell(cell);
        cell.setPhrase(new Phrase(" - Xác nhận sản phẩm không bị bong tróc seal", font1));
        cell.setBorder(Rectangle.NO_BORDER);
        tableAddressCaution.addCell(cell);
        tableSignal.addCell(tableAddressCaution);

        PdfPTable tableCustomerSignal = new PdfPTable(1);
        tableCustomerSignal.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableCustomerSignal.setWidthPercentage(40f);
        tableCustomerSignal.setWidths(new float[] {1.0f});
        tableCustomerSignal.setSpacingBefore(10);

        cell.setPhrase(new Phrase(" - Chữ ký người nhận", font1));
        cell.setBorder(Rectangle.NO_BORDER);
        tableCustomerSignal.addCell(cell);
        tableSignal.addCell(tableCustomerSignal);

        //Register Document
        document.add(p);
        document.add(tableLogo);
        document.add(tableAddress);
        document.add(tableOrder);
        document.add(tableTotal);
        document.add(tableSignal);

        document.close();
    }

    public static String getAddress(String p, String d, String m, String detail) throws JSONException {
        String address = detail;
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("https://provinces.open-api.vn/api/w/"+m))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response1 = null;
        try {
            response1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject jObject1  = new JSONObject(response1.body());
        address+=", "+ jObject1.get("name");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("https://provinces.open-api.vn/api/d/"+d))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response2 = null;
        try {
            response2 = HttpClient.newHttpClient().send(request2, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject jObject2  = new JSONObject(response2.body());
        address+=", "+jObject2.get("name");
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(URI.create("https://provinces.open-api.vn/api/p/"+p))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response3 = null;
        try {
            response3 = HttpClient.newHttpClient().send(request3, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject jObject3  = new JSONObject(response3.body());
        address+=", "+jObject3.get("name");
        return address;
    }

}
