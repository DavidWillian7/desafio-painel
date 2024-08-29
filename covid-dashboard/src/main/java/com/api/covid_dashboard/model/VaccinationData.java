package com.api.covid_dashboard.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vaccination_data")
public class VaccinationData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String country;

    @Column(name = "country_iso_code", nullable = false, length = 10)
    private String countryIsoCode;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "total_vaccinations")
    private Long totalVaccinations;

    @Column(name = "total_people_vaccinated")
    private Long totalPeopleVaccinated;

    @Column(name = "total_people_fully_vaccinated")
    private Long totalPeopleFullyVaccinated;

    @Column(name = "daily_vaccinations_raw")
    private Long dailyVaccinationsRaw;

    @Column(name = "daily_vaccinations")
    private Long dailyVaccinations;

    @Column(name = "total_vaccinations_per_hundred", precision = 5, scale = 2)
    private BigDecimal totalVaccinationsPerHundred;

    @Column(name = "total_people_vaccinated_per_hundred", precision = 5, scale = 2)
    private BigDecimal totalPeopleVaccinatedPerHundred;

    @Column(name = "total_people_fully_vaccinated_per_hundred", precision = 5, scale = 2)
    private BigDecimal totalPeopleFullyVaccinatedPerHundred;

    @Column(name = "number_of_vaccinations_per_day")
    private Long numberOfVaccinationsPerDay;

    @Column(name = "daily_vaccinations_per_million", precision = 10, scale = 2)
    private BigDecimal dailyVaccinationsPerMillion;

    @Column(name = "vaccines_used")
    private Long vaccinesUsed;

    @Column(name = "source_name")
    private String sourceName;

    @Column(name = "source_website")
    private String sourceWebsite;
}
