package hub.guzio.JMatrix;

import hub.guzio.SaneServer.Logger;
import hub.guzio.SaneServer.Response;

import java.util.Optional;

public interface AuthProcessor {
    Optional<Response> onAuth(java.util.List<String> authHeader);
}
