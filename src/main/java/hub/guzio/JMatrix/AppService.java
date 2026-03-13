package hub.guzio.JMatrix;

import com.sun.net.httpserver.HttpServer;
import hub.guzio.JMatrix.authProcessors.DefaultAuth;
import hub.guzio.JMatrix.data.RegistrationYaml;
import hub.guzio.JMatrix.handlers.UnknownEndpoint;
import hub.guzio.JMatrix.handlers._internal.*;
import hub.guzio.JMatrix.handlers._internal.PingHandler;
import hub.guzio.JMatrix.handlers._internal.query.UnknownProtocolQueryHandler;
import hub.guzio.JMatrix.handlers._internal.query.inProtocol.LocationQueryInProtocolHandler;
import hub.guzio.JMatrix.handlers._internal.query.inProtocol.ProtocolQueryHandler;
import hub.guzio.JMatrix.handlers._internal.query.inProtocol.UserQueryInProtocolHandler;
import hub.guzio.JMatrix.handlers._internal.query.LocationQueryHandler;
import hub.guzio.JMatrix.handlers._internal.query.UserQueryHandler;
import hub.guzio.SaneServer.Logger;
import hub.guzio.SaneServer.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Objects;
import java.util.Optional;

public abstract class AppService implements AutoCloseable {
    Optional<HttpServer> attachedServer = Optional.empty();
    boolean isClosed = false;
    @NotNull public HttpClient sender = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();

    public final Logger logger;
    public final int backlog;
    public final RegistrationYaml registration;
    public final AuthProcessor auth;

    protected AppService(@NotNull Logger logger, int backlog, @NotNull RegistrationYaml registration){
        this.logger = logger;
        this.backlog = backlog;
        this.registration = registration;
        this.auth = new DefaultAuth(registration.hs_token(), logger);
    }

    protected AppService(@NotNull Logger logger, @NotNull RegistrationYaml registration){
        this.logger = logger;
        this.backlog = 1024;
        this.registration = registration;
        this.auth = new DefaultAuth(registration.hs_token(), logger);
    }

    protected AppService(@NotNull Logger logger, int backlog, @NotNull RegistrationYaml registration, @NotNull AuthProcessor auth){
        this.logger = logger;
        this.backlog = backlog;
        this.registration = registration;
        this.auth = auth;
    }

    protected AppService(@NotNull Logger logger, @NotNull RegistrationYaml registration, @NotNull AuthProcessor auth){
        this.logger = logger;
        this.backlog = 1024;
        this.registration = registration;
        this.auth = auth;
    }

    public HttpServer serve(@Nullable InetSocketAddress port) throws IllegalArgumentException, IOException {
        if (isClosed) throw new IllegalStateException("Attempted to serve a closed appservice.");

        if (attachedServer.isPresent()) {
            if (Objects.equals(attachedServer.get().getAddress(), port) && !Objects.isNull(port)) return attachedServer.get();
            else throw new IllegalArgumentException("Attempted to re-serve an already served appservice on a different port.");
        }

        if (Objects.isNull(port)) throw new IllegalArgumentException("Called serve() with port==null (ie. tried to access the underlying server), nut the appservice hasn't yet been served.");

        HttpServer server = HttpServer.create(port, backlog);
        attachedServer = Optional.of(server);

        //Core endpoints
        server.createContext("/_matrix/app/v1/transactions/", new TransactionHandler(this));
        server.createContext("/_matrix/app/v1/ping", new PingHandler(this));
        server.createContext("/_matrix/app/v1/users/", new UserCreateHandler(this));
        server.createContext("/_matrix/app/v1/rooms/", new RoomCreateHandler(this));

        //Static protocol endpoints
        server.createContext("/_matrix/app/v1/thirdparty/user", new UserQueryHandler(this));
        server.createContext("/_matrix/app/v1/thirdparty/protocol/", new UnknownProtocolQueryHandler(this));
        server.createContext("/_matrix/app/v1/thirdparty/location", new LocationQueryHandler(this));

        //Dynamic protocol endpoints
        if (registration.protocols().isPresent()){
            for(var proto : registration.protocols().get().entrySet()){
                server.createContext("/_matrix/app/v1/thirdparty/user/"+proto.getKey(), new UserQueryInProtocolHandler(this, proto.getValue()));
                server.createContext("/_matrix/app/v1/thirdparty/protocol/"+proto.getKey(), new ProtocolQueryHandler(this, proto.getValue()));
                server.createContext("/_matrix/app/v1/thirdparty/location/"+proto.getKey(), new LocationQueryInProtocolHandler(this, proto.getValue()));
            }
        }

        //Unknown endpoints
        server.createContext("/_matrix/", new UnknownEndpoint(logger));

        return server;
    }

    public HttpResponse<String> sendAuthenticated(@NotNull HttpRequest.Builder rq) throws IOException, InterruptedException, IllegalStateException {
        if (isClosed) throw new IllegalStateException("Attempted to send a request from a closed appservice.");
        return sender.send(rq.setHeader("Authorization", "Bearer "+registration.as_token()).build(), HttpResponse.BodyHandlers.ofString());
    }

    public void close(int timeout){
        if (isClosed) return;

        try { attachedServer.orElseThrow().stop(timeout); }
        catch (Throwable e) { /*We don't care if nothing was attached yet. If it wasn't - isn't obviously stopped.*/ }

        boolean safeExit;
        try { safeExit = sender.awaitTermination(Duration.of(timeout, ChronoUnit.SECONDS)); }
        catch (Throwable e) {
            try{ safeExit = sender.isTerminated(); }
            catch (Throwable eee){
                //This should never throw anything; just making sure that safeExit will get set to false, in case the wildest of edge-cases happen.
                safeExit = false;
            }
        }

        try {
            if (!safeExit) sender.shutdownNow();
            //sender.close(); <-- This was supposed to be here, but - upon further inspection of the class - it seems like this mf has a hardcoded timeout of ONE DAY! (And also just calls the other stuff that I had, anyway.) Leaving this comment in, to make sure that I remember to never accidentally put .close() here again.
        }
        catch (Throwable e) { /*Same case as above, but it's isClosed getting set to true.*/ }

        isClosed = true;
    }

    @Override
    public void close() { close(3); }

    public abstract Optional<Response> onTransaction(String body) throws Throwable;
    public abstract Optional<Response> onUserRequest(String userId) throws Throwable;
    public abstract Optional<Response> onRoomRequest(String roomAlias) throws Throwable;
}