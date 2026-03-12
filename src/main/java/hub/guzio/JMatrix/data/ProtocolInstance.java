package hub.guzio.JMatrix.data;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

public record ProtocolInstance(
        @NotNull String desc,
        @NotNull Map<String, String> fields,
        @NotNull Optional<URI> icon,
        @NotNull String network_id
){
    @Override
    public @NotNull String toString() {
        return "{}"; //TODO
    }
}