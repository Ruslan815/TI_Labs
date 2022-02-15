package Lab1;

import java.io.*;
import java.util.Random;

public class FileGenerator {
    public static void generateFile(String fileName, char[] uniqueChars, int[] probs, int textSizeInChars) throws IOException {
        File file = new File(fileName);
        FileOutputStream fileOutStream = new FileOutputStream(file);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < textSizeInChars; i++) {
            sb.append(uniqueChars[getRandomIndex(probs)]);
        }

        fileOutStream.write(sb.toString().getBytes());
        fileOutStream.close();
    }

    static int getRandomIndex(int[] probs) {
        Random rnd = new Random();
        int randomPoint = rnd.nextInt(100);

        for (int i = 0; i < probs.length; i++) {
            if (randomPoint < probs[i]) {
                return i;
            } else {
                randomPoint -= probs[i];
            }
        }
        return probs.length - 1;
    }

    public static String removeChar(String s, char c) {
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c) r.append(s.charAt(i));
        }
        return r.toString();
    }

    public static void generateText(String filename) throws IOException {
        File fileIn = new File(filename);
        File fileOut = new File(filename + ".new");
        FileInputStream fin = new FileInputStream(fileIn);
        FileOutputStream fout = new FileOutputStream(fileOut);
        BufferedReader buf = new BufferedReader(new InputStreamReader(fin));
        String str = "";

        while ((str = buf.readLine()) != null) {
            str = str.trim();
            str = str.toLowerCase();
            str = str + " ";

            StringBuilder newStr = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                char tempSymbol = str.charAt(i);
                if (Character.isSpaceChar(tempSymbol) || Character.isLetter(tempSymbol)) {
                    newStr.append(tempSymbol);
                }
            }

            fout.write(newStr.toString().getBytes());
        }
        fout.close();
    }
}
