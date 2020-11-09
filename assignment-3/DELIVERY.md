# A smart client to download content from HTTP servers 

## Motivation
In today's internet, most of the content consumed by users is transferred over the HTTP protocol. In the specific case of multimedia content, the volume of the consumed information varies from a few Mbytes, in the case of photographs, up to several Gbytes, in the case of movies.

It is not realistic to think of such bulky objects being transferred in a single HTTP request/reply interaction, using a single TCP connection. Inevitably, due to the high volume of data of such content, momentary anomalies in the network, or problems in the servers, it is necessary to resort to more than one interaction among the client and the server(s). In addition, in the case of movies, as they can take several hours to play, it is not mandatory or interesting to transfer in only one chunk the full content, or from the same server. Also, a faster download may be achieved if transferring in parallel from several servers, using different HTTP ranges.

## Goals
To complete this assignment you must program an HTTP client capable of transfering a large file (e.g., above 100 Mbytes) from a set of HTTP "tricky" servers, in the shortest time. These servers, whenever they receive a request of an object, may only send part of the requested object or break the connection in the middle of the transfer. Also, each server has different and variable transfer performances. These servers accept HTTP range requests.

### Minimal goal of your work
Your program should be an HTTP client called **GetFile** that must be able to correctly download a (huge) file from a HTTP server. The server may send just part of the file at each request or the connection with this server can be broken. For your implementation and testing purposes, you can use the provided server **HttpTrickyServer.jar**, [downloadble here](./exemplo3/HttpTrickyServer.jar) 

[Files for testing purposes are available here.](https://drive.google.com/drive/folders/146LsGoiD2SOUYMjkBeSDbmndzPxUxY-m?usp=sharing)

Use the following command to start this server (it runs by default on port 8080):

`java -jar HttpTrickyServer.jar`

As an example, for downloading the IFB.mp4 movie trailer in your computer, your client will be run in the following way:

`java GetFile http://localhost:8080/IFB.mp4`

Your implementation, when started, always downloads a fresh copy of the file.

### Extra goals of your work
Your program should be able to correctly download a (huge) file from a set of HTTP "tricky" servers. 

For testing, you can start a pool of several servers in the same machine, but you must use a different port for each one. On Unix-like system you can use a command sequence like the following (all servers launched in background):

`java -jar HttpTrickyServer.jar 8080 &`
`java -jar HttpTrickyServer.jar 8081 &`
`java -jar HttpTrickyServer.jar 8082 &`
`java -jar HttpTrickyServer.jar 8083 &`

Note: You can also use the available script [available here](./exemplo3/serverclusterstart.sh) to launch the four servers.

As an example, for downloading the IFB.mp4 movie trailer in your computer, from the 4 above servers, also in your computer, your client will be run in the following way:

`java GetFile http://localhost:8080/IFB.mp4 http://localhost:8081/IFB.mp4 http://localhost:8082/IFB.mp4 http://localhost:8083/IFB.mp4`

In your implementation you can implement whatever solution you prefer:

**a)** Sending a set of successive requests to the same server;

**b)** Sending requests to several different servers, for example in a round-robin way

**c)** Sending requests to several different servers, in parallel. 

**d)** Any other policy, according to the evolution of your observations.

Note: each server only serve one client after the previous one (it’s not a concurrent server).

### Output statistics
It is mandatory that your program collects the following informations:

```
Time elapsed to complete the full transfer (in seconds)
Total number of bytes downloaded (in bytes)
End-to-end average bitrate of the full transfer (in bytes/sec)
Number of requests performed by the client during the file transfer
Average size of the payload of each HTTP reply (in bytes)
Average request/reply duration (in milliseconds)
```

Use the following output format (from Stats.java) - numbers shown are hypothetical:

```
Transfer stats:
Total time elapsed (s):		   4
Download size (bytes):		   15000000
End-to-end debit (Kbytes/s): 8000
Number of requests:		       1500
Avg. reply length (bytes):	 10000
Avg. request duration (ms):	 1
```



## Grading (in 0-20 scale)

* A program that transfers files incorrectly, will be graded at most 6. A correct program that does not print correct statistics, will be also graded at most 6.

* A program that transfers files correctly but only contacting one server (minimal goal), will be graded at most 10.

* A program that transfers files correctly, from more than one server (but not in parallel) will be graded at most 15.

* Programs that try to achieve the best performance using all servers in parallel, have no a priori grading limitations.

* Code clarity and structure, as well as performance indications according to your used policy will be accounted.
