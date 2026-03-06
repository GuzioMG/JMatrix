package hub.guzio.MatrixTest.cannedHandlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import hub.guzio.MatrixTest.sensibleServer.Response;
import hub.guzio.MatrixTest.sensibleServer.SmartHandler;

import java.net.URI;

public class AliasEndpoint extends SmartHandler{
    @Override
    public Response onRequest(HttpExchange rq, URI path, Headers resp) {
        return new Response(404, "json", "{\"errcode\":\"M_UNRECOGNIZED\",\"error\":\"ERROR 404: Sorry, this appservice doesn't support any RoomAlias-related features yet.\"}");
    }
}
