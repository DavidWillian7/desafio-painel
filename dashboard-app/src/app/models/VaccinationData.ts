export interface VaccinationData {
  id: number;
  country: string;
  countryIsoCode: string;
  date: string; 
  totalVaccinations: number | null;
  totalPeopleVaccinated: number | null;
  totalPeopleFullyVaccinated: number | null;
  dailyVaccinationsRaw: number | null;
  dailyVaccinations: number | null;
  totalVaccinationsPerHundred: number | null;
  totalPeopleVaccinatedPerHundred: number | null;
  totalPeopleFullyVaccinatedPerHundred: number | null;
  numberOfVaccinationsPerDay: number | null;
  dailyVaccinationsPerMillion: number | null;
  vaccinesUsed: number | null;
  sourceName: string | null;
  sourceWebsite: string | null;
}
