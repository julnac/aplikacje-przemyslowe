public class Worker {
    private String name;
    private String surname;
    private String email;
    private Position position;
    private Company company;
    private double salary;

    public Worker(String name, String surname, String email, Position position, Company company) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.position = position;
        this.company = company;
        this.salary = position.getBaseSalary();
    }

    public String getName() {
        return name;
    }
    public String getSurname() { return surname; }
    public String getEmail() {
        return email;
    }
    public Position getPosition() {
        return position;
    }
    public String getCompany() { return company.getName();}
    public double getSalary() {
        return salary;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) { this.surname = surname; }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPosition(Position position) {
        this.position = position;
    }
    public void setCompany(Company company) { this.company = company;}
    public void setSalary(double salary) { this.salary = salary;}

    public Company getCompanyObject() { return company; }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Worker)) return false;
        Worker worker = (Worker) o;
        return email.equals(worker.email);
    }

    @Override
    public int hashCode(){
        return email.hashCode();
    }

    @Override
    public String toString(){
        return "Worker{name='" + name + "', surname='" + surname + "', email='" + email + "', position=" + position + ", company=" + company.getName() + ", salary=" + salary + "}";
    }

}
