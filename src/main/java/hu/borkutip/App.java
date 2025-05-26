package hu.borkutip;

import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.font.FontSet;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) throws IOException {
        createPdf();
    }

    private static void createPdf() throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter("hello.pdf"));
        Document document = new Document(pdf);

        FontProvider fontProvider = new DefaultFontProvider(false, true, false, "NotoSans");
        FontSet fontSet = fontProvider.getFontSet();
        // Map with all the font names and fonts
        Map<String, PdfFont> fonts = fontSet.getFonts().stream()
                .collect(Collectors.toMap(
                        fontInfo -> fontInfo.getDescriptor().getFullNameLowerCase(),
                        fontProvider::getPdfFont,
                        (existing, replacement) -> existing));


        // Embed all the fonts
        fonts.values().forEach(document::setFont);

        // Check all the fonts
        String string = "ÉÁŰŐÚÖÜÓÍéáűőúöüóí";
        fonts.forEach((fontName, font) -> {
            Text text = new Text(fontName + ":" + string);
            text.setFont(font);
            Paragraph paragraph = new Paragraph(text);
            document.add(paragraph);
        });

        document.close();
    }
}
