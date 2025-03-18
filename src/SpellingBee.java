import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
// Spelling Bee by Zander Deutch

/**
 * Spelling Bee
 * <p>
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 * <p>
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 * <p>
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 * <p>
 * Written on March 5, 2023 for CS2 @ Menlo School
 * <p>
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    public ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        words = generateHelper("", letters);

    }

    public ArrayList<String> generateHelper(String s, String letters) {
        ArrayList<String> helper = new ArrayList<String>();
        if (letters.isEmpty()) {
            if(s.isEmpty()) {
                return helper;
            } else {
                helper.add(s);
                return helper;
            }
        }
        for (int i = 0; i < letters.length(); i++) {
            ArrayList<String> tempList = generateHelper(s + letters.charAt(i), letters.substring(0, i) + letters.substring(i + 1));
            for (int j = 0; j < tempList.size(); j++) {
                helper.add(tempList.get(j));
            }
        }
        if(!s.isEmpty()) {
            helper.add(s);
        }
        return helper;
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        words = mergeSort(words, 0, words.size() - 1);
    }

    public ArrayList<String> mergeSort(ArrayList<String> arr, int low, int high) {
        if(high - low == 0) {
            ArrayList<String> sorted = new ArrayList<String>();
            sorted.add(arr.get(low));
            return sorted;
        }

        int mid = (high + low) / 2;

        ArrayList<String> arr1 = mergeSort(arr, low, mid);
        ArrayList<String> arr2 = mergeSort(arr, mid + 1, high);

        return merge(arr1, arr2);
    }

    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2) {
        ArrayList<String> merged = new ArrayList<String>();

        int i = 0;
        int j = 0;

        while(i < arr1.size() && j < arr2.size()) {
            if(arr1.get(i).compareTo(arr2.get(j)) <= 0) {
                merged.add(arr1.get(i));
                i++;
            } else {
                merged.add(arr2.get(j));
                j++;
            }
        }

        while(i < arr1.size()) {
            merged.add(arr1.get(i));
            i++;
        }

        while(j < arr2.size()) {
            merged.add(arr2.get(j));
            j++;
        }

        return merged;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while (s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        System.out.println(sb.words);
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
