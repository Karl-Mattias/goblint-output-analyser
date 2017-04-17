package ee.ut;

import ee.ut.data.CompactWarning;
import ee.ut.data.Warning;
import ee.ut.utils.RegionChecker;
import ee.ut.utils.WarningAnalyser;
import ee.ut.utils.XMLParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
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

        File inputXML = new File(args[0]);
        List<Warning> warnings = XMLParser.extractWarnings(inputXML);

        List<CompactWarning> compactWarnings = new ArrayList<>();
        List<Warning> noCompactWarnings = new ArrayList<>();

        for (Warning warning : warnings) {
            RegionChecker.removeCorrectRegions(warning);
            CompactWarning compactWarning = WarningAnalyser.getCompactWarning(warning);
            if (compactWarning == null || compactWarning.getStrengthOfBelief() <= BELIEF_THRESHOLD) {
                noCompactWarnings.add(warning);
            } else {
                compactWarnings.add(compactWarning);
            }
        }

        compactWarnings.stream()
                .sorted((c1, c2) -> Float.compare(c2.getStrengthOfBelief(), c1.getStrengthOfBelief()))
                .forEach(System.out::println);

        noCompactWarnings.forEach(System.out::println);
    }
}
