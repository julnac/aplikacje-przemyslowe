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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApiService {

    private final String apiUrl;
    private final HttpClient httpClient;
    private final Gson gson;

    public ApiService(
            @Value("${app.api.url}") String apiUrl,
            HttpClient httpClient,
            Gson gson
    ) {
        this.apiUrl = apiUrl;
        this.httpClient = httpClient;
        this.gson = gson;
        System.out.println("ApiService initialized with URL: " + apiUrl);
    }


    public List<Employee> fetchEmployeesFromApi() throws ApiException {
        List<Employee> employees = new ArrayList<>();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new ApiException("Błąd HTTP: " + response.statusCode());
            }

            JsonArray array = JsonParser.parseString(response.body()).getAsJsonArray();

            for (JsonElement element : array) {
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
                employees.add(new Employee (
                        firstName,
                        lastName,
                        email,
                        Position.PROGRAMISTA,
                        companyName
                        // Position.PROGRAMISTA.getBaseSalary() // np. 8000.0
                ));

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