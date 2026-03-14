package hub.guzio.JMatrix.authProcessors;

import hub.guzio.JMatrix.AuthProcessor;
import hub.guzio.SaneServer.Logger;
import hub.guzio.SaneServer.Response;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DefaultAuth implements AuthProcessor {
    final String credentials;
    final Logger l;

    public DefaultAuth(String hs_token, Logger logger) {
        credentials = hs_token;
        l = logger;
    }

    @Override
    public Optional<Response> onAuth(List<String> authHeader) {
        var msgBase = "Possible break-in attempt (tho, more likely, just a misconfigured registration.yaml): ";

        if (Objects.isNull(authHeader) || authHeader.size() != 1) {
            var authHeaderStringified = "<NULL>";
            int length = 0;
            if (!Objects.isNull(authHeader)){
                length = authHeader.size();
                authHeaderStringified = "\""+String.join("\", \"", authHeader)+"\"";
            }
            var msg = "No valid token was provided. Or maybe multiple were, this code doesn't care. The bottom line is that exactly 1 (one) auth token was expected, but that wasn't the case (got "+authHeaderStringified+" (ie. something of length "+length+") instead). Treating this request as unauthenticated.";
            l.wrn(msgBase+msg);
            return Optional.of(new Response(401, "json", "{\"errcode\":\"M_MISSING_TOKEN\",\"error\":\"" + msg.replaceAll("\"", "\\\"") + "\"}")); //That escape is not redundant - JSON's gonna sh!t its pants if it ever sees a " without a \ in a string.
        }

        var authToken = authHeader.getFirst();
        if (!Objects.equals(authToken, credentials)) {
            var msg = "The homeserver gave this appservice an invalid token (got \""+authToken+"\" instead of the expected value).";
            l.wrn(msgBase+msg);
            return Optional.of(new Response(403, "json", "{\"errcode\":\"M_UNKNOWN_TOKEN\",\"error\":\"" + msg.replaceAll("\"", "\\\"") + "\"}")); //That escape is not redundant - JSON's gonna sh!t its pants if it ever sees a " without a \ in a string.
        }

        return Optional.empty();
    }
}
