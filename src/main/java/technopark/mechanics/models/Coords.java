package technopark.mechanics.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("PublicField")
public class Coords {

    public Coords(@JsonProperty("x") int x, @JsonProperty("y") int y) {
        this.x = x;
        this.y = y;
    }

    public int x; // убрал final
    public int y;

    @Override
    public String toString() {
        return '{'
                + "x=" + x
                + ", y=" + y
                + '}';
    }

    @SuppressWarnings("NewMethodNamingConvention")
    @NotNull
    public static Coords of(int x, int y) {
        return new Coords(x, y);
    }
}
