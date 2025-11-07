package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.Entity.ServiceReport;
import com.example.SWP391_FALL25.Entity.ServiceReportDetails;
import com.example.SWP391_FALL25.Repository.ServiceReportDetailsRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfGeneratorService {

    @Autowired
    private ServiceReportDetailsRepository serviceReportDetailsRepository;

    public byte[] generateReportPDF(ServiceReport report) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 50, 50, 60, 50);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // ===== 1️⃣ HEADER =====
            // Logo (nếu có file logo trong resources/static/images/logo.png)
            try {
                Image logo = Image.getInstance("src/main/resources/static/images/logo.png");
                logo.scaleAbsolute(70, 70);
                logo.setAlignment(Element.ALIGN_LEFT);
                document.add(logo);
            } catch (Exception e) {
                // Nếu không có logo, vẫn tiếp tục
            }

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, new BaseColor(41, 128, 185));
            Font subFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLDITALIC);
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 11);
            Font smallGray = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, BaseColor.GRAY);

            Paragraph title = new Paragraph("EV SERVICE REPORT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            Paragraph sub = new Paragraph("Electric Vehicle Maintenance Center", subFont);
            sub.setAlignment(Element.ALIGN_CENTER);
            document.add(sub);

            document.add(new LineSeparator());
            document.add(new Paragraph(" "));

            // ===== 2️⃣ CUSTOMER INFO =====
            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingBefore(10f);
            infoTable.setSpacingAfter(15f);
            infoTable.setWidths(new float[]{1.5f, 2f});

            infoTable.addCell(getCell("Customer Name:", boldFont, Element.ALIGN_LEFT, BaseColor.WHITE));
            infoTable.addCell(getCell(report.getAppointment().getVehicle().getCustomer().getFullname(), normalFont, Element.ALIGN_LEFT, BaseColor.WHITE));

            infoTable.addCell(getCell("Vehicle:", boldFont, Element.ALIGN_LEFT, BaseColor.WHITE));
            infoTable.addCell(getCell(report.getAppointment().getVehicle().getBrand() + " " +
                    report.getAppointment().getVehicle().getModel(), normalFont, Element.ALIGN_LEFT, BaseColor.WHITE));

            infoTable.addCell(getCell("License Plate:", boldFont, Element.ALIGN_LEFT, BaseColor.WHITE));
            infoTable.addCell(getCell(report.getAppointment().getVehicle().getLicensePlate(), normalFont, Element.ALIGN_LEFT, BaseColor.WHITE));

            infoTable.addCell(getCell("Appointment Date:", boldFont, Element.ALIGN_LEFT, BaseColor.WHITE));
            infoTable.addCell(getCell(df.format(report.getAppointment().getAppointmentDate()), normalFont, Element.ALIGN_LEFT, BaseColor.WHITE));

            infoTable.addCell(getCell("Technician:", boldFont, Element.ALIGN_LEFT, BaseColor.WHITE));
            infoTable.addCell(getCell(report.getAppointment().getTechnicianAssigned(), normalFont, Element.ALIGN_LEFT, BaseColor.WHITE));

            document.add(infoTable);

            // ===== 3️⃣ SERVICE DETAILS TABLE =====
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3, 2, 1, 2, 2});

            // Header
            BaseColor headerColor = new BaseColor(41, 128, 185);
            String[] headers = {"Service Task", "Part", "Qty", "Labor Cost (VND)", "Total (VND)"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE)));
                cell.setBackgroundColor(headerColor);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
                table.addCell(cell);
            }

            // Data rows
            List<ServiceReportDetails> details = serviceReportDetailsRepository.findByReport(report);
            double totalSum = 0;

            for (ServiceReportDetails d : details) {
                table.addCell(getCell(d.getService(), normalFont, Element.ALIGN_LEFT, BaseColor.WHITE));
                table.addCell(getCell(d.getPart() != null ? d.getPart().getName() : "-", normalFont, Element.ALIGN_LEFT, BaseColor.WHITE));
                table.addCell(getCell(String.valueOf(d.getQuantity()), normalFont, Element.ALIGN_CENTER, BaseColor.WHITE));
                table.addCell(getCell(String.format("%,.0f", d.getLaborCost()), normalFont, Element.ALIGN_RIGHT, BaseColor.WHITE));
                table.addCell(getCell(String.format("%,.0f", d.getTotalCost()), normalFont, Element.ALIGN_RIGHT, BaseColor.WHITE));
                totalSum += d.getTotalCost();
            }

            // Tổng cộng
            PdfPCell totalLabel = new PdfPCell(new Phrase("TOTAL PAYMENT", boldFont));
            totalLabel.setColspan(4);
            totalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalLabel.setPadding(8);
            table.addCell(totalLabel);

            PdfPCell totalValue = new PdfPCell(new Phrase(String.format("%,.0f VND", totalSum), boldFont));
            totalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalValue.setPadding(8);
            table.addCell(totalValue);

            document.add(table);

            // ===== 4️⃣ FOOTER =====
            document.add(new Paragraph(" "));
            document.add(new LineSeparator());
            Paragraph thank = new Paragraph("Thank you for trusting our Electric Vehicle Maintenance Service!", smallGray);
            thank.setAlignment(Element.ALIGN_CENTER);
            document.add(thank);

            Paragraph contact = new Paragraph("Hotline: 1900 888 999 | Email: support@evservice.vn | Website: www.evservice.vn", smallGray);
            contact.setAlignment(Element.ALIGN_CENTER);
            document.add(contact);

            document.close();
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error while generating PDF: " + e.getMessage());
        }
    }

    private PdfPCell getCell(String text, Font font, int alignment, BaseColor bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(6);
        cell.setBackgroundColor(bgColor);
        cell.setBorderColor(new BaseColor(220, 220, 220));
        return cell;
    }
}
