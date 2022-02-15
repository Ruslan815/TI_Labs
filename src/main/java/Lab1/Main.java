package Lab1;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String file1 = "first.txt";
        String file2 = "second.txt";
        int textSizeInChars = 10_250;
        char[] uniqueChars = new char[]{'1', '2', '3', '4', '5'};
        int[] p1 = new int[]{20, 20, 20, 20, 20};
        int[] p2 = new int[]{30, 30, 20, 15, 5}; // 0,3*1.736 + 0,3*1.736 + 0,2*2.322 + 0,15*2.736 + 0,05*4.322 = 1,0416 + 0,4644 + 0,4104 + 0,2161 = 2,1325

        FileGenerator.generateFile(file1, uniqueChars, p1, textSizeInChars);
        FileGenerator.generateFile(file2, uniqueChars, p2, textSizeInChars);

        //displayLab1();
        displayLab2();
    }

    public static void displayLab1() throws IOException {
        System.out.println("Экспериментальная оценка энтропии (частоты отдельных символов):");
        System.out.print("Файл №1: ");
        System.out.println(Entropy.Shanon("first.txt", 1));
        System.out.print("Файл №2: ");
        System.out.println(Entropy.Shanon("second.txt", 1));
        System.out.println();

        System.out.println("Экспериментальная оценка энтропии (частоты пар символов):");
        System.out.print("Файл №1: ");
        System.out.println(Entropy.Shanon("first.txt", 2));
        System.out.print("Файл №2: ");
        System.out.println(Entropy.Shanon("second.txt", 2));
    }

    public static void displayLab2() throws IOException {
        System.out.println("\nЛаб.раб. №2:");
        FileGenerator.generateText("JaneEyre.txt");
        FileGenerator.generateText("lab1Code.txt");

        System.out.println("JaneEyre\n"
                + "1 char = " + Entropy.Shanon("JaneEyre.txt.new", 1) + "\n"
                + "2 char = " + Entropy.Shanon("JaneEyre.txt.new", 2) + " \n"
                + "3 char = " + Entropy.Shanon("JaneEyre.txt.new", 3) + " \n");
        System.out.println("lab1Code\n"
                + "1 char = " + Entropy.Shanon("lab1Code.txt.new", 1) + " \n"
                + "2 char = " + Entropy.Shanon("lab1Code.txt.new", 2) + " \n"
                + "3 char = " + Entropy.Shanon("lab1Code.txt.new", 3) + " \n");
    }
}
