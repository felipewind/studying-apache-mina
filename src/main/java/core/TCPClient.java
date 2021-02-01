package core;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class TCPClient {

    private static int TIMEOUT = 1000;
    private static String HOSTNAME = "127.0.0.1";
    private static int PORT = 5050;

    public static void main(String[] args) throws Throwable {

        NioSocketConnector connector = new NioSocketConnector();
        // Configurando
        configureConnector(connector);
        // Cria sessao
        IoSession session = connect(connector);
        if (session != null) {
            // Envia comandos do teclado
            sendCommands(session);
        }
        // Encerra conexoes
        close(connector, session);
    }

    /*
     *  Configura NioSocketConnector
     */
    public static void configureConnector(final NioSocketConnector connector) {
        connector.setConnectTimeoutMillis(TIMEOUT);

        connector.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
        connector.getFilterChain().addLast("logger", new LoggingFilter());

        connector.setHandler(new ClientHandler());
    }

    /*
     *  Conexao com o server
     */
    private static IoSession connect(final NioSocketConnector connector)
            throws InterruptedException {
        IoSession session = null;
        try {
            ConnectFuture future = connector.connect(new
                    InetSocketAddress(HOSTNAME, PORT));
            future.awaitUninterruptibly();
            session = future.getSession();
        } catch (RuntimeIoException e) {
            System.err.println("Failed to connect.");
            e.printStackTrace();
        }
        return session;
    }

    /*
     *  Envia comando do teclado ao servidor
     */
    private static void sendCommands(final IoSession session) {
        final Scanner scanner = new Scanner(System.in);
        String text;
        do {
            System.out.println("Entre com texto: ");
            text = scanner.nextLine();
            session.write(text);
        } while (!"quit".equalsIgnoreCase(text));
    }

    /*
     *  Encerra conexao
     */
    private static void close(final NioSocketConnector connector,
                              final IoSession session) {
        if (session != null) {
            if (session.isConnected()) {
                session.close(false);
                session.getCloseFuture().awaitUninterruptibly();
            }
        }
        connector.dispose();
    }
}
