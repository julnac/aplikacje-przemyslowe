package employeeSystem.service;

import com.google.gson.*;
import employeeSystem.exception.ApiException;
import employeeSystem.model.Employee;
import employeeSystem.model.Position;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ApiService {

    private final HttpClient httpClient;

    public ApiService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    // konstruktor testowy
    public ApiService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public List<Employee> fetchEmployeesFromApi(String apiUrl) throws ApiException {
        List<Employee> employees = new ArrayList<>();

        try {
            // 🔹 Tworzymy klienta HTTP (Java 11+)
            // HttpClient client = HttpClient.newHttpClient();

            // 🔹 Przygotowujemy żądanie GET
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            // 🔹 Wykonujemy żądanie
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new ApiException("Błąd HTTP: " + response.statusCode());
            }

            String jsonResponse = response.body();

            // 🔹 Parsowanie JSON przy użyciu Gson (JsonArray)
            JsonArray jsonArray = JsonParser.parseString(jsonResponse).getAsJsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();

                // name (np. "Leanne Graham")
                String fullName = obj.get("name").getAsString();
                String[] nameParts = fullName.split(" ", 2);
                String firstName = nameParts.length > 0 ? nameParts[0] : "";
                String lastName = nameParts.length > 1 ? nameParts[1] : "";

                // email
                String email = obj.get("email").getAsString();

                // company.name
                String companyName = obj.getAsJsonObject("company").get("name").getAsString();

                // tworzymy pracownika
                Employee emp = new Employee(
                        firstName,
                        lastName,
                        email,
                        Position.PROGRAMISTA,
                        companyName
                        // Position.PROGRAMISTA.getBaseSalary() // np. 8000.0
                );

                employees.add(emp);
            }

        } catch (IOException | InterruptedException e) {
            throw new ApiException("Błąd połączenia z API: " + e.getMessage(), e);
        } catch (JsonParseException e) {
            throw new ApiException("Błąd parsowania JSON: " + e.getMessage(), e);
        }

        return employees;
    }
}

//uwaga!
//employeeSystem.model.Worker{name='Mrs.', surname='Dennis Schulist', email='Karley_Dach@jasper.info', position=PROGRAMISTA, company=Considine-Lockman, salary=8000.0}