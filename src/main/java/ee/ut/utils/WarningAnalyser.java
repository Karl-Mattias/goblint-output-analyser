package ee.ut.utils;

import ee.ut.data.Access;
import ee.ut.data.CompactWarning;
import ee.ut.data.Protector;
import ee.ut.data.Warning;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Karl-Mattias on 11.04.2017
 */
public class WarningAnalyser {

    private WarningAnalyser(){}

    public static CompactWarning getCompactWarning(Warning warning) {

        List<Access> accesses = warning.getAccesses();
        int nrOfAccesses = accesses.size();

        Map.Entry<Protector, Long> mostCommonProtector = accesses.stream()
                .flatMap(access -> access.getProtectors().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .orElse(null);

        if (mostCommonProtector == null) {
            return null;
        }

        List<Access> causedBy = new ArrayList<>();
        List<Access> holdsIn = new ArrayList<>();

        for (Access access : accesses) {
            if (access.getProtectors().contains(mostCommonProtector.getKey())) {
                holdsIn.add(access);
            } else {
                causedBy.add(access);
            }
        }

        float strengthOfBelief = (float) mostCommonProtector.getValue() / nrOfAccesses;

        return new CompactWarning(causedBy, holdsIn, mostCommonProtector.getKey(), strengthOfBelief, warning.getId(), warning);
    }
}
