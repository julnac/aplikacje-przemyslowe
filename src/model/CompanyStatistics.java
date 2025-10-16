package model;

public class CompanyStatistics {
    private String companyName;
    private long employeeCount;
    private double averageSalary;
    private String topEarnerFullName;

    public CompanyStatistics(String companyName, long employeeCount, double averageSalary, String topEarnerFullName) {
        this.companyName = companyName;
        this.employeeCount = employeeCount;
        this.averageSalary = averageSalary;
        this.topEarnerFullName = topEarnerFullName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public long getEmployeeCount() {
        return employeeCount;
    }

    public double getAverageSalary() {
        return averageSalary;
    }

    public String getTopEarnerFullName() {
        return topEarnerFullName;
    }

    @Override
    public String toString() {
        return String.format(
                "%s → liczba pracowników: %d, średnia pensja: %.2f PLN, najlepiej zarabia: %s",
                companyName, employeeCount, averageSalary, topEarnerFullName
        );
    }
}
