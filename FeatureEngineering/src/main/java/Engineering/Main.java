package Engineering;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import java.io.*;
import java.util.*;


/**
 * Created by dingwang on 15/10/6.
 */
public class Main {

    private LinkedHashSet<String> tweets = new LinkedHashSet<>();
    private LinkedHashSet<String> words = new LinkedHashSet<>();
    private ArrayList<String> positive = new ArrayList<>();
    private String inputPath,outputPath;
    private Normalising normalising = new Normalising();

    public Main(String inputPath,String outputPath){
        this.inputPath =inputPath;
        this.outputPath = outputPath;
    }
    public static void main(String[] args) {

        Main main = new Main(args[0], args[1]);
        Scanner inputStream = null;
        try {
            inputStream = new Scanner(new File(main.inputPath));
            while (inputStream.hasNextLine()) {
                 main.tweets.add(inputStream.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for(String s : main.tweets){
            main.words.addAll(main.getwords(s));
        }

        main.outputArff();

    }

    HashSet<String> getwords(String tweets){
        String[] split = tweets.split("\t");
        String content = split[3].replaceAll("[^a-zA-Z0-9 ]", "");

        String[] w= content.split(" ");



        HashSet<String> words = new HashSet<>();
        for(String s : w) {
            if (s.equals("")) continue;

            PorterStem stemmer = new PorterStem();
            String normedWord = normalising.nomalising(s.toLowerCase());
            if(normedWord == null){
                System.out.println(s);
                break;
            }
            stemmer.add(normedWord);
            stemmer.stem();
//            String stemmedWord = stemmer.toString();

            words.add(stemmer.toString());
        }
        this.positive.add(split[2]);

        return words;
    }

    void outputArff(){
        FastVector atts = new FastVector();
        for(String s:this.words){
            atts.addElement(new Attribute(s));
        }
        FastVector cls = new FastVector();
        cls.addElement("Y");
        cls.addElement("N");
        atts.addElement(new Attribute("dingw.class", cls));


        Instances data = new Instances("Tweets",atts,0);

        int i = 0;

        for(String tweet:this.tweets){
            int j = 0;
            double[] vals = new double[data.numAttributes()];
            HashSet<String> wordsOfTweet = this.getwords(tweet);
            for(String word:this.words){
                if(wordsOfTweet.contains(word)){
                    vals[j] = 1;
                }else {
                    vals[j] = 0;
                }
//                System.out.println(vals[j]);
                j++;
            }
            if(this.positive.get(i).equals("1")){
                vals[j] = cls.indexOf("Y");
            }else{
                vals[j] = cls.indexOf("N");

            }
            i++;
            data.add(new Instance(1.0, vals));
        }



        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(
                    new FileWriter(this.outputPath));
            writer.write(data.toString());
            writer.flush();
            writer.close();
        }  catch (IOException e) {
            e.printStackTrace();
        }

    }




}
