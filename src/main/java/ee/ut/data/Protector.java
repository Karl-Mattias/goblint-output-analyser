package ee.ut.data;

import com.google.common.base.Objects;

/**
 * Created by Karl-Mattias on 08.04.2017
 */
public class Protector {

    private final String id;
    private final ProtectorType type;

    public Protector(String id, ProtectorType type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public String toString() {
        return type + ":" + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Protector protector = (Protector) o;
        return Objects.equal(id, protector.id) &&
                type == protector.type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, type);
    }
}


