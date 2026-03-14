package hub.guzio.JMatrix;

import hub.guzio.JMatrix.data.FieldType;
import hub.guzio.JMatrix.data.ProtocolInstance;
import hub.guzio.SaneServer.Logger;
import hub.guzio.SaneServer.Response;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class AdhdProtocol extends Protocol {
    public AdhdProtocol(Logger logger) throws URISyntaxException {
        super(Map.of("example", new FieldType("meow", "meow")), new URI("http://guziohub.ovh/assets/logo/favicon.svg"), new String[]{"example"}, new String[]{"example"}, "bettertest", logger);
    }

    @Override
    public Map<String, Map<String, String>> onLocationQueryByAlias(@NotNull Optional<String> alias) {
        if (alias.isPresent()) protolog.log("Got request for alias: "+alias.get());
        else protolog.log("Got request for any alias.");
        return Map.of();
    }

    @Override
    public Optional<Response> onLocationQueryByProtocol(@NotNull Map<String, String> args) {
        protolog.log("Got request for location with args: KEYS="+Arrays.toString(args.keySet().toArray())+";VALUES="+Arrays.toString(args.entrySet().toArray()));
        return Optional.empty();
    }

    @Override
    public ProtocolInstance[] onInstancesQueryByProtocol() {
        protolog.log("Returning a meow-stance list...");
        return new ProtocolInstance[]{
                new ProtocolInstance("The Cathouse", Map.of("example", "meow"), Optional.empty(), "mrrp")
        };
    }

    @Override
    public Map<String, Map<String, String>> onUsersQueryById(@NotNull Optional<String> MxId) {
        if (MxId.isPresent()) protolog.log("Got request for MXID: "+MxId.get());
        else protolog.log("Got request for any MXID.");
        return Map.of();
    }

    @Override
    public Optional<Response> onUsersQueryByProtocol(@NotNull Map<String, String> args) {
        protolog.log("Got request for users with args: KEYS="+Arrays.toString(args.keySet().toArray())+";VALUES="+Arrays.toString(args.entrySet().toArray()));
        return Optional.empty();
    }
}