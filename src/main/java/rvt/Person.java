package rvt;

import java.util.ArrayList;

public class Person {
    private String name;
    private String address;

    public Person(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String toString() {
        return name + "\n  " + address;
    }

    public static class Student extends Person {
        private int studyCredits;

        public Student(String name, String address) {
            super(name, address);
            this.studyCredits = 0;
        }

        public void study() {
            studyCredits++;
        }

        public int credits() {
            return studyCredits;
        }

        @Override
        public String toString() {
            return super.toString() + "\n  Study credits " + studyCredits;
        }
    }

    public static class Teacher extends Person {
        private int salary;

        public Teacher(String name, String address, int salary) {
            super(name, address);
            this.salary = salary;
        }

        @Override
        public String toString() {
            return super.toString() + "\n  salary " + salary + " euro/month";
        }
    }

    public static void printPersons(ArrayList<Person> persons) {
        for (Person p : persons) {
            System.out.println(p);
        }
    }

    public static void main(String[] args) {
        Teacher ada = new Teacher("Ada Lovelace", "24 Maddox St. London W1S 2QN", 1200);
        Teacher esko = new Teacher("Esko Ukkonen", "Mannerheimintie 15 00100 Helsinki", 5400);
        System.out.println(ada);
        System.out.println(esko);

        Student ollie = new Student("Ollie", "6381 Hollywood Blvd. Los Angeles 90028");
        int i = 0;
        while (i < 25) {
            ollie.study();
            i++;
        }
        System.out.println(ollie);

        ArrayList<Person> persons = new ArrayList<>();
        persons.add(new Teacher("Ada Lovelace", "24 Maddox St. London W1S 2QN", 1200));
        persons.add(new Student("Ollie", "6381 Hollywood Blvd. Los Angeles 90028"));

        printPersons(persons);
    }
}
