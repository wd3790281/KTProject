package au.edu.unimelb.dingw2.kt; /**
 * Created by dingwang on 15/9/6.
 */
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class Main {
    public static void main(String[] args){
        SetArgs bean = new SetArgs();
        CmdLineParser parser = new CmdLineParser(bean);
        try {
            parser.parseArgument(args);
        } catch(CmdLineException e){
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            return;
        }


        String queryFilePath = bean.queriesPath;
        String tweetFilePath = bean.docPath;
        int threshold = bean.threshold;
        String methodStr = bean.method;
        int n = bean.ngram;



        if(methodStr.equals("ngram")){
            Ngrams.Ngram(queryFilePath,tweetFilePath,n,threshold);
        }else{
            GlobalEditDistance.GlobaleEditDistance(queryFilePath, tweetFilePath, threshold);
        }

    }
}
