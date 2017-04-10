package ee.ut.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import ee.ut.data.Access;
import ee.ut.data.Protector;
import ee.ut.data.Warning;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Karl-Mattias on 10.04.2017
 */
public class RegionChecker {

    private RegionChecker() {}

    public static void removeCorrectRegions(Warning warning) {

        Multimap<String, Protector> regionLocksMap = ArrayListMultimap.create();

        for (Access access : warning.getAccesses()) {

            String region = access.getRegion();
            Set<Protector> protectors = access.getProtectors();

            if (region == null) continue;

            if (regionLocksMap.containsKey(region)) {

                // Only keep in the map all those protections that are in all of the accesses for a certain region
                regionLocksMap.get(region).retainAll(protectors);

            } else {
                regionLocksMap.putAll(region, protectors);
            }

        }

        List<Access> filteredAccesses = warning.getAccesses()
                .stream()
                .filter(access -> !(access.getRegion() != null && regionLocksMap.containsKey(access.getRegion())))
                .collect(Collectors.toList());

        warning.setAccesses(filteredAccesses);

    }
}
