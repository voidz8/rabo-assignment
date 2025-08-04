# Rabobank Customer Statement Processor

This is a Kotlin + Spring Boot application that reads customer statement records from **CSV** and **XML** files, validates them, and logs any issues.

---

## Features

- Parses **CSV** and **XML** files from the classpath (`/src/main/resources`).
- Validates each transaction record:
  1. Ensures **unique transaction references** (cross-file duplicate detection).
  2. Verifies **end balance correctness** (`startBalance + mutation == endBalance` with max 0.01 tolerance).
- Logs all **invalid records** with specific error reasons (e.g. duplicate, incorrect balance).
- Generates a report `errors.csv` with all invalid records (reference + reasons).
- Returns a list of valid records for further processing or integration.

---

## How to Run

### 🔧 Prerequisites
- JDK 21 installed
- Maven (for local build)
- Docker (optional, for containerized run)

---

### Run via Maven

```bash
./mvnw clean spring-boot:run
```

The application will:

1. Read `records.csv` and `records.xml` from `src/main/resources`
2. Validate all records:
  - Remove duplicate references
  - Check end balance correctness
3. Log all valid records
4. Write invalid records to `errors.csv` in the root directory

---

### Run via Docker

#### Step 1: Build the JAR

```bash
./mvnw clean package -DskipTests
```

#### Step 2: Build the Docker image

```bash
docker build -t rabobank-assignment .
```

#### Step 3: Run the container

```bash
docker run --rm rabobank-assignment
```

The application will perform the same steps and output `errors.csv` inside the container.

---

## Validation Rules
- **Duplicate references**: Any duplicate `reference` value will be logged and filtered out.
- **End balance check**: If `startBalance + mutation != endBalance`, the record is logged as invalid and filtered out.

---

## Tests

Run all tests with:
```bash
./mvnw test
```

## Assumptions
1. Description fields are allowed to be empty.
2. Input files are currently loaded from the classpath.
3. The output error report is saved as a CSV file `errors.csv` in the working directory.

## Design Choices
1. Invalid records are logged and written to a separate error report.
2. Only valid records are returned for further processing.
3. CSV and XML files are processed together to catch duplicates across formats.
4. Reference uniqueness is validated across all input records.
5. End balance validation is strict: startBalance + mutation == endBalance with a tolerance of 0.01.
6. Input filenames for CSV and XML are configurable via application.yaml.