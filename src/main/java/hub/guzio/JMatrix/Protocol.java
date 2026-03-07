package hub.guzio.JMatrix;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import hub.guzio.JMatrix.data.FieldType;
import hub.guzio.JMatrix.data.ProtocolInstance;
import hub.guzio.SaneServer.Response;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.util.Map;

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

    public abstract Map<String, Map<String, String>> onLocationQueryByAlias(@Nullable String alias);
    public abstract Response onLocationQueryByProtocol(HttpExchange rq, URI path, Headers resp);

    public abstract ProtocolInstance[] onInstancesQueryByProtocol();

    public abstract Map<String, Map<String, String>> onUsersQueryById(@Nullable String MxId);
    public abstract Response onUsersQueryByProtocol(HttpExchange rq, URI path, Headers resp);
}