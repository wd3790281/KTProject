package Engineering;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

/**
 * Created by dingwang on 15/10/8.
 */
public class Args {

    @Argument(index = 0)
    public String inPath;

    @Argument(index = 1)
    public String outPath;

    @Option(name = "--trained", usage = "Input the trained dataset in arff")
    public String arffPath;

    @Option(name = "-m", usage = "select built dev.arff or test.arff")
    public String builtModel = "dev";
}
