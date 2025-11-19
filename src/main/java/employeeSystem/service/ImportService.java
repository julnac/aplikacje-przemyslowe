package employeeSystem.service;

import employeeSystem.model.ImportSummary;
import employeeSystem.model.Position;
import employeeSystem.model.Employee;
import employeeSystem.exception.InvalidDataException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class ImportService {

    private final EmployeeService employeeService;
    private final String csvFilePath;

    public ImportService(
            EmployeeService employeeService,
            @Value("${app.import.csv-file}") String csvFilePath
            ) {
        this.employeeService = employeeService;
        this.csvFilePath = csvFilePath;
        System.out.println("Import service has been created by Spring!");
    }

    public ImportSummary importFromCsv () {
        int importedCount = 0;
        List<String> errors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            int lineNumber = 0;

            // pominięcie nagłówka
            reader.readLine();
            lineNumber++;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                try {
                    String[] parts = line.split(",");

                    if (parts.length != 6) {
                        throw new InvalidDataException("Niepoprawna liczba kolumn (" + parts.length + ")");
                    }

                    String firstName = parts[0].trim();
                    String lastName = parts[1].trim();
                    String email = parts[2].trim();
                    String company = parts[3].trim();
                    String positionStr = parts[4].trim();
                    String salaryStr = parts[5].trim();

                    // walidacja stanowiska
                    Position position;
                    try {
                        position = Position.valueOf(positionStr.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new InvalidDataException("Nieznane stanowisko: " + positionStr);
                    }

                    //walidacja pensji
                    double salary;
                    try {
                        salary = Double.parseDouble(salaryStr);
                        if (salary <= 0) throw new InvalidDataException("Wynagrodzenie musi byc dodatnie");
                    } catch (NumberFormatException e){
                        throw new InvalidDataException("Niepoprawny format wynagrodzenia: " + salaryStr);
                    }

                    Employee emp = new Employee(firstName, lastName, email, position, company);
                    employeeService.addEmployee(emp);
                    importedCount++;

                } catch (InvalidDataException e) {
                    errors.add("Linia " + lineNumber + ": " + e.getMessage());
                }
            }
        } catch(IOException e) {
            errors.add("Błąd odczytu pliku: " + e.getMessage());
        }

        return new ImportSummary(importedCount, errors);

    }

}


