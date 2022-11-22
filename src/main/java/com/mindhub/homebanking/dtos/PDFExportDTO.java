package com.mindhub.homebanking.dtos;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class PDFExportDTO {
    private List<TransactionDTO> listTransactionDTO;

    public PDFExportDTO(List<TransactionDTO> listTransactionDTO) {
        this.listTransactionDTO = listTransactionDTO;
    }


    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);

        cell.setPhrase(new Phrase("Transaction nr", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Amount", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Balance", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Date-Time", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        for (TransactionDTO transactionDTO : listTransactionDTO) {
            table.addCell(String.valueOf(transactionDTO.getId()));
            table.addCell(String.valueOf(transactionDTO.getAmount()));
            table.addCell(String.valueOf(transactionDTO.getBalance()));
            table.addCell(String.valueOf(transactionDTO.getDescription()));
            table.addCell(String.valueOf(transactionDTO.getDate()));
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_BOLD);
        fontTitle.setSize(18);

        Paragraph p = new Paragraph("Transactions", fontTitle);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f, 1.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

    }

    public List<TransactionDTO> getListTransactionDTO() {
        return listTransactionDTO;
    }

    public void setListTransactionDTO(List<TransactionDTO> listTransactionDTO) {
        this.listTransactionDTO = listTransactionDTO;
    }
}
