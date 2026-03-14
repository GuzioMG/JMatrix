package hub.guzio.JMatrix.handlers._internal.query.inProtocol;

import com.sun.net.httpserver.HttpExchange;
import hub.guzio.JMatrix.AppService;
import hub.guzio.JMatrix.Protocol;
import hub.guzio.JMatrix._internal.Utils;
import hub.guzio.JMatrix.handlers.GuardedMatrixHandler;
import hub.guzio.JMatrix.handlers._internal.query.UnknownProtocolQueryHandler;
import hub.guzio.SaneServer.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class UserQueryInProtocolHandler extends GuardedMatrixHandler {
    final Protocol proto;

    public UserQueryInProtocolHandler(AppService appservice, Protocol proto){
        super(appservice, 6, "GET", new Response(404, "json", "{\"errcode\":\"M_NOT_FOUND\",\"error\":\"The requested protocol was found, but there were no users found as part of said protocol, that would match the specified query.\"}"));
        this.proto = proto;
    }

    @Override
    protected Optional<Response> onRequest(HttpExchange rq, String body, String pathArg, int pathLength, String[] queryArgs) throws Throwable {
        if(!Objects.equals(pathArg, proto.name)) return Optional.of(UnknownProtocolQueryHandler.UNKNOWN_PROTOCOL_RESPONSE);
        Map<String, String> args = new HashMap<>();
        for (var field : proto.user_fields){
            var arg = Utils.extractArg(queryArgs, field);
            if (arg.isPresent() && !Objects.equals(arg.get(), "\b")) args.put(field, arg.get());
        }
        return proto.onUsersQueryByProtocol(args);
    }
}