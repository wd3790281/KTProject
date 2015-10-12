package Engineering;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by dingwang on 15/10/7.
 */
public class Normalising {

    private HashMap<String, String> pairs = new HashMap<>();

    public Normalising(){
        getFile();
    }

    public HashMap<String, String> getFile() {

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("emnlp_dict.txt").getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] split = line.split("\t");
                pairs.put(split[0],split[1]);
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return pairs;

    }

    public String nomalising(String word){
        String handledWord = handleWord(word);
        if(pairs.containsKey(handledWord)) {
            String nomalisedWord = pairs.get(handledWord);
            return nomalisedWord;
        }
        else {
            return word;
        }
    }

    private String handleWord(String word){
        StringBuilder handledWord = new StringBuilder("");
        char[] characters = word.toCharArray();
        int count = 0;
        for(int i = 0; i<characters.length-1; i++){
            if(characters[i] == characters[i+1])
                count ++;
            else
                count = 0;
            if(count<=2)
                handledWord.append(characters[i]);
        }
        handledWord.append(characters[characters.length-1]);
        return handledWord.toString();
    }


}
