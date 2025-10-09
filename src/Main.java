import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        // tworzenie systemu rejestru pracowników
        WorkerRegistry registry = new WorkerRegistry();

        // firmy
        Company comp1 = new Company("ABC Corp", "123-456-78-90", new ArrayList<>());
        Company comp2 = new Company("TechCorp", "999-888-77-66", new ArrayList<>());

        // pracownicy
        Worker w1 = new Worker("Jan", "Kowalski", "jan.kowalski@gmail.com", Position.MANAGER, comp1);
        Worker w2 = new Worker("Anna", "Nowak", "anna.nowak@gmail.com", Position.PROGRAMISTA, comp1);
        Worker w3 = new Worker("Piotr", "Zając", "piotr.zajac@gmail.com", Position.STAZYSTA, comp1);
        Worker w4 = new Worker("Ewa", "Malinowska", "ewa.malinowska@gmail.com", Position.PREZES, comp2);
        Worker w5 = new Worker("Tomasz", "Wiśniewski", "tomasz.wisniewski@gmail.com", Position.WICEPREZES, comp2);
        Worker w6 = new Worker("Karolina", "Maj", "karolina.maj@gmail.com", Position.PROGRAMISTA, comp2);

        registry.addWorker(w1);
        registry.addWorker(w2);
        registry.addWorker(w3);
        registry.addWorker(w4);
        registry.addWorker(w5);
        registry.addWorker(w6);


        System.out.println("\n=== Wszyscy pracownicy w systemie ===");
        registry.getAllWorkers().forEach(System.out::println);

        //  Operacje analityczne dla systemu
        System.out.println("\n=== Pracownicy z firmy ABC Corp ===");
        WorkerRegistry.findByCompany("ABC Corp", registry.getAllWorkers()).forEach(System.out::println);

        System.out.println("\n=== Posortowani alfabetycznie po nazwisku (globalnie) ===");
        WorkerRegistry.sortBySurname(registry.getAllWorkers()).forEach(System.out::println);

        System.out.println("\n=== Grupowanie po stanowisku (globalnie) ===");
        WorkerRegistry.groupByPosition(registry.getAllWorkers()).forEach((position, workers) -> {
            System.out.println(position + ":");
            workers.forEach(worker -> System.out.println("  " + worker));
        });

        System.out.println("\n=== Liczba pracowników na każdym stanowisku(globalnie) ===");
        System.out.println(WorkerRegistry.countByPosition(registry.getAllWorkers()));

        // Operacje finansowe dla systemu
        System.out.println("\n=== Średnie wynagrodzenie w całym systemie ===");
        System.out.printf("%.2f PLN%n", WorkerRegistry.averageSalary(registry.getAllWorkers()));

        System.out.println("\n=== Pracownik z najwyższym wynagrodzeniem (globalnie) ===");
        WorkerRegistry.highestPaid(registry.getAllWorkers()).ifPresent(System.out::println);

        //Statystyki dla firm
        System.out.println("\n=== Średnia pensja w firmie TechCorp ===");
        System.out.printf("%.2f PLN%n", WorkerRegistry.averageSalary(comp2.getWorkers()));

        System.out.println("\n=== Najlepiej zarabiający w firmie ABC Corp ===");
        WorkerRegistry.highestPaid(comp1.getWorkers()).ifPresent(System.out::println);
    }
}
