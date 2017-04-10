package ee.ut;

import java.util.List;

/**
 * Created by Karl-Mattias on 08.04.2017
 */
public class Warning {

    private final String id;
    private List<Access> accesses;

    public Warning(String id) {
        this.id = id;
    }


    public void setAccesses(List<Access> accesses) {
        this.accesses = accesses;
    }

    public List<Access> getAccesses() {
        return accesses;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Memory location ").append(id);
        for (Access access : accesses) {
            sb.append("\n   ").append(access.toString());
        }
        return sb.toString();
    }
}