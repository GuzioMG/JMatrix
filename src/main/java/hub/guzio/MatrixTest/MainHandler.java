package hub.guzio.MatrixTest;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import hub.guzio.MatrixTest.sensibleServer.Response;
import hub.guzio.MatrixTest.sensibleServer.SmartHandler;

import java.net.URI;

public class MainHandler extends SmartHandler {

    @Override
    public Response onRequest(HttpExchange rq, URI path, Headers resp) {
        return new Response(200, "json", "{}");
    }
}
