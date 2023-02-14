package domain;

import java.util.Objects;

public class Position {

    private int position = 0;

    public void increase() {
        position++;
    }

    public int compareTo(Position other) {
        return this.position - other.position;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position other = (Position) o;
        return position == other.position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }
}