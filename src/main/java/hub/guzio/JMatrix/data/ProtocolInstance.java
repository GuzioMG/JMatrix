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
) {
    @Override
    public @NotNull String toString() {
        var icon = "";
        if (this.icon.isPresent()) icon = "\"icon\":\""+this.icon.get()+"\",";

        StringBuilder fields = new StringBuilder();
        var ifFirst = true;
        for (var field : this.fields.entrySet()) {
            var delimiter = ",";
            if (ifFirst){
                delimiter = "";
                ifFirst = false;
            }
            fields.append(delimiter).append("\"").append(field.getKey()).append("\":\"").append(field.getValue()).append("\"");
        }

        return "{\"desc\":\""+desc+"\",\"fields\":{"+fields+"},"+icon+"\"network_id\":\""+network_id+"\"}";
    }
}