package hub.guzio.JMatrix.handlers._internal;

import com.sun.net.httpserver.HttpExchange;
import hub.guzio.JMatrix.AppService;
import hub.guzio.JMatrix.handlers.GuardedMatrixHandler;
import hub.guzio.SaneServer.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionHandler extends GuardedMatrixHandler {
    final AppService as;
    final List<String> handled = new ArrayList<>();

    public TransactionHandler(AppService appservice){
        super(appservice, 5, "PUT", new Response(200, "json", "{}"));
        as = appservice;
    }

    @Override
    protected Optional<Response> onRequest(HttpExchange rq, String body, String txnId, int pathLength, String[] queryArgs) throws Throwable {
        if(handled.contains(txnId)) return Optional.empty();
        handled.add(txnId);
        return as.onTransaction(body);
    }
}