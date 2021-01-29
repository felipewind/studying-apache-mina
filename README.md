# studying-apache-mina

Project to study Apache Mina

# Introduction

Apache MINA = Multi-Purpose Infrastructure for Network Applications.

# Source of information for this study

https://mina.apache.org/mina-project/quick-start-guide.html

https://www.devmedia.com.br/conhecendo-o-apache-mina-framework/27081

# Synchronicity

Based on the NIO API (Non-Blocking I/O).

It is asynchronous.

In a synchronous connection, one thread must wait the response to a request.

In the asynchronous model, one thread can deal with multiple connections.

# TCPServer

## Classes
- TCPServer
- ServerHandler

## Steps to the test

Execute the TCPServer.

We will have a TCP/IP server running on port 5050 capable of processing text commands.
 
Command to connect with the TCPServer
```
telnet 127.0.0.1 5050
```

Commands that can be sent to the TCPServer
- `data` - returns the date/time from the system
- `quit` - finish the session
- `any other command` - returns as an echo




