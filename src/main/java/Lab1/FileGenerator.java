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
}
