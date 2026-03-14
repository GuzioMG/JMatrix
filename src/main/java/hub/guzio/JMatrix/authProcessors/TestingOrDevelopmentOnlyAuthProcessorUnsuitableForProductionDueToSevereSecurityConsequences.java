package hub.guzio.JMatrix.authProcessors;

import hub.guzio.JMatrix.AuthProcessor;
import hub.guzio.SaneServer.Logger;
import hub.guzio.SaneServer.Response;

import java.util.List;
import java.util.Optional;

public class TestingOrDevelopmentOnlyAuthProcessorUnsuitableForProductionDueToSevereSecurityConsequences implements AuthProcessor {
    final Logger warner;

    public TestingOrDevelopmentOnlyAuthProcessorUnsuitableForProductionDueToSevereSecurityConsequences(Logger logger) {
        warner = logger;
    }

    @Override
    public Optional<Response> onAuth(List<String> authHeader) {
        warner.wrn("USING AN UNSAFE, DEVELOPMENT-ONLY AUTH PROCESSOR! IF YOU'RE SEEING THIS MESSAGE IN PRODUCTION, STOP THIS APPSERVICE NOW, AS YOU'RE CURRENTLY NOT BEING PROTECTED AGAINST MALICIOUS HOMESERVERS IN ANY WAY!!!");
        return Optional.empty();
    }
}