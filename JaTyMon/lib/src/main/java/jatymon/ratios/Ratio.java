package jatymon.ratios;

import java.util.Objects;

public class Ratio {
    private final Double value;

    public Ratio(final Double value) {
        this.value = value;
    }

    public Ratio(final double value) {
        this.value = value;
    }

    public boolean isValid() {
        return value >= 0 && value <= 1;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NullRatio) {
            return false;
        }

        return obj instanceof Ratio ratio && ratio.value.equals(value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
