package sd2223.trab1.server;

import java.net.InetAddress;
import java.net.URI;
import java.util.logging.Logger;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import sd2223.trab1.discovery.Discovery;
import sd2223.trab1.server.serverResources.UsersResource;

public class UserServer {

    protected static final int READ_TIMEOUT = 5000;
    protected static final int CONNECT_TIMEOUT = 5000;

    private static Logger Log = Logger.getLogger(UserServer.class.getName());

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s\n");
    }

    public static final int PORT = 8080;
    public static final String SERVICE = "users";
    private static final String SERVER_URI_FMT = "http://user.%s.%d/rest";

    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Use: java <path> domain");
            return;
        }

        String domain = args[0];

        try {

            ResourceConfig config = new ResourceConfig();
            config.register(UsersResource.class);

            String serverURI = String.format(SERVER_URI_FMT, domain, PORT);
            JdkHttpServerFactory.createHttpServer(URI.create(serverURI), config);

            Log.info(String.format("%s Server ready @ %s\n", SERVICE, serverURI));

            // More code can be executed here...

            Discovery disc = Discovery.getInstance();
            disc.announce(domain, SERVICE, serverURI);

        } catch (Exception e) {
            Log.severe(e.getMessage());
        }
    }
}
