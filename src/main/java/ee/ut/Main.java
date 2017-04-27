package ee.ut;

import ee.ut.data.CompactWarning;
import ee.ut.data.BasicWarning;
import ee.ut.data.NoLockWarning;
import ee.ut.data.Warning;
import ee.ut.utils.RegionChecker;
import ee.ut.utils.WarningAnalyser;
import ee.ut.utils.XMLParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

/**
 * Created by Karl-Mattias on 08.04.2017
 */
public class Main {

    // What must the ratio of memory accesses to accesses with the given belief be for the belief to be noted
    private static final double BELIEF_THRESHOLD = 0.5;

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        if (args.length < 1) {
            System.out.println("Please add the location of the xml file as an argument!");
            System.exit(0);
        }

        File inputFile = new File(args[0]);

        if (inputFile.isFile()) {
            System.out.println(getWarnings(inputFile));
        } else if (inputFile.isDirectory()) {

            File[] files = inputFile.listFiles();
            if (files == null) throw new RuntimeException("Directory threw an exception");

            File resultDir = new File("result");
            if (!resultDir.exists()) {
                if (!resultDir.mkdir()) throw new RuntimeException("Could not make directory");
            }

            for (File file : files) {

                if (!file.getName().endsWith(".txt")) {
                    continue;
                }

                String newFile = "result/" + file.getName().replace(".txt", ".xml");
                boolean hasWritten = false;

                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {

                        String line;
                        while ((line = reader.readLine()) != null) {

                            if (line.trim().startsWith("<") && line.trim().endsWith(">")) {
                                writer.write(line);
                                hasWritten = true;
                            }
                        }
                    }
                }

                if (!hasWritten) {
                    File deleteNewFile = new File(newFile);
                    if (!deleteNewFile.delete()) throw new RuntimeException("Could not delete file");
                }

            } // for


            File[] xmlFiles = resultDir.listFiles();
            if (xmlFiles == null) throw new RuntimeException("Result dir threw an exception");

            for (File xmlFile : xmlFiles) {
                if (!xmlFile.getName().endsWith(".xml")) continue;
                String warnings = getWarnings(xmlFile);
                String fileName = "result/" + xmlFile.getName().replace(".xml", ".txt");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                    writer.write(warnings);
                }
            }

            System.out.println(nrWarnings);
            System.out.println(nrCompact);
            System.out.println(nrNoLock);
            System.out.println(nrNoCompact);
        }  // isDirectory
    }

    private static int nrWarnings = 0;
    private static int nrCompact = 0;
    private static int nrNoLock = 0;
    private static int nrNoCompact = 0;


    private static String getWarnings(File inputXML) throws IOException, SAXException, ParserConfigurationException {
        List<BasicWarning> warnings = XMLParser.extractWarnings(inputXML);
        nrWarnings += warnings.size();

        List<CompactWarning> compactWarnings = new ArrayList<>();
        List<BasicWarning> noCompactWarnings = new ArrayList<>();
        List<NoLockWarning> noLockWarnings = new ArrayList<>();

        for (BasicWarning warning : RegionChecker.performRegionOperations(warnings)) {
            Warning compactWarning = WarningAnalyser.getCompactWarning(warning);
            if (compactWarning instanceof CompactWarning &&
                    ((CompactWarning) compactWarning).getStrengthOfBelief() >= BELIEF_THRESHOLD) {
                compactWarnings.add((CompactWarning) compactWarning);
            }
            else if (compactWarning instanceof NoLockWarning) {
                noLockWarnings.add((NoLockWarning) compactWarning);
            } else {
                noCompactWarnings.add(warning);
            }
        }

        nrCompact += compactWarnings.size();
        nrNoLock += noLockWarnings.size();
        nrNoCompact += noCompactWarnings.size();

        StringBuilder sb = new StringBuilder();

        compactWarnings.stream()
                .sorted((c1, c2) -> Float.compare(c2.getStrengthOfBelief(), c1.getStrengthOfBelief()))
                .forEach(warning -> sb.append(warning.toString()).append('\n'));

        noLockWarnings.forEach(warning -> sb.append(warning.toString()).append('\n'));
        noCompactWarnings.forEach(warning -> sb.append(warning.toString()).append('\n'));

        return sb.toString();
    }
}
