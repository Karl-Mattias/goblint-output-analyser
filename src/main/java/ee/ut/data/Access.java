package ee.ut.data;

import com.google.common.base.Objects;

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

    public Set<Protector> getProtectors() {
        return protectors;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLocation() {
        return location;
    }

    public boolean isWrite() {
        return isWrite;
    }

    public String locationString() {
        StringBuilder sb = new StringBuilder();

        if (isWrite) sb.append("Write @");
        else sb.append("Read @");

        sb.append(location);
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (isWrite) sb.append("Write @");
        else sb.append("Read @");

        sb.append(location);

        if (region != null) sb.append(" in region \"").append(region).append("\"");

        sb.append(" with protectors ").append(protectors.toString());

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Access access = (Access) o;
        return isWrite == access.isWrite &&
                Objects.equal(location, access.location) &&
                Objects.equal(protectors, access.protectors) &&
                Objects.equal(region, access.region);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(location, isWrite, protectors, region);
    }
}
