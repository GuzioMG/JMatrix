package hub.guzio.MatrixTest.sensibleServer;

import java.nio.charset.StandardCharsets;

public record Response(int code, String format, String body) {
    public int length() {return getBytes().length;}
    public byte[] getBytes() {return body.getBytes(StandardCharsets.UTF_8);}
}