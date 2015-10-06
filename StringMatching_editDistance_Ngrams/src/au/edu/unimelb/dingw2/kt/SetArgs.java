package au.edu.unimelb.dingw2.kt;

import org.kohsuke.args4j.Option;

/**
 * Created by dingwang on 15/9/6.
 */
public class SetArgs {
    @Option(name = "-q", usage = "Sets the path of the query file")
    public String queriesPath;

    @Option(name = "-t", usage = "Sets the path of the tweets document")
    public String docPath;

    @Option(name = "-m", usage = "Sets matching method, default is edit distance")
    public String method;

    @Option(name = "-n", usage = "Sets n in n-grams")
    public int ngram = 2;

    @Option(name = "--threshold", usage = "Sets similarity threshold")
    public int threshold = 80;
}
