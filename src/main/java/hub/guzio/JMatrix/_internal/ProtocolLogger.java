package hub.guzio.JMatrix._internal;

import hub.guzio.JMatrix.Protocol;
import hub.guzio.SaneServer.Logger;

public class ProtocolLogger extends Logger {
    public final String prefix;
    public final Logger underlyingLogger;

    public ProtocolLogger(Protocol proto, Logger underlyingLogger) {
        super();
        this.prefix = "[Protocol: "+proto.name+"] ";
        this.underlyingLogger = underlyingLogger;
    }

    @Override
    public void log(String msg) {
        underlyingLogger.log(prefix+msg);
    }

    @Override
    public void wrn(String msg) {
        underlyingLogger.wrn(prefix+msg);
    }

    @Override
    public void wrn(String msg, Throwable e) {
        underlyingLogger.wrn(prefix+msg, e);
    }

    @Override
    public void err(String msg) {
        underlyingLogger.err(prefix+msg);
    }

    @Override
    public void err(String msg, Throwable e) {
        underlyingLogger.err(prefix+msg, e);
    }
}