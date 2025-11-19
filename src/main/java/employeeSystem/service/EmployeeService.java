package employeeSystem.service;

import employeeSystem.model.CompanyStatistics;
import employeeSystem.model.Position;
import employeeSystem.model.Employee;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private HashMap<String, Employee> employeesMap = new HashMap<>();

    public EmployeeService() {
        this.employeesMap = new HashMap<>();
        System.out.println("Employee service has been created by Spring!");
    }

//    zarzadzanie pracownikami

    public void addEmployee(Employee worker) {
        boolean exists = employeesMap.containsKey(worker.getEmail());
        if (exists) {
            System.out.println("Pracownik z mailem " + worker.getEmail() + " już istnieje!");
        } else {
            employeesMap.put(worker.getEmail(), worker);
        }
    }

    public void removeEmployee(Employee worker) {
        employeesMap.remove(worker.getEmail());
    }

    public List<Employee> getAllEmployees() {
        return employeesMap.values().stream().toList();
    }


//    statystyki analityczne

    public static List<Employee> findByCompany(String companyName, List<Employee> workers) {
        return workers.stream()
                .filter(worker -> worker.getCompany().equals(companyName))
                .collect(Collectors.toList());
    }

    public static List<Employee> sortBySurname(List<Employee> workers) {
        return workers.stream()
                .sorted((w1, w2) -> {
                    String surname1 = w1.getSurname();
                    String surname2 = w2.getSurname();
                    return surname1.compareTo(surname2);
                })
                .collect(Collectors.toList());
    }

    public static Map<Position, List<Employee>> groupByPosition(List<Employee> workers) {
        return workers.stream()
                .collect(Collectors.groupingBy(Employee::getPosition));
    }

    public static Map<Position, Long> countByPosition(List<Employee> workers) {
        return workers.stream()
                .collect(Collectors.groupingBy(Employee::getPosition, Collectors.counting()));
    }

//    statystyki finansowe

    public static double averageSalary(List<Employee> workers) {
        return workers.stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
    }

    public static Optional<Employee> highestPaid(List<Employee> workers) {
        return workers.stream()
                .max(Comparator.comparingDouble(Employee::getSalary));
    }

    //Operacje analityczne

    public List<Employee> validateSalaryConsistency() {
        return employeesMap.values().stream()
                .filter(e -> e.getSalary() < e.getPosition().getBaseSalary())
                .collect(Collectors.toList());
    }

    public Map<String, CompanyStatistics> getCompanyStatistics() {
        return employeesMap.values().stream()
                .collect(Collectors.groupingBy(
                        Employee::getCompany,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    long count = list.size();
                                    double avgSalary = list.stream()
                                            .mapToDouble(Employee::getSalary)
                                            .average()
                                            .orElse(0.0);

                                    Employee topEarner = list.stream()
                                            .max(Comparator.comparingDouble(Employee::getSalary))
                                            .orElse(null);

                                    String topEarnerName = topEarner != null
                                            ? topEarner.getName() + " " + topEarner.getSurname()
                                            : "brak imienia";

                                    return new CompanyStatistics(
                                            list.get(0).getCompany(),
                                            count,
                                            avgSalary,
                                            topEarnerName
                                    );
                                }
                        )
                ));
    }

}
