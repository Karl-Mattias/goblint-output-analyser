package ee.ut;

import java.util.Set;

/**
 * Created by Karl-Mattias on 08.04.2017
 */
public class Access {

    private final String location;
    private final boolean isWrite;  // True for write, false for read
    private final Set<Protector> protectors;
    private String region;

    public Access(String location, boolean isWrite, Set<Protector> protectors) {
        this.location = location;
        this.isWrite = isWrite;
        this.protectors = protectors;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Access && isWrite == ((Access) obj).isWrite && location.equals(((Access) obj).location);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (isWrite) sb.append("Write @");
        else sb.append("Read @");

        sb.append(location);

        if (region != null) sb.append(" in region ").append(region);

        sb.append(" with ").append(protectors.toString());

        return sb.toString();
    }
}
