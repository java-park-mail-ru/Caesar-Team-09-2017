package technopark.mechanics.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import technopark.mechanics.Config;

@SuppressWarnings("PublicField")
public class Bonus {

    public Bonus(@JsonProperty("bonus") Config.Bonus bonus) {
        this.bonus = bonus;
    }

    public final Config.Bonus bonus;

    @Override
    public String toString() {
        return '{'
                + "bonus=" + bonus
                + '}';
    }

    @SuppressWarnings("NewMethodNamingConvention")
    @NotNull
    public static Bonus of(Config.Bonus bonus) {
        return new Bonus(bonus);
    }

    public Config.Bonus getBonus() {
        return bonus;
    }
}