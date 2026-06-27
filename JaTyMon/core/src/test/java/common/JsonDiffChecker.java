package common;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class JsonDiffChecker {

    public static void diff(JsonNode a, JsonNode b, String path, List<DiffEntry> out) {
        if (a == null || b == null || !a.getNodeType().equals(b.getNodeType())) {
            out.add(new DiffEntry(path, a, b));
            return;
        }

        // Both a and b are values in a Json object
        if (a.isValueNode() && !a.equals(b)) {
            out.add(new DiffEntry(path, a, b));
            return;
        }

        // Both a and b are Json objects
        if (a.isObject()) {
            if (a.equals(b)) {
                return;
            }

            final var fieldNames = new HashSet<String>();
            a.fieldNames().forEachRemaining(fieldNames::add);
            b.fieldNames().forEachRemaining(fieldNames::add);

            for (final String f : fieldNames) {
                diff(a.get(f), b.get(f), path + "/" + f, out);
            }

            return;
        }

        // Both a and b are array types (i.e., decl nodes have multiple states)
        if (a.isArray()) {
            for (int i = 0; i < Math.max(a.size(), b.size()); i++) {
                final JsonNode av = i < a.size() ? a.get(i) : null;
                final JsonNode bv = i < b.size() ? b.get(i) : null;
                diff(av, bv, path + "[" + i + "]", out);
            }
        }
    }

    public record DiffEntry(String path, JsonNode expected, JsonNode actual) {}
}
