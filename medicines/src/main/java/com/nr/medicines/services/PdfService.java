package com.nr.medicines.services;

import com.nr.medicines.models.*;
import com.nr.medicines.repositories.DayPeriodsRepository;
import com.nr.medicines.repositories.MedicationRepository;
import com.nr.medicines.repositories.TemporaryMedicationRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PdfService {
    private final MedicationRepository medicationRepository;
    private final DayPeriodsRepository dayPeriodsRepository;
    private final TemporaryMedicationRepository temporaryMedicationRepository;

    public PdfService(
            MedicationRepository medicationRepository,
            DayPeriodsRepository dayPeriodsRepository,
            TemporaryMedicationRepository temporaryMedicationRepository
    ){
        this.dayPeriodsRepository = dayPeriodsRepository;
        this.medicationRepository = medicationRepository;
        this.temporaryMedicationRepository = temporaryMedicationRepository;
    }

    private static StringBuilder getStringBuilder(List<Medication> medicationList, String cardString) {
        StringBuilder cards = new StringBuilder();

        for(Medication medication : medicationList){
            String card = cardString;
            card = card.replace("{{color}}", "white");
            card = card.replace("{{name}}", medication.getMedicine().getName());
            card = card.replace("{{dosage}}", medication.getMedicine().getDosage());
            card = card.replace("{{generic}}", "medication.getMedicine().isGeneric()");
            card = card.replace("{{form}}", medication.getMedicine().getForm());
            card = card.replace("{{packagingSize}}", medication.getMedicine().getPackagingSize());
            card = card.replace("{{obs}}", medication.getObservations() == null ? "" : medication.getObservations());
            card = card.replace("{{image}}", "http://localhost:8080/medicines-api/medicines/image/"+ medication.getMedicine().getBarcode());

            cards.append(card);
        }
        return cards;
    }

    private static StringBuilder getTemporaryMedicationStringBuilder(List<TemporaryMedication> temporaryMedicationList, String cardString) {
        StringBuilder cards = new StringBuilder();

        for(TemporaryMedication temporaryMedication : temporaryMedicationList){
            String card = cardString;
            card = card.replace("{{color}}", "#f7e2b2");
            card = card.replace("{{name}}", temporaryMedication.getMedication().getMedicine().getName());
            card = card.replace("{{dosage}}", temporaryMedication.getMedication().getMedicine().getDosage());
            card = card.replace("{{generic}}", "temporaryMedication.getMedication().getMedicine().isGeneric()");
            card = card.replace("{{form}}", temporaryMedication.getMedication().getMedicine().getForm());
            card = card.replace("{{packagingSize}}", temporaryMedication.getMedication().getMedicine().getPackagingSize());
            card = card.replace("{{obs}}", temporaryMedication.getMedication().getObservations() == null ? "" : temporaryMedication.getMedication().getObservations());
            card = card.replace("{{image}}", "http://localhost:8080/medicines-api/medicines/image/"+ temporaryMedication.getMedication().getMedicine().getBarcode());

            cards.append(card);
        }
        return cards;
    }

    public ByteArrayOutputStream generateHtmlToPdf(Person person, PdfContentEnum pdfContent) throws Exception {
        String htmlFile;
        try (InputStream inputStream = getClass().getResourceAsStream("/templates/file.html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            htmlFile = reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        }

        String cardFile;
        try (InputStream inputStream = getClass().getResourceAsStream("/templates/card-"+pdfContent.getValue()+".html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            cardFile = reader.lines()
                        .collect(Collectors.joining(System.lineSeparator()));
        }

        Document inputHtml = createWellFormedHtml(htmlFile, cardFile, person);
        return xhtmlToPdf(inputHtml);
    }

    private Document createWellFormedHtml(String html, String cardString, Person person) throws IOException {
        List<DayPeriod> dayPeriods = this.dayPeriodsRepository.findAll();

        for(DayPeriod dayPeriod : dayPeriods){
            List<Medication> medicationList = this.medicationRepository.findByPersonAndDayPeriodOrderByCreated(person, dayPeriod);
            List<TemporaryMedication> temporaryMedicationsList = this.temporaryMedicationRepository.findTemporaryMedications(person, dayPeriod);

            StringBuilder cards = getStringBuilder(medicationList, cardString);
            StringBuilder temporaryMedicationCards = getTemporaryMedicationStringBuilder(temporaryMedicationsList, cardString);

            html = html.replace("{{"+dayPeriod.getDescription()+"}}", cards.append(temporaryMedicationCards));
        }

        Document document = Jsoup.parse(html, String.valueOf(StandardCharsets.UTF_8));
        document.outputSettings()
                .syntax(Document.OutputSettings.Syntax.xml);
        return document;
    }

    private ByteArrayOutputStream xhtmlToPdf(Document xhtml) throws Exception {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);
            renderer.setDocumentFromString(xhtml.html());
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream;
        }
    }
}
