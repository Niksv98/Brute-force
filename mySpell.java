import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Spelling {
    
    private final Map<String, Double> dictionary = new HashMap<String, Double>();
    
    List<Character> charList = new ArrayList<Character>();
    String charString = "aābcčdeēfgģhiījkķlļmnņoprsštuūvzžwxyqбвгдёжзийлпуфхцчшщъыьэюяäöüß";
    List<String> wordList;
    double wordCount;

    public Spelling(String text) {
        
        for(char c : charString.toCharArray()) {
            charList.add(c);
        }

        text.toLowerCase();
        
        wordList = Arrays.asList(text.split(" "));

        wordCount = wordList.size();
        
        for(String word : wordList){
            double number = Collections.frequency(wordList, word)/wordCount;
            dictionary.put(word, number);
        }
    }

    private final ArrayList<String> edits(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i=0; i < word.length(); ++i)
            result.add(word.substring(0, i) + word.substring(i+1));
        for(int i=0; i < word.length()-1; ++i)
            result.add(word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2));
        for(int i=0; i < word.length(); ++i)
            for(char c : charList)
                result.add(word.substring(0, i) + c + word.substring(i+1));
        for(int i=0; i <= word.length(); ++i)
            for(char c : charList)
                result.add(word.substring(0, i) + c + word.substring(i));
        return result;
    }

    public final ArrayList<String> correct(String word) {
        word.toLowerCase();
        ArrayList<String> list = edits(word);
        Map<String, Double> candidates = new HashMap<String, Double>();
        ValueComparator map_vc = new ValueComparator(candidates);
        TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(map_vc);
        ArrayList<String> tempList = new ArrayList<String>();
        ArrayList<String> result = new ArrayList<String>();
           
        for(String s : list){
            if(dictionary.containsKey(s))
                candidates.put(s, dictionary.get(s));
            for(String w : edits(s))
                if(dictionary.containsKey(w))
                    candidates.put(w, dictionary.get(w));
        }
        
        sorted_map.putAll(candidates);
        tempList.addAll(sorted_map.keySet());

        if(tempList.size() > 0) {
            if(tempList.size() > 3){
                for (int i = 0; i < 3; i++) {
                    result.add(tempList.get(i));
                }
                return result;
            }
            else{
                for(String a : tempList){
                    result.add(a);
                }
                return result;
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        String file = "termini.txt";

        File f = new File(file);
        FileReader in = new FileReader(f);
        char[] buffer = new char[(int)f.length()];
        in.read(buffer);

        String text = new String(buffer);
        text.toLowerCase();
        in.close();

        Spelling spellObj = new Spelling(text);
    
        ArrayList<String> wordsToCorrect = new ArrayList<>();
        wordsToCorrect.add("lapņa");
        wordsToCorrect.add("dilless");
        wordsToCorrect.add("greibfrūts");
        wordsToCorrect.add("bezdelgīactiņa");
        wordsToCorrect.add("knapwed");
        wordsToCorrect.add("sugaar");
        wordsToCorrect.add("coriandel");
        wordsToCorrect.add("balckberry");

        long start;
        long end;

        for(String word : wordsToCorrect){
            System.out.println("\n-----> vards: " + word);
            float all_time = 0;
            for(int i = 0; i < 5; i++){
                ArrayList<String> suggestions = new ArrayList<>();
                start = System.currentTimeMillis();
                suggestions = spellObj.correct(word);
                end = System.currentTimeMillis();
                float spell_time =  (end - start) / 1000F;
                all_time += spell_time;
                if(i == 0){
                    System.out.println("labojumi: " + suggestions);
                    System.out.println("laiks: ");
                }
                System.out.println(spell_time);
                if(i == 4){
                    System.out.println("\nThe average time to spell " + word + " is " + (all_time/5));
                }
            }
        }
    }
}