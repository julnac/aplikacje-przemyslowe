package employeeSystem.model;

import java.util.List;

public class ImportSummary {
    private int importedCount;
    private List<String> errors;

    public ImportSummary(int importedCount, List<String> errors){
        this.importedCount = importedCount;
        this.errors = errors;
    }

    public int getImportedCount() {
        return importedCount;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void setImportedCount(int importedCount) {
        this.importedCount = importedCount;
    }

    @Override
    public String toString() {
        return "Zaimportowano: " + importedCount + ", błędy: " + errors;
    }
}
