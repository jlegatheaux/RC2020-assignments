# Assignment 3: File Transfers in parallel using TCP and HTTP
**Note**: The assignment and guidelines below are not in its final version. Final version will be available soon.

## A robust client to download content from multiple HTTP servers

In this Assignment and its Guidelines you will learn how to program with Sockets to program client/server applications supported by TCP and how to program clients that can transfer contents from HTTTP servers. After the initial guidance, diivded into two parts, the final goal is to implement a Client/Server application for File Transfer based on HTTP, where clients can download files from several web-servers used in parallel, in order to maximize the file transfer rate. 
For the assignmemt the implementation of the web-servers are provided as initial material. the assignmemt consists in the developmet of the required client implementation.


## Backgorund and references
### Programming with TCP Sockets in Java

- You can find a **tutorial on programming with Sockets in Java Language** in https://docs.oracle.com/javase/tutorial/networking/sockets/**
- Remember that for the work assignmemt you will be particularly focused on the develpment of clients using TCP sockets (supporting HTTP Requests/Responses) because the HTTP  
servers to be used are provided in advance.
- You also have a convenient explanation in the text book of the course: **https://legatheaux.eu/book/cnfbook-pub.pdf, see chapter 5, section 5.3**.
- Complementarily you have these examples for your preliminary tests: **echo-client.java** and **echo-sever.java**:  a very simple client/server application implementing an ECHO protocol. 
- In the part I (below) you find the initial guidelines for "Networking Programming using TCP Sockets in Java"

### Programming with HTTP using TCP Sockets
As you know, HTTP is supported by the TCP transport protocol, and it operates in two basic variants (HTTP/1.0 - implementaing HTTP Request/Response with non-persistet connections, and hTTP/1.1: using persistent connections). Clients that interact with HTTP servers must send correct HTTP requests (with the proper HEADERS), sent as formatted 
requests sent in the TCP connection previous estabished with the server. Clients must be able to receive HTTP responses, processing them according to the HTTP protocol (interpreting the HEADERS and CONTENTS in the RESPONSE).

For the operation of the HTTP protocol you must consider the explanation in the theoretical classes. 
- You can also study the HTTP protocol in the course textbook: **https://legatheaux.eu/book/cnfbook-pub.pdf, see chpter 12**, paying special attention to HTTP requests/responses using RANGE REQUESTS. 
- In the part II (below) you find the initial guildelines for "Using the HTTP Protocol to Download Digital Objects from a Server""

### Assignment Motivation

In today's internet, most of the users consumed content is carried over the HTTP protocol. In the specific case of multimedia contents, the volume of the consumed information varies from a few Mbytes (in the case of photographs), up to several Gbytes (in the case of movies).
It is not realistic to think of such bulky objects being transferred in a single HTTP request/reply interaction and using a single TCP connection. 
Inevitably, due to the high volumes of data, momentary anomalies in the network, or problems in the servers, it is necessary to resort to more than one interaction among the client and the server(s). In addition, in the case of movies, as they can take hours to play, it is not mandatory or interesting to transfer in only one chunk the full content, or from the same server. Also, a faster download may be achieved if transferring in parallel from several servers, using different HTTP ranges.

## PART I - Networking Programming using TCP Sockets in Java

Summary
- Client/Server Model with TCP
- TCP Sockets
- Java Example
- Exercise: File Transfer over TCP


## PART II -Using the HTTP Protocol to Download Digital Objects from a Server

### Goals

To complete this assignment you must program an HTTP client that must be able to transfer a voluminous file (e.g. above 100 Mbytes) from a set of HTTP "tricky" servers, in the shortest time. These "tricky" servers, whenever they receive a request of an object, may only send part of the requested object or break the connection in the middle of the transfer. Also, each server can exhibit variable transfer performances. Servers accept ranges requests from clients.

The assignmet is composed by two developments for delivering: delivery 1 and delivery 2.


##Devlivery 1

Reqeuirements will be included...

##Delivery 2

Requirements will be included...


### Additional materials
All the needed materials (testing files, programs, scripts, ...) for
the assignmet are available in this GitHub repositpry.


