CREATE TABLE vaccination_data (
    id SERIAL PRIMARY KEY,
    country VARCHAR(255) NOT NULL,
    country_iso_code VARCHAR(10) NOT NULL,
    date DATE NOT NULL,
    total_vaccinations BIGINT,
    total_people_vaccinated BIGINT,
    total_people_fully_vaccinated BIGINT,
    daily_vaccinations_raw BIGINT,
    daily_vaccinations BIGINT,
    total_vaccinations_per_hundred DECIMAL(5, 2),
    total_people_vaccinated_per_hundred DECIMAL(5, 2),
    total_people_fully_vaccinated_per_hundred DECIMAL(5, 2),
    number_of_vaccinations_per_day BIGINT,
    daily_vaccinations_per_million DECIMAL(10, 2),
    vaccines_used BIGINT,
    source_name VARCHAR(255),
    source_website VARCHAR(255)
);

CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    data_nascimento DATE,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);