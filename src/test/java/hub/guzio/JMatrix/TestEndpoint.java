package hub.guzio.JMatrix;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import hub.guzio.SaneServer.Response;
import hub.guzio.SaneServer.SmartHandler;

import java.net.URI;

public class TestEndpoint extends SmartHandler {

    @Override
    protected Response onRequest(HttpExchange httpExchange, Headers headers, URI uri, String[] strings, String[] strings1) throws Throwable {
        return new Response(200, "plain", "What about this, huh?");
    }
}
