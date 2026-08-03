package cli;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Production-feel log analyzer CLI for Java 25 interviews.
 *
 * <p>Stdout = report; stderr = structured logs. Non-zero exit on errors.
 */
public final class LogAnalyzer {
    private static final CliLogger LOG = CliLogger.info();

    public static void main(String[] argv) {
        Args args;
        try {
            args = Args.parse(argv);
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage());
            System.err.println();
            System.err.print(Args.helpText());
            System.exit(ExitCodes.USAGE);
            return;
        }

        if (args.help()) {
            System.out.print(Args.helpText());
            System.exit(ExitCodes.SUCCESS);
            return;
        }

        try {
            run(args);
            System.exit(ExitCodes.SUCCESS);
        } catch (IOException e) {
            LOG.error("I/O failure reading " + args.file(), e);
            System.exit(ExitCodes.IO);
        } catch (Exception e) {
            LOG.error("Unexpected failure", e);
            System.exit(ExitCodes.UNEXPECTED);
        }
    }

    static void run(Args args) throws IOException {
        LOG.info("Analyzing " + args.file().toAbsolutePath());
        if (!Files.isRegularFile(args.file())) {
            throw new IOException("Not a readable file: " + args.file());
        }

        List<LogLine> lines;
        try (var stream = Files.lines(args.file())) {
            lines = stream.map(LogParser::parse).toList();
        }

        LOG.info("Parsed " + lines.size() + " lines");
        AnalysisReport report = AnalysisReport.of(lines, args);
        System.out.print(report.render());
    }

    private LogAnalyzer() {}
}
