package Engineering;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
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
        Args bean = new Args();
        CmdLineParser parser = new CmdLineParser(bean);
        try {
            parser.parseArgument(args);
        } catch(CmdLineException e){
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            return;
        }


        String inPath = bean.inPath;
        String outPath = bean.outPath;
        String trainArff = bean.arffPath;

        Main main = new Main(inPath, outPath);
//        Main main = new Main(args[0], args[1]);
//        String trainArff = args[2];
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

        if( trainArff != null)
            main.outputDevArff(trainArff);
        else
            main.outputTrainArff();

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
            stemmer.add(normedWord);
            stemmer.stem();
            words.add(stemmer.toString());
        }
        this.positive.add(split[2]);

        return words;
    }

    void outputArff(String data){
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(
                    new FileWriter(this.outputPath));
            writer.write(data);
            writer.flush();
            writer.close();
        }  catch (IOException e) {
            e.printStackTrace();
        }

    }
    void outputTrainArff(){

        FastVector atts = new FastVector();
        for(String s:this.words){
            atts.addElement(new Attribute(s));
        }

        FastVector cls = new FastVector();
        cls.addElement("Y");
        cls.addElement("N");
        atts.addElement(new Attribute("ADR", cls));

        Instances data = new Instances("Tweets",atts,0);

        int i = 0;

        for(String tweet:this.tweets) {
            int j = 0;
            double[] vals = new double[data.numAttributes()];
            HashSet<String> wordsOfTweet = this.getwords(tweet);
            for (String word : this.words) {
                if (wordsOfTweet.contains(word)) {
                    vals[j] = 1;
                } else {
                    vals[j] = 0;
                }
                j++;
            }
            if (this.positive.get(i).equals("1")) {
                vals[j] = cls.indexOf("Y");
            } else {
                vals[j] = cls.indexOf("N");

            }
            i++;
            data.add(new Instance(1.0, vals));
        }
        outputArff(data.toString());

    }


    void outputDevArff(String arffPath){
        try {
            DataSource source = new DataSource(arffPath);
            Instances trainedData = source.getDataSet();

            FastVector atts = new FastVector();
            for(int i = 0; i<trainedData.numAttributes()-1;i++){
                atts.addElement(trainedData.attribute(i));
            }
            FastVector cls = new FastVector();
            cls.addElement("Y");
            cls.addElement("N");
            atts.addElement(new Attribute("ADR", cls));


            Instances data = new Instances("Tweets",atts,0);
            int i = 0;
            for(String tweet:this.tweets){
                int j = 0;
                double[] vals = new double[trainedData.numAttributes()];
                HashSet<String> wordsOfTweet = this.getwords(tweet);
                for(;j<data.numAttributes()-1;j++){
                    String[] split = atts.elementAt(j).toString().split(" ");
                    if(wordsOfTweet.contains(split[1])){
                        vals[j] = 1;
                    }else {
                        vals[j] = 0;
                        System.out.println(split[1]);
                    }
                }
                if(this.positive.get(i).equals("1")){
                    vals[j] = cls.indexOf("Y");
                }else{
                    vals[j] = cls.indexOf("N");

                }
                i++;
                data.add(new Instance(1.0, vals));
            }
            outputArff(data.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
