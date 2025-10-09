import java.util.*;
import java.util.stream.Collectors;

public class WorkerRegistry {
    private List<Worker> allWorkers = new ArrayList<>();

//    zarzadzanie pracownikami

    public void addWorker(Worker worker) {
        boolean exists = allWorkers.stream().anyMatch(w -> w.getEmail().equals(worker.getEmail()));
        if (exists) {
            System.out.println("Pracownik z mailem " + worker.getEmail() + " już istnieje!");
        } else {
            allWorkers.add(worker);
            worker.getCompanyObject().addWorker(worker);
        }
    }

    public void removeWorker(Worker worker) {
        allWorkers.remove(worker);
        worker.getCompanyObject().removeWorker(worker);
    }

    public List<Worker> getAllWorkers() {
        return allWorkers;
    }


//    statystyki analityczne

    public static List<Worker> findByCompany(String companyName, List<Worker> workers) {
        return workers.stream()
                .filter(worker -> worker.getCompany().equals(companyName))
                .collect(Collectors.toList());
    }

    public static List<Worker> sortBySurname(List<Worker> workers) {
        return workers.stream()
                .sorted((w1, w2) -> {
                    String surname1 = w1.getSurname();
                    String surname2 = w2.getSurname();
                    return surname1.compareTo(surname2);
                })
                .collect(Collectors.toList());
    }

    public static Map<Position, List<Worker>> groupByPosition(List<Worker> workers) {
        return workers.stream()
                .collect(Collectors.groupingBy(Worker::getPosition));
    }

    public static Map<Position, Long> countByPosition(List<Worker> workers) {
        return workers.stream()
                .collect(Collectors.groupingBy(Worker::getPosition, Collectors.counting()));
    }

//    statystyki finansowe

    public static double averageSalary(List<Worker> workers) {
        return workers.stream()
                .mapToDouble(Worker::getSalary)
                .average()
                .orElse(0.0);
    }

    public static Optional<Worker> highestPaid(List<Worker> workers) {
        return workers.stream()
                .max(Comparator.comparingDouble(Worker::getSalary));
    }

    @Override
    public String toString() {
        return "WorkerRegistry{allWorkers=" + allWorkers + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkerRegistry)) return false;
        WorkerRegistry that = (WorkerRegistry) o;
        return allWorkers.equals(that.allWorkers);
    }

}
