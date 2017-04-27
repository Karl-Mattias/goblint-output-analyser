package ee.ut.data;

/**
 * Created by Karl-Mattias on 24.04.2017
 */
public class NoLockWarning extends Warning {

    private BasicWarning warning;

    public NoLockWarning(BasicWarning warning) {
        this.warning = warning;
    }

    @Override
    public String getId() {
        return warning.getId();
    }

    @Override
    public String toString() {
        return "Possible datarace at " + warning.getId() +
                "\n   Detected because the following accesses take no locks: " + compactString(warning.getAccesses());
    }
}
