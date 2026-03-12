package hub.guzio.JMatrix.data;

import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;

public record Namespace(
        boolean exclusive,
        @NotNull @RegExp String regex
) {
    @Override
    public @NotNull String toString() {
        return "    - regex: "+regex+"\n    exclusive: "+exclusive+"\n";
    }
}
