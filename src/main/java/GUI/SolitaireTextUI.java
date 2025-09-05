package GUI;

import solitaire.SolitaireGame;

import java.util.Scanner;

public class SolitaireTextUI {
    SolitaireGame sg;

    public SolitaireTextUI() {
        sg = new SolitaireGame();
    }

    public void playGame() {
        int tableauFuente;
        int tableauDestino;
        Scanner sc = new Scanner(System.in);

        // mostrar estado actual del juego
        System.out.println(sg);
        menu();
        System.out.println("Que operación quieres hacer?");

        String op = sc.nextLine();
        op = op.toUpperCase();
        while (!op.equals("Q") && !sg.isGameOver()) {
            switch (op) {
                case "A":
                    sg.moveWasteToFoundation();
                    break;
                case "B":
                    sg.drawCards();
                    break;
                case "C":
                    sg.reloadDrawPile();
                    break;
                case "D": // D) Move Tableau to Foundation
                    System.out.println("Escribe el número del Tableau (1-7) de donde se toma la carta.");
                    tableauFuente = getTableauNumber();
                    if (tableauFuente > 0 && tableauFuente <= 7) {
                        sg.moveTableauToFoundation(tableauFuente);
                    }
                    break;
                case "E": // E) Move Tableau to Tableau
                    System.out.println("Escribe el número del Tableau (1-7) donde se toma la carta. ");
                    tableauFuente = getTableauNumber();
                    if (tableauFuente > 0 && tableauFuente <= 7) {
                        System.out.println("Escribe el número del Tableau (1-7) donde se colocará. ");
                        tableauDestino = getTableauNumber();
                        if (tableauDestino > 0 && tableauDestino <= 7) {
                            sg.moveTableauToTableau(tableauFuente, tableauDestino);
                        }
                    }
                    break;
                case "F": // F) Move Waste Pile to Tableau
                    System.out.println("Escribe el número del Tableau (1-7) donde se colocará. ");
                    tableauDestino = getTableauNumber();
                    if (tableauDestino > 0 && tableauDestino <= 7) {
                        sg.moveWasteToTableau(tableauDestino);
                    }
                    break;
                default:
                    System.out.println("Operación desconocida");
            }
            System.out.println(sg);

            menu();
            System.out.println("Que operación quieres hacer?");
            op = sc.nextLine();
            op = op.toUpperCase();
        }
        if (sg.isGameOver()) {
            System.out.println("GAME OVER");
        }
    }

    private void menu() {
        System.out.println("A) Waste Pile to Foundation");
        System.out.println("B) Draw Cards.");
        System.out.println("C) Reload Draw Pile");
        System.out.println("D) Move Tableau to Foundation");
        System.out.println("E) Move Tableau to Tableau");
        System.out.println("F) Move Waste Pile to Tableau");
        System.out.println("Q) Salir.");
    }

    private int getTableauNumber() {
        Scanner sc = new Scanner(System.in);
        int tableauNumber = 0;
        if (sc.hasNextInt()) {
            tableauNumber = sc.nextInt();
        }
        return tableauNumber;
    }

}
