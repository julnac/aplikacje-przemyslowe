package employeeSystem;

import employeeSystem.exception.ApiException;
import employeeSystem.model.Employee;

import employeeSystem.service.ApiService;
import employeeSystem.service.EmployeeService;
import employeeSystem.service.ImportService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import java.util.List;

@SpringBootApplication
@ImportResource("classpath:employees-beans.xml")
public class EmployeeManagementApplication implements CommandLineRunner {

    private final EmployeeService employeeService;
    private final ApiService apiService;
    private final ImportService importService;
    private final List<Employee> predefinedEmployees;

    public EmployeeManagementApplication(
            EmployeeService employeeService,
            ApiService apiService,
            ImportService importService,
            @Qualifier("xmlEmployees") List<Employee> predefinedEmployees) {
        this.employeeService = employeeService;
        this.apiService = apiService;
        this.importService = importService;
        this.predefinedEmployees = predefinedEmployees;
    }

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementApplication.class, args);
    }

    @Override
    public void run(String... args) {

        System.out.println("\n=== START SYSTEMU PRACOWNIKÓW ===\n");

        // Add employees from csv
        System.out.println("\nAdding employees from CSV...");
        var csvImportSummary = importService.importFromCsv();
        System.out.println(csvImportSummary);

        // Add employees from XML configuration
        System.out.println("\nLoading predefined employees from XML...");
        for (Employee employee : predefinedEmployees) {
            employeeService.addEmployee(employee);
        }

        // Add employees from api
        System.out.println("\nAdding more employees from API...");
        try {
            var employees = apiService.fetchEmployeesFromApi();
            employees.forEach(employeeService::addEmployee);
        } catch (ApiException e) {
            System.err.println("Błąd pobierania danych: " + e.getMessage());
        }

        List<Employee> allEmployees = employeeService.getAllEmployees();

        System.out.println("\n=== Wszyscy pracownicy ===");
        allEmployees.forEach(System.out::println);

        System.out.println("\n=== Pracownicy z firmy ABC Corp ===");
        employeeService.findByCompany("ABC Corp", allEmployees)
                .forEach(System.out::println);

        System.out.println("\n=== Posortowani po nazwisku ===");
        EmployeeService.sortBySurname(allEmployees)
                .forEach(System.out::println);

        System.out.println("\n=== Grupowanie po stanowisku ===");
        EmployeeService.groupByPosition(allEmployees)
                .forEach((pos, workers) -> {
                    System.out.println(pos + ":");
                    workers.forEach(w -> System.out.println("  " + w));
                });

        System.out.println("\n=== Statystyki firm ===");
        employeeService.getCompanyStatistics().values()
                .forEach(System.out::println);

        System.out.println("\n=== KONIEC DZIAŁANIA ===\n");
    }


}
