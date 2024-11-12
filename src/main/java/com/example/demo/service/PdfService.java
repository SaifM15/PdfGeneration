package com.example.demo.service;

import org.springframework.stereotype.Service;
import com.example.demo.pojo.Invoice;
import com.example.demo.pojo.Item;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

@Service
public class PdfService {

    public ByteArrayInputStream generatePdf(Invoice invoice) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin;
                float xStart = margin;
                float contentWidth = page.getMediaBox().getWidth() - 2 * margin;
                
                // Table for Seller and Buyer Information
                float tableTopY = yStart - 50;
                float cellHeight = 100f;
                float cellWidth = contentWidth / 2;

                contentStream.setLineWidth(1f);
                contentStream.addRect(xStart, tableTopY - cellHeight, cellWidth, cellHeight); // Seller cell
                contentStream.addRect(xStart + cellWidth, tableTopY - cellHeight, cellWidth, cellHeight); // Buyer cell
                contentStream.stroke();

                // Vertical Line between Seller and Buyer
                contentStream.moveTo(xStart + cellWidth, tableTopY);
                contentStream.lineTo(xStart + cellWidth, tableTopY - cellHeight);
                contentStream.stroke();

                // Adding Seller Information
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.newLineAtOffset(xStart + 10, tableTopY - 20);
                contentStream.showText("Seller:");
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText(invoice.getSeller());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText(invoice.getSellerAddress());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("GSTIN: " + invoice.getSellerGstin());
                contentStream.endText();

                // Adding Buyer Information
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.newLineAtOffset(xStart + cellWidth + 10, tableTopY - 20);
                contentStream.showText("Buyer:");
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText(invoice.getBuyer());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText(invoice.getBuyerAddress());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("GSTIN: " + invoice.getBuyerGstin());
                contentStream.endText();

                // Header for Item Table
                float itemsTableY = tableTopY - cellHeight - 30;
                drawTableHeader(contentStream, xStart, itemsTableY);

                // Adding Items Rows
                drawTableRows(contentStream, xStart, itemsTableY - 20, invoice.getItems());
            }

            document.save(outputStream);
            Files.write(Paths.get("D:/pdf/output.pdf"), outputStream.toByteArray()); // Optional save to file
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private void drawTableHeader(PDPageContentStream contentStream, float x, float y) throws IOException {
        float headerHeight = 20f;
        float columnWidth = 500 / 4; // Four equal-width columns

        contentStream.setLineWidth(1f);
        contentStream.addRect(x, y - headerHeight, 500, headerHeight);
        contentStream.moveTo(x + columnWidth, y);
        contentStream.lineTo(x + columnWidth, y - headerHeight);
        contentStream.moveTo(x + 2 * columnWidth, y);
        contentStream.lineTo(x + 2 * columnWidth, y - headerHeight);
        contentStream.moveTo(x + 3 * columnWidth, y);
        contentStream.lineTo(x + 3 * columnWidth, y - headerHeight);
        contentStream.stroke();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        contentStream.newLineAtOffset(x + 5, y - 15);
        contentStream.showText("Item");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText("Quantity");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText("Rate");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText("Amount");
        contentStream.endText();
    }

    private void drawTableRows(PDPageContentStream contentStream, float x, float y, List<Item> items) throws IOException {
        float rowHeight = 20f;
        float columnWidth = 500 / 4; // Four equal-width columns

        contentStream.setFont(PDType1Font.HELVETICA, 10);

        for (Item item : items) {
            contentStream.addRect(x, y - rowHeight, 500, rowHeight);
            contentStream.moveTo(x + columnWidth, y);
            contentStream.lineTo(x + columnWidth, y - rowHeight);
            contentStream.moveTo(x + 2 * columnWidth, y);
            contentStream.lineTo(x + 2 * columnWidth, y - rowHeight);
            contentStream.moveTo(x + 3 * columnWidth, y);
            contentStream.lineTo(x + 3 * columnWidth, y - rowHeight);
            contentStream.stroke();

            contentStream.beginText();
            contentStream.newLineAtOffset(x + 5, y - 15);
            contentStream.showText(item.getName());
            contentStream.newLineAtOffset(columnWidth, 0);
            contentStream.showText(String.valueOf(item.getQuantity()));
            contentStream.newLineAtOffset(columnWidth, 0);
            contentStream.showText(String.valueOf(item.getRate()));
            contentStream.newLineAtOffset(columnWidth, 0);
            contentStream.showText(String.valueOf(item.getAmount()));
            contentStream.endText();

            y -= rowHeight;
        }
    }
}
