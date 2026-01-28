package rvt;
import java.util.Scanner;
class  ToDoList {
    private String[] Uzdevumi;
    private int skaits;
    public ToDoList() {
        this.Uzdevumi = new String[10];
        this.skaits = 0;
    }
    public void add(String string) {
        if (skaits < Uzdevumi.length) {
            Uzdevumi[skaits] = string;
            skaits++;
        }
    }
    public void print() {
        for (int i = 0; i < skaits; i++) {
            System.out.println((i + 1) + ": " + Uzdevumi[i]);
        }
    }
    public void remove(int indekss) {
        if (indekss < 1 || indekss > skaits) {
            return;
        }
        for (int i = indekss - 1; i < skaits - 1; i++) {
            Uzdevumi[i] = Uzdevumi[i + 1];
        }
        Uzdevumi[skaits - 1] = null;
        skaits--;
        System.out.println(indekss + ". Uzdevums tika izdzests!");
    }
}
public class ToDo {
    public static void main(String[] args) {
    Scanner skeneris = new Scanner(System.in);
    ToDoList mansUzdevumuSaraksts = new ToDoList();
    String ievade;
    while (true) {
        System.out.println("Ievadiet uzdevumu (vai 'izdrukāt', 'dzēst', 'iziet'):");
        ievade = skeneris.nextLine();
        if (ievade.equals("ievadit")) {
            System.out.println("Ievadiet uzdevuma aprakstu:");
            String apraksts = skeneris.nextLine();
            mansUzdevumuSaraksts.add(apraksts);
        } else if (ievade.equals("izdrukāt")) {
            mansUzdevumuSaraksts.print();
        } else if (ievade.equals("dzēst")) {
            System.out.println("Ievadiet dzēšamā uzdevuma numuru:");
            int numurs = Integer.parseInt(skeneris.nextLine());
            mansUzdevumuSaraksts.remove(numurs);
        } else if (ievade.equals("iziet")) {
            System.out.println("Iziet no programmas.");
            break;
        } else {
            System.out.println("tada darbiba neeksiste.");
        }
        skeneris.close();
    }
}
}

