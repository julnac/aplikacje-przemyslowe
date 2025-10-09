import java.util.List;

public class Company {
    private String name;
    private String nip;
    private List<Worker> workers;

    public Company(String name, String nip, List<Worker> workers) {
        this.name = name;
        this.nip = nip;
        this.workers = workers;
    }

    public String getName() {
        return name;
    }
    public String getNip() {
        return nip;
    }
    public List<Worker> getWorkers() {
        return workers;
    }

    public void setName(String name) { this.name = name;}
    public void setNip(String nip) { this.nip = nip;}
    public void setWorkers(List<Worker> workers) { this.workers = workers;}

    public void addWorker(Worker worker) {
        boolean exists = workers.stream().anyMatch(w -> w.getEmail().equals(worker.getEmail()));
        if (exists) {
            System.out.println("Pracownik z mailem " + worker.getEmail() + " już istnieje!");
        } else {
            workers.add(worker);
        }
    }

    public void removeWorker(Worker worker) {
        workers.remove(worker);
    }

    @Override
    public String toString() {
        return "Company{name='" + name + "', nip='" + nip + "', workers=" + workers + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company)) return false;
        Company company = (Company) o;
        return nip.equals(company.nip);
    }

    @Override
    public int hashCode() {
        return nip.hashCode();
    }

}
