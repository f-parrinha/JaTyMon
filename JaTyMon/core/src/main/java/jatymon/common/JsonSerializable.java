package jatymon.common;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Classes implementing {@code JsonSerializable} can produce JSON objects representing themselves
 * @author Francisco Parrinha
 */
public interface JsonSerializable {
    Map<String, Object> toJson();

    static List<Map<String, Object>> fromList(List<? extends JsonSerializable> list) {
        var res = new LinkedList<Map<String, Object>>();
        for(var el : list) {
            res.add(el.toJson());
        }
        return res;
    }
}
