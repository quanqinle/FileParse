package com.github.quanqinle.parse;

import com.github.quanqinle.util.LogUtil;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;
import java.io.IOException;

public class ParsePDF {

  public static void main(String[] args) throws IOException {
    String documentFile = "C:\\MyData\\《Java 8实战》.pdf";

    try (PDDocument pdfDocument = Loader.loadPDF(new File(documentFile))) {
      PDDocumentCatalog dc = pdfDocument.getDocumentCatalog();
      int numPages = dc.getPages().getCount();
      LogUtil.info("page cnt = " + numPages);

      PDFTextStripper stripper = new PDFTextStripper();
      stripper.setStartPage(5);
      stripper.setEndPage(5);
      String content = stripper.getText(pdfDocument);
      LogUtil.info(content);

      for (PDPage page : pdfDocument.getPages()) {
        // todo
      }
    }
  }
}
