package com.api.covid_dashboard.repository;

import com.api.covid_dashboard.model.VaccinationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CovidDataRepository extends JpaRepository<VaccinationData, Long> {
    List<VaccinationData> findAllByOrderByDateDesc();
}
