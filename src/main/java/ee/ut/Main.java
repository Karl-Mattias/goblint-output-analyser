package ee.ut;

import ee.ut.data.Warning;
import ee.ut.utils.RegionChecker;
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

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        if (args.length < 1) {
            System.out.println("Please add the location of the xml file as an argument!");
            System.exit(0);
        }

        File inputXML = new File(args[0]);
        List<Warning> warnings = XMLParser.extractWarnings(inputXML);

        System.out.println(warnings);

        for (Warning warning : warnings) {
            RegionChecker.removeCorrectRegions(warning);
        }

        System.out.println(warnings);

    }
}
