package technoPark.mechanics.models.part;

import org.jetbrains.annotations.NotNull;
import technoPark.mechanics.models.Coords;
import technoPark.mechanics.models.Snap;


public class MousePart implements GamePart {
    @NotNull
    private Coords mouse;


    public MousePart() {
        this.mouse = new Coords(0.0f, 0.0f);
    }

    @NotNull
    public Coords getMouse() {
        return mouse;
    }

    public void setMouse(@NotNull Coords mouse) {
        this.mouse = mouse;
    }

    @Override
    public MouseSnap takeSnap() {
        return new MouseSnap(this);
    }

    public static final class MouseSnap implements Snap<MousePart> {

        private final Coords mouse;

        public MouseSnap(MousePart mouse) {
            this.mouse = mouse.getMouse();
        }

        public Coords getMouse() {
            return mouse;
        }
    }
}
