import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class FileAnalyzing implements Runnable {
    private final String filename;
    private final Map<String, Integer> wordCounts = new HashMap<>();
    private int longestWordLength = 0;
    private int shortestWordLength = Integer.MAX_VALUE;
    public int sumWordsLength = 0;

    public FileAnalyzing(String filename) {
        this.filename = filename;
    }

    public int getSumWordsLength() {
        return sumWordsLength;
    }

    @Override
    public void run() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            for (String word : lines) {
                sumWordsLength += word.length();
                wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
                if (word.length() > longestWordLength) {
                    longestWordLength = word.length();
                }
                if (word.length() < shortestWordLength) {
                    shortestWordLength = word.length();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getWordCounts() {
        return wordCounts;
    }

    public int getLongestWordLength() {
        return longestWordLength;
    }

    public int getShortestWordLength() {
        return shortestWordLength;
    }
}

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        String[] fileNames = {
                "Workshops/Multi_threading_workshop_9/file_1.txt",
                "Workshops/Multi_threading_workshop_9/file_2.txt", "Workshops/Multi_threading_workshop_9/file_3.txt", "Workshops/Multi_threading_workshop_9/file_3.txt", "Workshops/Multi_threading_workshop_9/file_4.txt", "Workshops/Multi_threading_workshop_9/file_5.txt"
                , "Workshops/Multi_threading_workshop_9/file_6.txt", "Workshops/Multi_threading_workshop_9/file_7.txt", "Workshops/Multi_threading_workshop_9/file_8.txt", "Workshops/Multi_threading_workshop_9/file_9.txt", "Workshops/Multi_threading_workshop_9/file_10.txt"
                , "Workshops/Multi_threading_workshop_9/file_10.txt", "Workshops/Multi_threading_workshop_9/file_11.txt", "Workshops/Multi_threading_workshop_9/file_12.txt", "Workshops/Multi_threading_workshop_9/file_13.txt", "Workshops/Multi_threading_workshop_9/file_14.txt", "Workshops/Multi_threading_workshop_9/file_15.txt"
                , "Workshops/Multi_threading_workshop_9/file_16.txt", "Workshops/Multi_threading_workshop_9/file_17.txt", "Workshops/Multi_threading_workshop_9/file_18.txt", "Workshops/Multi_threading_workshop_9/file_19.txt", "Workshops/Multi_threading_workshop_9/file_20.txt"
        };

        Map<String, FileAnalyzing> threads = new HashMap<>();

        for (String filename : fileNames) {
            FileAnalyzing thread = new FileAnalyzing(filename);
            threads.put(filename, thread);
            executor.submit(thread);
        }

        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            Map<String, Integer> totalWordCounts = new HashMap<>();
            int totalLongestWordLength = 0;
            int totalShortestWordLength = Integer.MAX_VALUE;
            int totalWords = 0, v=0;

            for (FileAnalyzing thread : threads.values()) {
                Map<String, Integer> wordCounts = thread.getWordCounts();
                for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
                    totalWordCounts.put(entry.getKey(), totalWordCounts.getOrDefault(entry.getKey(), 0) + entry.getValue());
                }

                int longestWordLength = thread.getLongestWordLength();
                if (longestWordLength > totalLongestWordLength) {
                    totalLongestWordLength = longestWordLength;
                }

                int shortestWordLength = thread.getShortestWordLength();
                if (shortestWordLength < totalShortestWordLength) {
                    totalShortestWordLength = shortestWordLength;
                }

                for (int count : wordCounts.values()) {
                    totalWords += count;
                }
                v+=thread.getSumWordsLength();
            }

            double averageWordLength = v/totalWordCounts.size();

            System.out.println("1) words count: " + totalWordCounts.size());
            System.out.println("2)Longest word length: " + totalLongestWordLength);
            System.out.println("3)Shortest word length: " + totalShortestWordLength);
            System.out.println("4)Average word length: " + (int) averageWordLength);

            List<String> longestWords = new ArrayList<>();
            List<String> shortestWords = new ArrayList<>();

            for (FileAnalyzing thread : threads.values()) {
                Map<String, Integer> wordCounts = thread.getWordCounts();

                for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
                    String word = entry.getKey();
                    int wordLength = word.length();

                    if (wordLength == totalLongestWordLength) {
                        longestWords.add(word);
                    }

                    if (wordLength == totalShortestWordLength) {
                        shortestWords.add(word);
                    }
                }
            }

            System.out.println("Longest word(s):");
            for (String word : longestWords) {
                System.out.println(word);
            }

            System.out.println("\nShortest word(s):");
            for (String word : shortestWords) {
                System.out.println(word);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
