package hub.guzio.JMatrix;

import hub.guzio.JMatrix.data.FieldType;
import hub.guzio.JMatrix.data.ProtocolInstance;
import hub.guzio.SaneServer.Response;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

public abstract class Protocol {
    public final Map<String, FieldType> field_types;
    public final URI icon;
    public final String[] location_fields;
    public final String[] user_fields;

    protected Protocol(Map<String, FieldType> field_types, URI icon, String[] location_fields, String[] user_fields) {
        this.field_types = field_types;
        this.icon = icon;
        this.location_fields = location_fields;
        this.user_fields = user_fields;
    }

    @Override
    public String toString(){
        StringBuilder field_types = new StringBuilder();
        for(var fieldType : this.field_types.entrySet()) field_types.append("\"").append(fieldType.getKey()).append("\":").append(fieldType.getValue().toString());

        var location_fields = "";
        if(this.location_fields.length > 0) location_fields = "\""+String.join("\",\"", this.location_fields)+"\"";

        var user_fields = "";
        if(this.user_fields.length > 0) user_fields = "\""+String.join("\",\"", this.user_fields)+"\"";

        return "{\"field_types\":{"+field_types+"},\"icon\":\""+icon+"\",\"instances\":["+String.join(",", AppService.stringifyArray(onInstancesQueryByProtocol()))+"],\"location_fields\":["+location_fields+"],\"user_fields\":["+user_fields+"]}";
    }

    public abstract Map<String, Map<String, String>> onLocationQueryByAlias(@Nullable String alias);
    public abstract Optional<Response> onLocationQueryByProtocol(String[] args);

    public abstract ProtocolInstance[] onInstancesQueryByProtocol();

    public abstract Map<String, Map<String, String>> onUsersQueryById(@Nullable String MxId);
    public abstract Optional<Response> onUsersQueryByProtocol(String[] args);
}