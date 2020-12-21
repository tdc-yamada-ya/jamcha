package jp.co.tdc.jamcha.cmd;

import org.apache.commons.cli.*;

import java.io.PrintWriter;

public class CommandLineHelper {
    public CommandLine tryParse(CommandLineParser parser, Options options, String[] arguments, PrintWriter helpWriter) {
        try {
            return parser.parse(options, arguments);
        } catch (ParseException e) {
            printHelp(options, helpWriter);
            throw new RuntimeException("command line parse error", e);
        }
    }

    void printHelp(Options options, PrintWriter helpWriter) {
        var hf = new HelpFormatter();
        hf.printHelp(
            helpWriter,
            hf.getWidth(),
            "Main",
            "",
            options,
            0,
            0,
            "");
        helpWriter.flush();
    }
}
