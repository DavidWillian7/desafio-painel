package com.api.covid_dashboard.controller;

import com.api.covid_dashboard.model.VaccinationData;
import com.api.covid_dashboard.service.CovidDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/covid-data")
public class CovidDataController {

    @Autowired
    private CovidDataService covidDataService;

    @PutMapping("/upload")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Por favor, selecione um arquivo para enviar.");
        }

        try {
            String csvContent = new String(file.getBytes());
            covidDataService.processCSVContent(csvContent);
            return ResponseEntity.accepted().body("Processamento do arquivo iniciado. Por favor, verifique os logs para o resultado.");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<VaccinationData>> getVaccinationData() {
        List<VaccinationData> data = covidDataService.getAllVaccinationData();
        return ResponseEntity.ok(data);
    }
}
