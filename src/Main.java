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
