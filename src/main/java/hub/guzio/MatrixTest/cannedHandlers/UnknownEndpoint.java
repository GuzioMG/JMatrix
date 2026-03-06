package hub.guzio.MatrixTest.cannedHandlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import hub.guzio.MatrixTest.sensibleServer.Response;
import hub.guzio.MatrixTest.sensibleServer.SmartHandler;

import java.net.URI;

public class UnknownEndpoint extends SmartHandler{
    @Override
    public Response onRequest(HttpExchange rq, URI path, Headers resp) {
        return getError(404, path, "");
    }

    public static Response getError(int code, URI path, String context) {
        return new Response(code, "json", "{\"errcode\":\"M_UNRECOGNIZED\",\"error\":\"ERROR 404: \\\""+path+"\\\" is not a valid Matrix AppService V1 endpoint"+context+".\"}");
    }
}
