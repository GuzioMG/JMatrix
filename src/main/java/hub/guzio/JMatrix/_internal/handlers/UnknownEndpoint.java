package hub.guzio.JMatrix._internal.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import hub.guzio.JMatrix.MatrixHandler;
import hub.guzio.SaneServer.Logger;
import hub.guzio.SaneServer.Response;

import java.net.URI;

public class UnknownEndpoint extends MatrixHandler {
    public UnknownEndpoint() {
        super(new Logger());
    }

    @Override
    public Response onRequest(HttpExchange rq, URI path, Headers resp) {
        return getError(404, path, "");
    }

    public static Response getError(int code, URI path, String context) {
        return new Response(code, "json", "{\"errcode\":\"M_UNRECOGNIZED\",\"error\":\"ERROR 404: \\\""+path+"\\\" is not a valid Matrix AppService V1 endpoint"+context+".\"}");
    }
}
