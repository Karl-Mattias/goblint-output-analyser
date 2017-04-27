package ee.ut.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Karl-Mattias on 24.04.2017
 */
public abstract class Warning {

    public abstract String getId();

    // (read@x, write@x) -> read&write@x
    List<String> compactString(List<Access> accesses) {

        Map<String, Access> accessMap = new HashMap<>();
        List<String> outPut = new ArrayList<>();

        for (Access access : accesses) {
            if (accessMap.containsKey(access.getLocation()) &&
                    (accessMap.get(access.getLocation()).isWrite() ^ access.isWrite())) {

                outPut.add(access.isWrite() ? "read&" + access.locationString() : "write&" + access.locationString());
                accessMap.remove(access.getLocation());

            } else {
                accessMap.put(access.getLocation(), access);
            }
        }

        accessMap.forEach((key, value) -> outPut.add(value.locationString()));

        return outPut;
    }
}
