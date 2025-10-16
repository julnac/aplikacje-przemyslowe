import exception.ApiException;
import model.Employee;
import service.ApiService;
import service.EmployeeService;
import service.ImportService;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        // tworzenie systemu rejestru pracowników
        EmployeeService registry = new EmployeeService();

        ApiService apiService = new ApiService();
        try {
            var employees = apiService.fetchEmployeesFromApi("https://jsonplaceholder.typicode.com/users");
            employees.forEach(registry::addEmployee);
        } catch (ApiException e) {
            System.err.println("Błąd pobierania danych: " + e.getMessage());
        }
//
//        // pracownicy
//        Employee w1 = new Employee("Jan", "Kowalski", "jan.kowalski@gmail.com", Position.MANAGER, "ABC Corp");
//        Employee w2 = new Employee("Anna", "Nowak", "anna.nowak@gmail.com", Position.PROGRAMISTA, "ABC Corp");
//        Employee w3 = new Employee("Piotr", "Zając", "piotr.zajac@gmail.com", Position.STAZYSTA, "ABC Corp");
//        Employee w4 = new Employee("Ewa", "Malinowska", "ewa.malinowska@gmail.com", Position.PREZES, "TechCorp");
//        Employee w5 = new Employee("Tomasz", "Wiśniewski", "tomasz.wisniewski@gmail.com", Position.WICEPREZES, "TechCorp");
//        Employee w6 = new Employee("Karolina", "Maj", "karolina.maj@gmail.com", Position.PROGRAMISTA, "TechCorp");
//
//        registry.addEmployee(w1);
//        registry.addEmployee(w2);
//        registry.addEmployee(w3);
//        registry.addEmployee(w4);
//        registry.addEmployee(w5);
//        registry.addEmployee(w6);

        ImportService importer = new ImportService(registry);
        var summary = importer.importFromCsv("src/service/employees.csv");
        System.out.println(summary);

        System.out.println("\n=== Wszyscy pracownicy w systemie ===");
        List<Employee> allEmployees = registry.getAllEmployees();
        allEmployees.forEach(System.out::println);
//        registry.getAllEmployees().forEach(System.out::println);

        //  Operacje analityczne dla systemu
        System.out.println("\n=== Pracownicy z firmy ABC Corp ===");
        EmployeeService.findByCompany("ABC Corp", allEmployees).forEach(System.out::println);

        System.out.println("\n=== Posortowani alfabetycznie po nazwisku (globalnie) ===");
        EmployeeService.sortBySurname(allEmployees).forEach(System.out::println);

        System.out.println("\n=== Grupowanie po stanowisku (globalnie) ===");
        EmployeeService.groupByPosition(registry.getAllEmployees()).forEach((position, workers) -> {
            System.out.println(position + ":");
            workers.forEach(worker -> System.out.println("  " + worker));
        });

        System.out.println("\n=== Liczba pracowników na każdym stanowisku(globalnie) ===");
        System.out.println(EmployeeService.countByPosition(allEmployees));

        // Operacje finansowe dla systemu
        System.out.println("\n=== Średnie wynagrodzenie w całym systemie ===");
        System.out.printf("%.2f PLN%n", EmployeeService.averageSalary(allEmployees));

        System.out.println("\n=== Pracownik z najwyższym wynagrodzeniem (globalnie) ===");
        EmployeeService.highestPaid(allEmployees).ifPresent(System.out::println);

        //Statystyki dla firm
        System.out.println("\n=== Średnia pensja w firmie TechCorp ===");
        var workers_from_techCorp = EmployeeService.findByCompany("TechCorp", allEmployees);
        System.out.printf("%.2f PLN%n", EmployeeService.averageSalary(workers_from_techCorp));

        System.out.println("\n=== Najlepiej zarabiający w firmie TechCorp ===");
        EmployeeService.highestPaid(workers_from_techCorp).ifPresent(System.out::println);

        // Operacje analityczne:

        System.out.println("\n=== Walidacja spójności wynagrodzeń ===");
        List<Employee> inconsistent = registry.validateSalaryConsistency();

        if (inconsistent.isEmpty()) {
            System.out.println("Wszyscy pracownicy mają poprawne wynagrodzenia.");
        } else {
            inconsistent.forEach(System.out::println);
        }

        System.out.println("\n=== Statystyki firm ===");
        registry.getCompanyStatistics().values().forEach(System.out::println);

    }
}
