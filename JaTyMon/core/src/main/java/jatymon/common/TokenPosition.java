package jatymon.common;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Record storing information regarding a token in the typestate source text
 * @param basename file/typestate name
 * @param pos position (ln, col)
 * @param line line
 * @param column column
 */
public record TokenPosition(String basename, int pos, int line, int column) implements JsonSerializable, Comparable<TokenPosition> {
    public static final TokenPosition NIL = new TokenPosition("", 0, 0, 0);

    public TokenPosition(final String basename, final int pos, final int line, final int column) {
        this.basename = Paths.get(basename).getFileName().toString();
        this.pos = pos;
        this.line = line;
        this.column = column;
    }

    public static TokenPosition createFrom(final ParserRuleContext ctx) {
        if (ctx == null) return NIL;
        return createFrom(ctx.getStart());
    }

    public static TokenPosition createFrom(final Token token) {
        return new TokenPosition(token.getTokenSource().getSourceName(),
                token.getStartIndex(),
                token.getLine(),
                token.getCharPositionInLine());
    }

    @Override
    public String toString() {
        return String.format("%s:%s", line, column);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof TokenPosition(String basename1, int pos1, int line1, int column1)) &&
                basename1.equals(basename) &&
                line1 == line &&
                column1 == column &&
                pos1 == pos;
    }

    @Override
    public int compareTo(TokenPosition other) {
        int lineDiff = this.line - other.line;
        return lineDiff != 0 ? lineDiff : this.column - other.column;
    }

    @Override
    public Map<String, Object> toJson() {
        var json = new LinkedHashMap<String, Object>();
        json.put("line", line);
        json.put("column", column);
        return json;
    }
}
