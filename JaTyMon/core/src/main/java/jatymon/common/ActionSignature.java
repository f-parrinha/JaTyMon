package jatymon.common;

import java.util.List;

/**
 * Class {@code ActionSignature} represents data objects describing a signature of a typestate action
 * @param name name of the action
 * @param args arguments of the action
 * @author Francisco Parrinha
 */
public record ActionSignature(String name, List<String> args) {
    @Override
    public boolean equals(Object obj) {
        return obj instanceof ActionSignature(String name1, List<String> args1) &&
                this.name.equals(name1) &&
                this.args.equals(args1);
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", name, String.join(",", args));
    }
}
