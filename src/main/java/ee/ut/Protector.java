package ee.ut;

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
}


