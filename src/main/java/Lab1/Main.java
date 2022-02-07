package Lab1;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String file1 = "first.txt";
        String file2 = "second.txt";
        int textSizeInChars = 10_250;
        char[] uniqueChars = new char[]{'1', '2', '3', '4', '5'};
        int[] p1 = new int[]{20, 20, 20, 20, 20};
        int[] p2 = new int[]{30, 30, 20, 15, 5};

        FileGenerator.generateFile(file1, uniqueChars, p1, textSizeInChars);
        FileGenerator.generateFile(file2, uniqueChars, p2, textSizeInChars);

        displayLab1();
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
}
