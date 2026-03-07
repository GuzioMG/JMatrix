package hub.guzio.JMatrix.data;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public record ProtocolInstance(String desc, Map<String, String> fields, Optional<String> icon, String network_id){
    @Override
    public @NotNull String toString() {
        return "{}"; //TODO
    }
}