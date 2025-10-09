package model;

public class Worker {
    private String name;
    private String surname;
    private String email;
    private Position position;
    private String company;
    private double salary;

    public Worker(String name, String surname, String email, Position position, String company) {
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
    public String getCompany() { return company;}
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
    public void setCompany(String company) { this.company = company;}
    public void setSalary(double salary) { this.salary = salary;}


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
        return "model.Worker{name='" + name + "', surname='" + surname + "', email='" + email + "', position=" + position + ", company=" + company + ", salary=" + salary + "}";
    }

}
