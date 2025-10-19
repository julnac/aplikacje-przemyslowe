package employeeSystem.service;

import employeeSystem.model.CompanyStatistics;
import employeeSystem.model.Employee;
import employeeSystem.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    private EmployeeService service;
    private Employee e1;
    private Employee e2;
    private Employee e3;

    @BeforeEach
    void setUp() {
        service = new EmployeeService();

        e1 = new Employee("Jan", "Kowalski", "jan.kowalski@firma.pl", Position.PROGRAMISTA, "FirmaA");
        e2 = new Employee("Anna", "Nowak", "anna.nowak@firma.pl", Position.MANAGER, "FirmaB");
        e3 = new Employee("Piotr", "Wiśniewski", "piotr.wisniewski@firma.pl", Position.PROGRAMISTA, "FirmaA");
    }

    // ===== Test dodawania pracownika =====
    @Test
    void addEmployee_uniqueEmail_employeeAdded() {
        service.addEmployee(e1);
        assertEquals(1, service.getAllEmployees().size());
    }

    @Test
    void addEmployee_duplicateEmail_printsMessage() {
        service.addEmployee(e1);
        service.addEmployee(e1); // próba dodania ponownie
        assertEquals(1, service.getAllEmployees().size());
    }

    @Test
    void addEmployee_nullEmployee_throwsException() {
        assertThrows(NullPointerException.class, () -> service.addEmployee(null));
    }

    // ===== Test wyszukiwania po firmie =====
    @Test
    void findByCompany_existingCompany_returnsEmployees() {
        service.addEmployee(e1);
        service.addEmployee(e2);
        service.addEmployee(e3);

        List<Employee> result = EmployeeService.findByCompany("FirmaA", service.getAllEmployees());
        assertEquals(2, result.size());
        assertTrue(result.contains(e1));
        assertTrue(result.contains(e3));
    }

    @Test
    void findByCompany_nonExistingCompany_returnsEmptyList() {
        service.addEmployee(e1);
        List<Employee> result = EmployeeService.findByCompany("NieistniejącaFirma", service.getAllEmployees());
        assertTrue(result.isEmpty());
    }

    // ===== Test średniego wynagrodzenia =====
    @Test
    void averageSalary_nonEmptyList_returnsCorrectAverage() {
        service.addEmployee(e1);
        service.addEmployee(e3);

        double avg = EmployeeService.averageSalary(service.getAllEmployees());
        assertEquals((3200 + 2900) / 2.0, avg);
    }

    @Test
    void averageSalary_emptyList_returnsZero() {
        double avg = EmployeeService.averageSalary(Collections.emptyList());
        assertEquals(0.0, avg);
    }

    // ===== Test najwyższego wynagrodzenia =====
    @Test
    void highestPaid_nonEmptyList_returnsCorrectEmployee() {
        service.addEmployee(e1);
        service.addEmployee(e3);

        Optional<Employee> highest = EmployeeService.highestPaid(service.getAllEmployees());
        assertTrue(highest.isPresent());
        assertEquals(e1, highest.get());
    }

    @Test
    void highestPaid_emptyList_returnsEmptyOptional() {
        Optional<Employee> highest = EmployeeService.highestPaid(Collections.emptyList());
        assertTrue(highest.isEmpty());
    }

    // ===== Test walidacji wynagrodzeń =====
    @Test
    void validateSalaryConsistency_returnsEmployeesWithSalaryBelowBase() {
        Employee eLow = new Employee("Kasia", "Zielińska", "kasia.zielinska@firma.pl", Position.PROGRAMISTA, "FirmaA");
        service.addEmployee(e1);
        service.addEmployee(eLow);

        List<Employee> invalid = service.validateSalaryConsistency();
        assertEquals(1, invalid.size());
        assertTrue(invalid.contains(eLow));
    }

    // ===== Test statystyk firmy =====
    @Test
    void getCompanyStatistics_returnsCorrectStatistics() {
        service.addEmployee(e1);
        service.addEmployee(e2);
        service.addEmployee(e3);

        Map<String, CompanyStatistics> stats = service.getCompanyStatistics();
        assertEquals(2, stats.size());

        CompanyStatistics firmaAStats = stats.get("FirmaA");
        assertEquals(2, firmaAStats.getEmployeeCount());
        assertEquals((3200 + 2900) / 2.0, firmaAStats.getAverageSalary());
        assertEquals("Jan Kowalski", firmaAStats.getTopEarnerFullName());
    }
}

