package hub.guzio.JMatrix;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import hub.guzio.SaneServer.Logger;
import hub.guzio.SaneServer.Response;

import java.net.URI;

public class MainHandler extends MatrixHandler {
    public MainHandler() {
        super(new Logger());
    }

    @Override
    public Response onRequest(HttpExchange rq, URI path, Headers resp) {
        return new Response(200, "json", "{}");
    }
}
