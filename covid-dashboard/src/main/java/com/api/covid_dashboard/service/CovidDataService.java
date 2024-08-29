package com.api.covid_dashboard.service;

import com.api.covid_dashboard.model.VaccinationData;
import com.api.covid_dashboard.repository.CovidDataRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CovidDataService {

    @Autowired
    private CovidDataRepository covidDataRepository;

    @Async
    public CompletableFuture<String> processCSVContent(String csvContent) {
        try (Reader reader = new BufferedReader(new InputStreamReader(new java.io.ByteArrayInputStream(csvContent.getBytes())));
             CSVParser csvParser = CSVFormat.DEFAULT.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .build()
                     .parse(reader)) {

            System.out.println("Cabeçalhos do CSV: " + csvParser.getHeaderNames());

            covidDataRepository.deleteAll();

            List<VaccinationData> vaccinationDataList = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (CSVRecord csvRecord : csvParser) {
                VaccinationData data = new VaccinationData();
                data.setCountry(csvRecord.get("country"));
                data.setCountryIsoCode(csvRecord.get("iso_code"));
                data.setDate(LocalDate.parse(csvRecord.get("date"), formatter));
                data.setTotalVaccinations(parseLong(csvRecord.get("total_vaccinations")));
                data.setTotalPeopleVaccinated(parseLong(csvRecord.get("people_vaccinated")));
                data.setTotalPeopleFullyVaccinated(parseLong(csvRecord.get("people_fully_vaccinated")));
                data.setDailyVaccinationsRaw(parseLong(csvRecord.get("daily_vaccinations_raw")));
                data.setDailyVaccinations(parseLong(csvRecord.get("daily_vaccinations")));
                data.setTotalVaccinationsPerHundred(parseDecimal(csvRecord.get("total_vaccinations_per_hundred")));
                data.setTotalPeopleVaccinatedPerHundred(parseDecimal(csvRecord.get("people_vaccinated_per_hundred")));
                data.setTotalPeopleFullyVaccinatedPerHundred(parseDecimal(csvRecord.get("people_fully_vaccinated_per_hundred")));
                data.setDailyVaccinationsPerMillion(parseDecimal(csvRecord.get("daily_vaccinations_per_million")));
                
                data.setNumberOfVaccinationsPerDay(data.getDailyVaccinations());
                data.setVaccinesUsed(null);
                data.setSourceName(csvRecord.get("source_name"));
                data.setSourceWebsite(csvRecord.get("source_website"));

                vaccinationDataList.add(data);
            }

            covidDataRepository.saveAll(vaccinationDataList);
            return CompletableFuture.completedFuture("Arquivo CSV processado com sucesso");
        } catch (IOException e) {
            System.err.println("Erro ao processar o conteúdo do CSV: " + e.getMessage());
            return CompletableFuture.completedFuture("Erro ao processar o arquivo: " + e.getMessage());
        }
    }

    private Long parseLong(String value) {
        if (value == null || value.isEmpty() || value.equals("")) {
            return 0L;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            try {
                return Math.round(Double.parseDouble(value));
            } catch (NumberFormatException ex) {
                return 0L;
            }
        }
    }

    private BigDecimal parseDecimal(String value) {
        return (value == null || value.isEmpty() || value.equals("")) ? BigDecimal.ZERO : new BigDecimal(value);
    }

    public List<VaccinationData> getAllVaccinationData() {
        return covidDataRepository.findAllByOrderByDateDesc();
    }
}
