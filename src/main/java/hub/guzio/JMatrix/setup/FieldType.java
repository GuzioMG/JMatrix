package hub.guzio.JMatrix.setup;

import org.jetbrains.annotations.NotNull;

public record FieldType(String placeholder, String regexp) {
    @Override
    public @NotNull String toString() {
        return "{\"placeholder\":\""+placeholder+"\",\"regexp\":\""+regexp+"\"}";
    }
}