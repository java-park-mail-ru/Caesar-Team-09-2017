package technoPark.mechanics.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import technoPark.mechanics.Config.KeyDown;

@SuppressWarnings("PublicField")
public class Move {

    public Move(@JsonProperty("keyDown") KeyDown keyDown) {
        this.keyDown = keyDown;
    }

    public final KeyDown keyDown;

    @Override
    public String toString() {
        return '{' +
                "keyDown=" + keyDown +
                '}';
    }
    @SuppressWarnings("NewMethodNamingConvention")
    @NotNull
    public static Move of(KeyDown keyDown) {
        return new Move(keyDown);
    }

    public KeyDown getKeyDown() {
        return keyDown;
    }
}