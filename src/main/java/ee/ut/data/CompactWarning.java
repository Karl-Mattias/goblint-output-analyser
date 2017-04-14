package ee.ut.data;

import java.util.*;

/**
 * Created by Karl-Mattias on 11.04.2017
 */
public class CompactWarning {

    private final List<String> causedBy;  // Where the protection belief does not hold
    private final List<String> holdsIn;  //  Where the protection belief holds
    private final Protector belief;
    private final float strengthOfBelief;
    private final String location;
    private final Warning warning;

    public CompactWarning(List<Access> causedBy, List<Access> holdsIn, Protector belief, float strengthOfBelief, String location, Warning warning) {
        this.causedBy = compactString(causedBy);
        this.holdsIn = compactString(holdsIn);
        this.belief = belief;
        this.strengthOfBelief = strengthOfBelief;
        this.location = location;
        this.warning = warning;
    }

    public float getStrengthOfBelief() {
        return strengthOfBelief;
    }

    public Warning getWarning() {
        return warning;
    }

    // (Read @x, Write @x) -> Read&Write @x
    private List<String> compactString(List<Access> accesses) {

        Map<String, Access> accessMap = new HashMap<>();
        List<String> outPut = new ArrayList<>();

        for (Access access : accesses) {
            if (accessMap.containsKey(access.getLocation()) &&
                    (accessMap.get(access.getLocation()).isWrite() ^ access.isWrite())) {

                outPut.add(access.isWrite() ? "Read&" + access.locationString() : "Write&" + access.locationString());
                accessMap.remove(access.getLocation());

            } else {
                accessMap.put(access.getLocation(), access);
            }
        }

        accessMap.forEach((key, value) -> outPut.add(value.locationString()));

        return outPut;
    }

    @Override
    public String toString() {

        return "Possible datarace at " + location +
                "\n   Probably caused by " + causedBy +
                " not having " + belief +
                " like in " + holdsIn;
    }
}
