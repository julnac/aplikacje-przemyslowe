import model.Position;
import model.Worker;
import service.WorkerRegistry;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        // tworzenie systemu rejestru pracowników
        WorkerRegistry registry = new WorkerRegistry();

        // pracownicy
        Worker w1 = new Worker("Jan", "Kowalski", "jan.kowalski@gmail.com", Position.MANAGER, "ABC Corp");
        Worker w2 = new Worker("Anna", "Nowak", "anna.nowak@gmail.com", Position.PROGRAMISTA, "ABC Corp");
        Worker w3 = new Worker("Piotr", "Zając", "piotr.zajac@gmail.com", Position.STAZYSTA, "ABC Corp");
        Worker w4 = new Worker("Ewa", "Malinowska", "ewa.malinowska@gmail.com", Position.PREZES, "TechCorp");
        Worker w5 = new Worker("Tomasz", "Wiśniewski", "tomasz.wisniewski@gmail.com", Position.WICEPREZES, "TechCorp");
        Worker w6 = new Worker("Karolina", "Maj", "karolina.maj@gmail.com", Position.PROGRAMISTA, "TechCorp");

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
        var workers_from_techCorp = WorkerRegistry.findByCompany("TechCorp", registry.getAllWorkers());
        System.out.printf("%.2f PLN%n", WorkerRegistry.averageSalary(workers_from_techCorp));

        System.out.println("\n=== Najlepiej zarabiający w firmie TechCorp ===");
        WorkerRegistry.highestPaid(workers_from_techCorp).ifPresent(System.out::println);
    }
}
