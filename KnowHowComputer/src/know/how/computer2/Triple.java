package know.how.computer2;

public class Triple<A, B, C> {
    public final A first;
    public final B second;
    public final C third;

    public Triple(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Triple<?, ?, ?> other)) return false;
        return first.equals(other.first) && second.equals(other.second) && third.equals(other.third);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(first, second, third);
    }
}

