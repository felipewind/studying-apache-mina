package core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

// https://mina.apache.org/mina-project/quick-start-guide.html

public class MinaTimeServer {

    private static final int PORT = 9123;

    public static void main(String[] args) throws IOException {

        // An object that will be used to listen for incoming connections. Since this
        // program will be TCP/IP based, we will add a SocketAcceptor to our program.
        IoAcceptor acceptor = new NioSocketAcceptor();

        // Add a filter to the configuration
        // This filter will log all information such as newly created sessions, messages
        // received, messages sent, session closed.
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());

        // The next filter is a ProtocolCodecFilter. This filter will translate binary
        // or protocol specific data into message object and vice versa. We use an
        // existing TextLine factory because it will handle text base message for you
        // (you don’t have to write the codec part)
        acceptor.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

        acceptor.setHandler(new TimeServerHandler());

        // add in the NioSocketAcceptor configuration. This will allow us to make
        // socket-specific settings for the socket that will    be used to accept
        // connections from clients.
        //
        // There are 2 new lines in the MinaTimeServer class. These methods set the set
        // the IoHandler, input buffer size and the idle property for the sessions. The
        // buffer size will be specified in order to tell the underlying operating
        // system how much room to allocate for incoming data. The second line will
        // specify when to check for idle sessions. In the call to setIdleTime, the
        // first parameter defines what actions to check for when determining if a
        // session is idle, the second parameter defines the length of time in seconds
        // that must occur before a session is deemed to be idle.

        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

        acceptor.bind( new InetSocketAddress(PORT) );

    }

}
