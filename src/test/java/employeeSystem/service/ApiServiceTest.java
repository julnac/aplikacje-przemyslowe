package employeeSystem.service;

import employeeSystem.exception.ApiException;
import employeeSystem.model.Employee;
import employeeSystem.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ApiServiceTest {

    private HttpClient mockClient;
    private HttpResponse<String> mockResponse;
    private ApiService apiService;

    @BeforeEach
    void setUp() {
        mockClient = Mockito.mock(HttpClient.class);
        mockResponse = Mockito.mock(HttpResponse.class);
        apiService = new ApiService(mockClient);
    }

    // -------------------------------------------------------
    // SCENARIUSZ 1: Poprawna odpowiedź JSON
    // -------------------------------------------------------
    @Test
    void shouldParseValidJsonResponse() throws Exception {
        String json = """
            [
              {
                "name": "Jan Kowalski",
                "email": "jan@firma.pl",
                "company": {"name": "FirmaA"}
              }
            ]
            """;

        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(json);
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        List<Employee> result = apiService.fetchEmployeesFromApi("https://fake.api/employees");

        assertEquals(1, result.size());
        Employee emp = result.get(0);
        assertEquals("Jan", emp.getName());
        assertEquals("Kowalski", emp.getSurname());
        assertEquals("FirmaA", emp.getCompany());
        assertEquals(Position.PROGRAMISTA, emp.getPosition());

        verify(mockClient, times(1)).send(any(), any());
    }

    // -------------------------------------------------------
    // SCENARIUSZ 2: Błąd HTTP 404
    // -------------------------------------------------------
    @Test
    void shouldThrowApiExceptionOnHttpError() throws Exception {
        when(mockResponse.statusCode()).thenReturn(404);
        when(mockResponse.body()).thenReturn("Not Found");
        when(mockClient.send(
                any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())
        ).thenReturn(mockResponse);

        ApiException ex = assertThrows(ApiException.class, () ->
                apiService.fetchEmployeesFromApi("https://fake.api/notfound")
        );

        assertTrue(ex.getMessage().contains("Błąd HTTP"));
    }

    // -------------------------------------------------------
    // SCENARIUSZ 3: Niepoprawny JSON
    // -------------------------------------------------------
    @Test
    void shouldThrowApiExceptionOnInvalidJson() throws Exception {
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn("{ not_valid_json }");
        when(mockClient.send(
                any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())
        ).thenReturn(mockResponse);

        ApiException ex = assertThrows(ApiException.class, () ->
                apiService.fetchEmployeesFromApi("https://fake.api/badjson")
        );

        assertTrue(ex.getMessage().contains("Błąd parsowania JSON"));
    }

    // -------------------------------------------------------
    // SCENARIUSZ 4: IOException (np. brak internetu)
    // -------------------------------------------------------
    @Test
    void shouldThrowApiExceptionOnIOException() throws Exception {
        when(mockClient.send(any(), any())).thenThrow(new java.io.IOException("network error"));

        ApiException ex = assertThrows(ApiException.class, () ->
                apiService.fetchEmployeesFromApi("https://fake.api/fail")
        );

        assertTrue(ex.getMessage().contains("Błąd połączenia"));
    }
}
