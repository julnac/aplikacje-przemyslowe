package service;

import model.ImportSummary;
import model.Position;
import model.Employee;
import exception.InvalidDataException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImportService {

    private final EmployeeService employeeService;

    public ImportService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public ImportSummary importFromCsv (String filePath) {
        int importedCount = 0;
        List<String> errors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
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


