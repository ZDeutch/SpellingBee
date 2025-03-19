import java.io.*;
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

    private final String letters;
    public ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<>();
    }

    public void generate() {
        // Call helper method
        words = generateHelper("", letters);

    }

    // This method returns an ArrayList of all possible words
    public ArrayList<String> generateHelper(String s, String letters) {

        // Make helper ArrayList to put words into
        ArrayList<String> helper = new ArrayList<>();

        // If there are no letters
        if (letters.isEmpty()) {
            if (s.isEmpty()) {
                // Return the ArrayList
            } else {
                // Otherwise add string to ArrayList
                helper.add(s);
            }
            return helper;
        }

        // For every letter in letters
        for (int i = 0; i < letters.length(); i++) {
            // Do another recursive call
            // Shifting each letter into string and removing that letter from letters
            ArrayList<String> tempList = generateHelper(s + letters.charAt(i), letters.substring(0, i) + letters.substring(i + 1));
            // Add all of these words into helper
            helper.addAll(tempList);
        }

        // Final check if the letters are empty
        if (!s.isEmpty()) {
            // Otherwise add to helper
            helper.add(s);
        }
        return helper;
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // Call to helper method
        words = mergeSort(words, 0, words.size() - 1);
    }

    // This method returns all the possible strings in a sorted alphabetical list
    public ArrayList<String> mergeSort(ArrayList<String> arr, int low, int high) {

        // Base case if high and low are the same
        if (high - low == 0) {

            // Use temp variable to return the single element
            ArrayList<String> arr_sorted = new ArrayList<>();
            arr_sorted.add(arr.get(low));
            return arr_sorted;
        }

        // Identify midpoint in array using average
        int mid = (high + low) / 2;

        // Recursively split each array until each array is a single element
        ArrayList<String> arr1 = mergeSort(arr, low, mid);
        ArrayList<String> arr2 = mergeSort(arr, mid + 1, high);

        // Finally merge the 2 divided arrays together
        return merge(arr1, arr2);
    }

    // This method merges the two ArrayLists until fully sorted in alphabetical order
    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2) {
        // Create ArrayList to move merged words into
        ArrayList<String> arr_merged = new ArrayList<>();

        // Indexes to track the index of each arrayList
        int i = 0;
        int j = 0;

        // Until one ArrayList runs out
        while (i < arr1.size() && j < arr2.size()) {

            // Add the first ArrayList to the merged ArrayList if letter is earlier in alphabet
            if (arr1.get(i).compareTo(arr2.get(j)) <= 0) {
                arr_merged.add(arr1.get(i));
                i++;
            } else {
                // Otherwise add the second ArrayList
                arr_merged.add(arr2.get(j));
                j++;
            }
        }

        // Fill merged ArrayList with remaining elements
        while (i < arr1.size()) {
            arr_merged.add(arr1.get(i));
            i++;
        }

        while (j < arr2.size()) {
            arr_merged.add(arr2.get(j));
            j++;
        }

        // Return merged ArrayList
        return arr_merged;
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

    public void checkWords() {
        // For each word, see whether it's a duplicate and remove by calling found method
        for (int i = 0; i < words.size(); i++) {
            if (!found(words.get(i), 0, DICTIONARY_SIZE - 1)) {
                words.remove(i);
                i--;
            }
        }
    }

    // This method checks whether a certain word is already in dictionary
    // Using Binary search
    public boolean found(String str, int first, int last) {
        // Determine the midpoint by adding the first index by the average
        int middle = first + (last - first) / 2;

        // Base case if first and last index are the same
        if (last - first == 0) {
            return false;
        }

        // If the word in the dictionary is same as the string, then it is found
        if (DICTIONARY[middle].equals(str)) {
            return true;
        }

        // If the string is alphabetically before the midpoint
        if (str.compareTo(DICTIONARY[middle]) < 0) {
            // Then do another call with the earlier half the array
            return found(str, first, middle);
        } else {
            // Otherwise do another call with the later half of the array
            return found(str, middle + 1, last);
        }

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
