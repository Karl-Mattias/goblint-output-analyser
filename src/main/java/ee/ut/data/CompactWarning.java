package ee.ut.data;

import java.util.*;

/**
 * Created by Karl-Mattias on 11.04.2017
 */
public class CompactWarning extends Warning {

    private final List<String> causedBy;  // Where the protection belief does not hold
    private final List<String> holdsIn;  //  Where the protection belief holds
    private final Protector belief;
    private final float strengthOfBelief;
    private final String location;
    private final BasicWarning warning;

    public CompactWarning(List<Access> causedBy, List<Access> holdsIn, Protector belief, float strengthOfBelief, String location, BasicWarning warning) {
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

    public BasicWarning getWarning() {
        return warning;
    }

    @Override
    public String toString() {

        return "Possible datarace at " + location +
                "\n   Probably caused by " + causedBy +
                " not having " + belief +
                " like in " + holdsIn;
    }

    @Override
    public String getId() {
        return warning.getId();
    }
}
