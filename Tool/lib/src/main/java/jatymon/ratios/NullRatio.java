package jatymon.ratios;

import jatymon.exceptions.NullRatiosHaveNoValueException;

import java.util.Objects;

public class NullRatio extends Ratio {
    public NullRatio() {
        super(null);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public double getValue() {
        throw new NullRatiosHaveNoValueException();
    }

    @Override
    public String toString() {
        return "NullRatio";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NullRatio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(toString());
    }
}
