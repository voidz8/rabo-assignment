# Rabobank Customer Statement Processor

This is a Kotlin + Spring Boot application that reads customer statement records from **CSV** and **XML** files, validates them, and logs any issues.

---

## **Features**
- Reads CSV and XML files from the classpath.
- Validates:
    1. Unique transaction references.
    2. Correct end balances(`startBalance + mutation == endBalance`).
- Logs invalid records with a clear reason.
- Returns a list of valid records for further processing.
---
## **How to Run**

### **Prerequisites**
- JDK 21 installed
- Maven

### **Run the application**

```bash
./mvnw spring-boot:run
```
The application will:
1. Read `records.csv` and `records.xml` from `src/main/resources`.
2. Validate the records.
3. Log the number of valid records and all validation errors.
---

## **Validation Rules**
- **Duplicate references**: Any duplicate `reference` value will be logged and filtered out.
- **End balance check**: If `startBalance + mutation != endBalance`, the record is logged as invalid and filtered out.

---

## **Tests**

Run all tests with:
```bash
./mvnw test
```

## Assumptions
1. Description fields are allowed to be empty.
2. Input files are currently loaded from the classpath.

## Design Choices
1. Invalid records are logged; only valid records are returned.
2. XML and CSV are processed together to catch cross-file duplicates.
3. Reference uniqueness is checked across all loaded records.
4. End balance validation is strict: `startBalance + mutation == endBalance`.
5. Files are named via application properties.
