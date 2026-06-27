package jatymon.babelprojects.abdquorum.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;

public class Database {
    private final Map<String, Entry> db;

    public Database() {
        this.db = new HashMap<>();
    }
    
    public void put(final String id, final Entry entry) {
        db.put(id, entry);
    }

    public void remove(final String id) {
        db.remove(id);
    }

    public boolean contains(final String id) {
        return db.containsKey(id);
    }

    public Entry getEntry(final String id) {
        if (db.containsKey(id)) {
            return db.get(id);
        }

        db.put(id, new Entry(new Tag(0,0), new byte[0]));
        return db.get(id);
    }


    public record Entry(Tag tag, byte[] value) {

        @Override
        public boolean equals(final Object obj) {
            return obj instanceof Entry(Tag tag1, byte[] value1)
                    && tag.equals(tag1)
                    && Arrays.equals(value, value1);
        }

        @Override
        public String toString() {
            return "Database.Entry{tag: %s, value: %s }".formatted(tag, HexFormat.of().formatHex(value));
        }
    }
}
