package employeeSystem.service;

import employeeSystem.exception.InvalidDataException;
import employeeSystem.model.Employee;
import employeeSystem.model.ImportSummary;
import employeeSystem.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImportServiceTest {

    private EmployeeService employeeService;
    private ImportService importService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService();
        importService = new ImportService(employeeService);
    }

    /**
     * Pomocnicza metoda do tworzenia plików CSV w katalogu tymczasowym.
     */
    private File createTempCsv(@TempDir Path tempDir, String content) throws IOException {
        File file = tempDir.resolve("test.csv").toFile();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
        return file;
    }

    // -------------------------------------------------------
    // SCENARIUSZ 1: Poprawny import danych
    // -------------------------------------------------------
    @Test
    void shouldImportValidData(@TempDir Path tempDir) throws IOException {
        // given
        String csv = "firstName,lastName,email,company,position,salary\n" +
                "Jan,Kowalski,jan@firma.pl,FirmaA,DEVELOPER,5000\n" +
                "Anna,Nowak,anna@firma.pl,FirmaB,MANAGER,7000\n";
        File csvFile = createTempCsv(tempDir, csv);

        // when
        ImportSummary summary = importService.importFromCsv(csvFile.getAbsolutePath());

        // then
        List<Employee> all = employeeService.getAllEmployees();
        assertEquals(2, all.size());
        assertEquals(2, summary.getImportedCount());
        assertTrue(summary.getErrors().isEmpty());
    }

    // -------------------------------------------------------
    // SCENARIUSZ 2: Niepoprawne stanowisko (błąd w jednej linii)
    // -------------------------------------------------------
    @Test
    void shouldHandleUnknownPosition(@TempDir Path tempDir) throws IOException {
        // given
        String csv = "firstName,lastName,email,company,position,salary\n" +
                "Jan,Kowalski,jan@firma.pl,FirmaA,UNKNOWN,4000\n";
        File csvFile = createTempCsv(tempDir, csv);

        // when
        ImportSummary summary = importService.importFromCsv(csvFile.getAbsolutePath());

        // then
        assertEquals(0, summary.getImportedCount());
        assertFalse(summary.getErrors().isEmpty());
        assertTrue(summary.getErrors().get(0).contains("Nieznane stanowisko"));
    }

    // -------------------------------------------------------
    // SCENARIUSZ 3: Ujemne wynagrodzenie
    // -------------------------------------------------------
    @Test
    void shouldHandleNegativeSalary(@TempDir Path tempDir) throws IOException {
        // given
        String csv = "firstName,lastName,email,company,position,salary\n" +
                "Jan,Kowalski,jan@firma.pl,FirmaA,DEVELOPER,-2000\n";
        File csvFile = createTempCsv(tempDir, csv);

        // when
        ImportSummary summary = importService.importFromCsv(csvFile.getAbsolutePath());

        // then
        assertEquals(0, summary.getImportedCount());
        assertTrue(summary.getErrors().get(0).contains("dodatnie"));
    }

    // -------------------------------------------------------
    // SCENARIUSZ 4: Niepoprawny format liczby
    // -------------------------------------------------------
    @Test
    void shouldHandleInvalidSalaryFormat(@TempDir Path tempDir) throws IOException {
        String csv = "firstName,lastName,email,company,position,salary\n" +
                "Jan,Kowalski,jan@firma.pl,FirmaA,DEVELOPER,abc\n";
        File csvFile = createTempCsv(tempDir, csv);

        ImportSummary summary = importService.importFromCsv(csvFile.getAbsolutePath());

        assertEquals(0, summary.getImportedCount());
        assertTrue(summary.getErrors().get(0).contains("Niepoprawny format wynagrodzenia"));
    }

    // -------------------------------------------------------
    // SCENARIUSZ 5: Brak pliku
    // -------------------------------------------------------
    @Test
    void shouldReturnErrorWhenFileNotFound() {
        ImportSummary summary = importService.importFromCsv("nieistnieje.csv");

        assertEquals(0, summary.getImportedCount());
        assertFalse(summary.getErrors().isEmpty());
        assertTrue(summary.getErrors().get(0).contains("Błąd odczytu pliku"));
    }
}

