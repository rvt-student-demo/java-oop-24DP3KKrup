package rvt;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class Studentu {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.start();
    }
}

class Student {
    private String firstName;
    private String lastName;
    private String email;
    private String personalId;
    private LocalDateTime registrationDate;

    public Student(String firstName, String lastName, String email, String personalId, LocalDateTime registrationDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.personalId = personalId;
        this.registrationDate = registrationDate;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPersonalId() { return personalId; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }

    public String toCSV() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.join(",", firstName, lastName, email, personalId, registrationDate.format(formatter));
    }

    public static Student fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length == 5) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return new Student(parts[0], parts[1], parts[2], parts[3], LocalDateTime.parse(parts[4], formatter));
        }
        return null;
    }
}

class FileHandler {
    
    // Pārbauda, no kurienes programma tiek palaista, lai vienmēr atrastu pareizo "data" mapi
    private String getFilePath() {
        String path = "data/students.csv";
        // Ja programmu palaiž no VSCode ārējā workspace mapes (Projects), mēs pieliekam klāt projekta mapes nosaukumu
        if (new File("java-oop-24DP3KKrup").exists()) {
            path = "java-oop-24DP3KKrup/data/students.csv";
        }
        return path;
    }

    public List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        File file = new File(getFilePath());
        if (!file.exists()) return students;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Student s = Student.fromCSV(line);
                if (s != null) students.add(s);
            }
        } catch (IOException e) {
            System.out.println("Kluda lasot failu: " + e.getMessage());
        }
        return students;
    }

    public void saveStudents(List<Student> students) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFilePath()))) {
            for (Student s : students) {
                bw.write(s.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Kluda saglabajot failu: " + e.getMessage());
        }
    }
}

class ConsoleUI {
    private Scanner scanner = new Scanner(System.in);
    private FileHandler fileHandler = new FileHandler();
    private List<Student> students;

    public ConsoleUI() {
        students = fileHandler.loadStudents();
    }

    public void start() {
        while (true) {
            System.out.println("\nIzvelieties darbibu: register, show, remove, edit, exit");
            System.out.print("> ");
            String command = scanner.nextLine().trim().toLowerCase();

            try {
                switch (command) {
                    case "register":
                        registerStudent();
                        break;
                    case "show":
                        showStudents();
                        break;
                    case "remove":
                        removeStudent();
                        break;
                    case "edit":
                        editStudent();
                        break;
                    case "exit":
                        System.out.println("Programma aptureta.");
                        return;
                    default:
                        System.out.println("Nezinama komanda.");
                }
            } catch (Exception e) {
                System.out.println("Kluda: " + e.getMessage());
            }
        }
    }

    private void registerStudent() {
        String firstName = readAndValidate("Vards", "^[a-zA-Z]{3,}$", "Vardam jasatur tikai burti un jabut vismaz 3 simbolus garam.");
        String lastName = readAndValidate("Uzvards", "^[a-zA-Z]{3,}$", "Uzvardam jasatur tikai burti un jabut vismaz 3 simbolus garam.");
        String email = readAndValidate("E-pasts", "^[A-Za-z0-9+_.-]+@(.+)$", "Nepareizs e-pasta formats.");

        if (students.stream().anyMatch(s -> s.getEmail().equalsIgnoreCase(email))) {
            throw new IllegalArgumentException("Sads e-pasts jau ir registrets.");
        }

        String personalId = readAndValidate("Personas kods (XXXXXX-XXXXX)", "^\\d{6}-\\d{5}$", "Nepareizs personas koda formats.");

        if (students.stream().anyMatch(s -> s.getPersonalId().equals(personalId))) {
            throw new IllegalArgumentException("Sads personas kods jau eksiste.");
        }

        Student student = new Student(firstName, lastName, email, personalId, LocalDateTime.now());
        students.add(student);
        fileHandler.saveStudents(students);
        System.out.println("Students veiksmigi registrets!");
    }

    private void showStudents() {
        if (students.isEmpty()) {
            System.out.println("Nav registretu studentu.");
            return;
        }

        String format = "| %-15s | %-15s | %-25s | %-15s | %-20s |%n";
        String border = "+-----------------+-----------------+---------------------------+-----------------+----------------------+";

        System.out.println(border);
        System.out.printf(format, "Vards", "Uzvards", "E-pasts", "Personas kods", "Registracijas datums");
        System.out.println(border);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Student s : students) {
            System.out.printf(format, s.getFirstName(), s.getLastName(), s.getEmail(), s.getPersonalId(), s.getRegistrationDate().format(dtf));      
        }
        System.out.println(border);
    }

    private void removeStudent() {
        System.out.print("Ievadiet personas kodu dzesanai: ");
        String personalId = scanner.nextLine().trim();

        boolean removed = students.removeIf(s -> s.getPersonalId().equals(personalId));
        if (removed) {
            fileHandler.saveStudents(students);
            System.out.println("Students veiksmigi izdzests.");
        } else {
            System.out.println("Students ar sadu personas kodu netika atrasts.");
        }
    }

    private void editStudent() {
        System.out.print("Ievadiet personas kodu redigesanai: ");
        String personalId = scanner.nextLine().trim();

        Student student = students.stream()
                .filter(s -> s.getPersonalId().equals(personalId))
                .findFirst()
                .orElse(null);

        if (student == null) {
            System.out.println("Students ar sadu personas kodu netika atrasts.");
            return;
        }

        System.out.println("Ievadiet jaunus datus (atstajiet tuksu, lai nemainitu):");

        System.out.print("Jauns vards (" + student.getFirstName() + "): ");
        String newFirstName = scanner.nextLine().trim();
        if (!newFirstName.isEmpty()) {
            if (Pattern.matches("^[a-zA-Z]{3,}$", newFirstName)) {
                student.setFirstName(newFirstName);
            } else {
                System.out.println("Kluda: Vardam jasatur tikai burti un jabut vismaz 3 simbolus garam. Vards netika mainits.");
            }
        }

        System.out.print("Jauns uzvards (" + student.getLastName() + "): ");
        String newLastName = scanner.nextLine().trim();
        if (!newLastName.isEmpty()) {
             if (Pattern.matches("^[a-zA-Z]{3,}$", newLastName)) {
                student.setLastName(newLastName);
            } else {
                System.out.println("Kluda: Uzvardam jasatur tikai burti un jabut vismaz 3 simbolus garam. Uzvards netika mainits.");
            }
        }

        System.out.print("Jauns e-pasts (" + student.getEmail() + "): ");
        String newEmail = scanner.nextLine().trim();
        if (!newEmail.isEmpty()) {
            if (Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", newEmail)) {
                if (students.stream().anyMatch(s -> !s.getPersonalId().equals(personalId) && s.getEmail().equalsIgnoreCase(newEmail))) {
                    System.out.println("Kluda: Sads e-pasts jau ir registrets citam studentam.");
                } else {
                    student.setEmail(newEmail);
                }
            } else {
                System.out.println("Kluda: Nepareizs e-pasta formats.");
            }
        }

        fileHandler.saveStudents(students);
        System.out.println("Studenta dati atjauninati.");
    }

    private String readAndValidate(String prompt, String regex, String errorMessage) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();
            if (Pattern.matches(regex, input)) {
                return input;
            }
            System.out.println("Kluda: " + errorMessage);
        }
    }
}
