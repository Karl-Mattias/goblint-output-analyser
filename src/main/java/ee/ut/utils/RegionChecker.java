package ee.ut.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import ee.ut.data.Access;
import ee.ut.data.Protector;
import ee.ut.data.BasicWarning;

import java.util.*;

/**
 * Created by Karl-Mattias on 10.04.2017
 */
public class RegionChecker {

    private RegionChecker() {}

    public static List<BasicWarning> performRegionOperations(List<BasicWarning> warnings) {
        return removeCorrectRegions(splitWarningsByRegions(warnings));
    }

    private static List<BasicWarning> splitWarningsByRegions(List<BasicWarning> warnings) {

        List<BasicWarning> newWarnings = new ArrayList<>();

        for (BasicWarning warning : warnings) {
            Multimap<String, Access> regionAccessMap = ArrayListMultimap.create();
            List<Access> noRegion = new ArrayList<>();

            for (Access access : warning.getAccesses()) {
                String region = access.getRegion();

                if (region == null) {
                    noRegion.add(access);
                } else {
                    regionAccessMap.put(region, access);
                }
            }

            if (!noRegion.isEmpty()) {
                BasicWarning newWarning = new BasicWarning(warning.getId());
                newWarning.setAccesses(noRegion);
                newWarnings.add(newWarning);
            }

            if (!regionAccessMap.isEmpty()) {
                for (Map.Entry<String, Collection<Access>> entry : regionAccessMap.asMap().entrySet()) {
                    BasicWarning newWarning = new BasicWarning(warning.getId());
                    newWarning.setAccesses((List<Access>) entry.getValue());
                    newWarnings.add(newWarning);
                }
            }

        }

        return newWarnings;
    }

    private static List<BasicWarning> removeCorrectRegions(List<BasicWarning> warnings) {

        List<BasicWarning> newWarnings = new ArrayList<>();

        for (BasicWarning warning : warnings) {
            Set<Protector> protectors = null;

            for (Access access : warning.getAccesses()) {

                if (protectors == null) {
                    protectors = access.getProtectors();
                } else {
                    // Only keep in the map all those protections that are in all of the accesses for a certain region
                    protectors.retainAll(access.getProtectors());
                }
            }

            if (protectors != null && protectors.isEmpty()) {
                newWarnings.add(warning);
            }
        }

        return newWarnings;

    }
}
