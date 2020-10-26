# Assignment 3: A smart client to download content from one or multiple HTTP servers

## Objectives

In this Assignment and its Guidelines you will learn how to program with Sockets to develop client/server applications supported by TCP and how to program clients that can transfer contents from HTTTP servers. After the initial technical introductions, divided in two parts, the final goal is to implement a Client/Server application for File Transfer based on HTTP, where clients can download files from several web-servers used in parallel, in order to maximize the file transfer rate. 
The implementation of the web-servers is provided as initial material. The assignmemt consists in the developmet of the required client implementation.

## Motivation

In today's internet, most of the users consumed content is carried over the HTTP protocol. In the specific case of multimedia contents, the volume of the consumed information varies from a few Mbytes (in the case of photographs), up to several Gbytes (in the case of movies).

It is not realistic to think of such bulky objects being transferred in a single HTTP request/reply interaction and using a single TCP connection. 

Inevitably, due to the high volumes of data, momentary anomalies in the network, or problems in the servers, it is necessary to resort to more than one interaction among the client and the server(s). In addition, in the case of movies, as they can take hours to play, it is not mandatory or interesting to transfer in only one chunk the full content, or from the same server. 

Also, a faster download may be achieved **if transferring in parallel from several servers, using different HTTP ranges**.

## Guidelines

To address the Work-Assigmnment 3 and its deliverables you must follow the following guidelines in two weeks

## [PART I - Introducton to TCP](./PART-1.md)

## [PART-II - Introduction to HTTP and Delivery](./PART-2.md)

