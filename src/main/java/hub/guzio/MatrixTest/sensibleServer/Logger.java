package hub.guzio.MatrixTest.sensibleServer;

public class Logger {
    public void log(String msg){
        System.out.println(msg);
    }

    public void wrn(String msg){
        System.out.println(msg);
    }

    public void wrn(String msg, Throwable e){
        System.out.println(msg+e.getMessage()+"\nCaused by:"+e.getCause());
        e.printStackTrace(System.out);
    }

    public void err(String msg) {
        System.err.println(msg);
    }

    public void err(String msg, Throwable e) {
        System.err.println(msg+e.getMessage()+"\nCaused by:"+e.getCause());
        e.printStackTrace(System.err);
    }
}
