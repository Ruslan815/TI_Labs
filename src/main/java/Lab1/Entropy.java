package Lab1;

import java.io.*;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class Entropy {
    private static int charCount; // Количество символов в файле

    public static float Shanon(String file, int strLen) throws IOException {
        LinkedHashMap<String, Float> H = calculateCharsFrequency(file, strLen); // Entropy = H

        float sum = 0;
        for (String i : H.keySet()) {
            float characterProbability = H.get(i) / charCount;
            H.put(i, (-1) * characterProbability * log2(characterProbability));
            sum += H.get(i);
        }
        if (strLen > 1) {
            sum /= strLen;
        }

        System.out.println("Unique symbols count = " + H.size());
        //System.out.println(H.entrySet());

        return sum;
    }

    public static LinkedHashMap<String, Float> calculateCharsFrequency(String file, int strLen) throws IOException {
        File readFile = new File(file);
        FileInputStream fin = new FileInputStream(readFile);
        BufferedReader buf = new BufferedReader(new InputStreamReader((fin)));
        String str = buf.readLine();

        TreeMap<String, Float> P = new TreeMap<>();
        LinkedHashMap<String, Float> sortedMap = new LinkedHashMap<>();

        charCount = str.length();

        for (int i = 0; i <= charCount - strLen; ++i) {
            String sub = str.substring(i, i + strLen);
            if (P.containsKey(sub)) {
                P.put(sub, P.get(sub) + 1f);
            } else {
                P.put(sub, 1f);
            }
        }

        // сортировка мапы по значению (по убыванию)
        P.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        return sortedMap;
    }

    public static float log2(float f) {
        return (float) (Math.log(f) / Math.log(2.0));
    }

    public static int getCharCount() {
        return charCount;
    }
}
