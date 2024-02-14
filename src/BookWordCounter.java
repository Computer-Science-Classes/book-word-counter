import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Counts the frequency of words in a book.
 */
public class BookWordCounter {
     private static final float LOAD_FACTOR = 0.75f;
    /**
     * Continuously asks the user for a book and a word
     *
     * @param args
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String bookName;
        String bookFilePath;
        boolean fileExists;
        // String bookName = "hamlet";
        // String bookFilePath = Paths.get("src/books", bookName + ".txt").toString();
        // boolean fileExists = Files.exists(Paths.get(bookFilePath));

        while (true) {
            do {
                System.out.println("Enter the name of the book (or 'quit' to stop):");
                bookName = sc.nextLine();

                if ("quit".equalsIgnoreCase(bookName)) {
                    sc.close();
                    return;
                }

                bookFilePath = Paths.get("src/books", bookName + ".txt").toString();
                fileExists = Files.exists(Paths.get(bookFilePath));

                if (!fileExists) {
                    System.out.println("The book '" + bookName + "' does not exist. Please try again.");
                }
            } while (!fileExists);
        
            Map<String, Integer> wordCountMap = createWordCountMap(bookFilePath);
            printHashMapDetails(wordCountMap);

            String word;
            do {
                System.out.println("Enter a word to search for, 'back' to choose another book, or 'quit' to stop:");
                word = sc.nextLine();

                if ("quit".equalsIgnoreCase(word)) {
                    sc.close();
                    return;
                }

                if ("back".equalsIgnoreCase(word)) {
                    break;
                }

                int count = wordCountMap.getOrDefault(word, 0);
                System.out.println("The word '" + word + "' appears " + count + " times.");
            } while (true);
        }
    }
    

    private static void printHashMapDetails(Map<String, Integer> map) {
        if (!(map instanceof HashMap)) {
            System.out.println("The provided map is not an instance of HashMap.");
            return;
        }
    
        try {
            HashMap<?, ?> hashMap = (HashMap<?, ?>) map;
            Class<?> hashMapClass = HashMap.class;
            Field tableField = hashMapClass.getDeclaredField("table");
            tableField.setAccessible(true);
            Object[] table = (Object[]) tableField.get(hashMap);
    
            int bucketCount = table != null ? table.length : 0;
            int size = hashMap.size();
            float loadFactor = LOAD_FACTOR;
            float memoryEfficiency = (float) size / (bucketCount * loadFactor);
    
            System.out.println("Number of buckets: " + bucketCount);
            System.out.println("Load factor: " + loadFactor);
            System.out.println("Current size: " + size);
            System.out.println("Memory efficiency): " + memoryEfficiency);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    /**
     * Creates a map of word counts from the book
     *
     * @param filePath The path to the book file
     * @return a map of words to their counts in the book
     */
    private static Map<String, Integer> createWordCountMap(String filePath) {
        Map<String, Integer> wordCountMap = new HashMap<>();
        try {
            Files.lines(Paths.get(filePath))
                    .map(line -> line.split("\\s+"))
                    .flatMap(Arrays::stream)
                    .forEach(word -> wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordCountMap;
    }
}
