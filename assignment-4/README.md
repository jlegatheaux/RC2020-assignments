
# Routing Protocols Using The Flooding Algorithm

In this Assignment, we will use CNSS to develop a routing protocol based on the Flooding Algorithm. Students must enhance a provided naive flooding solution by introducing filtering and optimization capabilities in the forwarding process implemented by *switching nodes*.

A deeper discussion of the subject is available in **chapter 15** of the course support book.

The basic flooding algorithm is very simple. It requires switching nodes to maintain **no state** besides the set of their active interfaces. In short, whenever there is a packet not addressed to the receiving node to forward, the algorithm simply sends a copy of that packet to all the node's interfaces, except the one from which the packet arrived.

If the network is a **tree, i.e., it has no cycles**, the packet will reach the destination, provided there is a path from the origin node to destination one. The only drawback is that several useless copies of the same packet will arrive to other nodes of the network but the original sender. These useless copies are discarded by all non destination nodes, i.e., nodes whose adddress is different from the destination address of the packet.

In this assignment we will use the network depicted in the figure below to test and develop the different versions of the algorithm. Initially, we will consider a state of that network where the link connecting nodes 4 and 5, as well as the link connectiong nodes 3 and 0, are both shutdow. As such, the network will have no cycles.

![The network used for testing the different versions of the algorithm](Figures/assign4.1.png)

In this network some nodes act as Sender nodes and periodically send a *ping like packet* to the receiver node in variable `dest` of their code, as shown in their `on_clock_tick()` upcall below. In the same code fragment are also shown upcalls `on_receive()` and `showState()`.

```java
public void on_clock_tick(int now) {
	countSent++;
	byte[] pl = Integer.toString(countSent).getBytes();
	DataPacket p = self.createDataPacket(dest,pl);
	self.send(p);
	log(now, " sent "+p+" payload "+countSent);
}

public void on_receive(int now, DataPacket p) {
	countReceived++;
	log(now, " received "+p+" payload: " + new String(p.getPayload(), StandardCharsets.UTF_8)); 
}
	
public void showState(int now) {
	System.out.print("Node status for node "+node_id+" ("+NAME+") "+"time "+now+" -");
	System.out.println(" sent "+countSent+" packet(s)"+" / received "+countReceived+" packet(s)");
}
```
Similar to your well known *echo service*, receiver nodes reply to the original sender of these packets.

```java
public void on_receive(int now, DataPacket p) {
	countReceived++;
	countSent++;
        self.send( self.createDataPacket( p.getSource(), p.getPayload() )  );
	log(now, " received / replyed "+p+" payload: " + new String(p.getPayload(), StandardCharsets.UTF_8)); 
}

public void showState(int now) {
	System.out.print("Node status for node "+node_id+" ("+NAME+") "+"time "+now+" -");
	System.out.println(" sent "+countSent+" packet(s)"+" / received "+countReceived+" packet(s)");
}
```
The code below shows the method `flood_packet` which is used by the ´ControlAlgorithm´ of all switching nodes to forward packets not directly addressed to them.

```java
private void flood_packet (int now, Packet p, int iface) {
	int copiesSent = 0;
	// do the flood
	for (int i = 0; i < links.length; i++) {
		if (i != iface && links[i].isUp()) {
			// always send a copy of p, not the object itself
			nodeObj.send(p.getCopy(), i);
			copiesSent++;
		}
	}
	if (copiesSent == 0) { // allows the local node to count dropped packets
		nodeObj.send(p, UNKNOWN);
	}
	trace(now, "forwarded " + copiesSent + " packet copy(ies)");
}
```
In the above method, parameter *iface* is the interface from which packet *p* has been received. The variable *links* of classes extending `ControlAlgorithm` is an array representing the interfaces (and the attached links) of the node. The control algorithm of sender and receiver nodes, as they only have one single interface, use a simpler `ControlAlgorithm` provided with the basic library of CNSS.

Configuration file [configs/config4.1](configs/config4.1) allows you to make a first simulation test of the basic naive flooding algorithm, provided in class [Flood.java](Flood.java), by issuing the following command (or a equivalent action compatible with your development environment):

```
java -cp bin:cnss/bin cnss.simulator.Simulator configs/config4.1
```
It is possible to analyse statistics on how the flood routing process is executed, since, at the end of the simulation, sender and receiver nodes print their final statistics (`dumpappstate 39000 all`) and all nodes also print statistics on the numbers of packets they processed (`dumppacketstats 39000 all`). In the printed lines of these last statistics, characters used before values mean the following (in all the presented cases, the corresponding counters have been incremented when the condition occurs):

* **s - sent** (a packet was sent by the node or the link)
* **r - received** (a packet was received by the node or the link)
* **d - dropped** (a packet was dropped because its TTL was going to reach 0 or there was no available interface to forward it)
* **f - forwarded** (the forwarded algorithm of the node has been called)

If you want to have a detailed look on how the [configs/config4.1](configs/config4.1) simulation progresses, you can uncomment the line `parameter trace` which will trigger the printing of tracing messages by the different control algorithms. That allows one to a have a much clear idea on how the routing of packets is progressing.

After that, you can proceed to simulations with different network configurations, namely, one that at processing step 18000 puts the link from node 0 to node 3 up, see file [configs/config4.2](configs/config4.2), as shown in the figure below.

![The network used for test configuration config4.2](Figures/assign4.2.png)

From that moment on, the network has cycles and becomes similar to a ring network. You can test it by running the next simulation:

```
java -cp bin:cnss/bin cnss.simulator.Simulator configs/config4.2
```
As you can see, things now perform very differently. Not only because nodes receive many duplicate packets, but also because the only way a flood stops is because something special arises to the forwarded packets. Guess what and explain why it happens. If you want to look at the deluge of events, you must run the simulation with tracing on. 

Another interesting observation is to compare the time a flood takes to end when the network has no cycles (afer the first packet is sent at time = 10000), to the time it takes when the network is not acyclic. You can look at it by using command `less` to stop the progress of the printing of the simulation results (touch space to continue).

```
java -cp bin:cnss/bin cnss.simulator.Simulator configs/config4.2 | less
```
You can abort the simulation as soon as you got the comparison done. You can try to justify the values by looking at the initial value of the TTL parameter of packets and the paths they use in the network. You can also try to devise a simple method to make the flood stop earlier. Can your methdod be generalised to any network?

# Your First Delivery (that can be marked at most 14 marks)

The goal of your first delivery is to enhance the `Flood`class provided to also implement a flooding optimisation known as **learning by the reverse path** which leverages the fact that if in an acyclic network, if a node `N` receives by interface `I` a packet originally sent by source `S`, then `I` is the beginning of a (unique and therefore shortest) path from `N` to `S`. After implementing it, to switch this optimization on, the only required action should be to uncomment the line  `parameter filter` in the configuration file.

By using file [configs/config4.1](configs/config4.1), with filtering on and off, it is easy to see that the number of duplicate packets drops with filtering on. This is easier to grasp with tracing on. This optimisation seems to have a radical implication since when performing simulation [configs/config4.2](configs/config4.2), with filtering on and off, it seems that  **learning by the reverse path** is capable of avoiding duplicates per se. 

Performing the same simulations with file [configs/config4.3](configs/config4.3), wich uses the network shown below, a network with all links up after time = 18000, and therefore more cycles than the previous ones, one realises that with filtering off, floods seem never end, while doing the same simulation with filtering on, the number of duplicates drops radically and, once again, floods stop quite soon.

![The network used for test configuration config4.3](Figures/assign4.3.png)

However, is it possible to prove that this kind of filtering is capable of avoiding most floods and the receiving of all duplicate packets? It is not possible to prove it because this is a false assertion. To prove that it is not true, it is enough to find a counterexample. For this, you can try the same simulations with a new version of sender nodes. One where each sender also sends a packet to a switch node using, like for example, the upcall `on_clock_tick` below

```java
public void on_clock_tick(int now) {
	countSent++;
	byte[] pl = Integer.toString(countSent).getBytes();
	DataPacket p = self.createDataPacket(dest,pl);
	self.send(p);
	countSent++;
	p = self.createDataPacket(5,pl);
	self.send(p);
	log(now, " sent "+p+" payload "+countSent);
}
```
by wich sender nodes also send a packet to the switching node number 5 at each clock tick. Running again the same simulation, even with filtering on, one realises that node 5 instead of receiving 6 packets from these nodes, receives 114 packets, while all other nodes received 0. Switching nodes use an empty `ApplicationAlgorithm` class and therefore ignore the received application packets.

The method capable of avoiding all duplicate packets during floods is simple, it requires switching nodes to detect and drop them. Implementing it is your job in this assignment.

# Assignment Delivery 

The provided class `Flood`, when the parameter `drop_duplicates` is set, should execute all the required actions to implement duplicate packets dropping.

```java
public void forward_packet(int now, Packet p, int iface) {

	if ( drop_duplicates ) {
		// do the required actions to recognise packet duplicates
		// if the packet is a duplicate, ignore it and return, else continue the processing
	} 
	
	if ( filter ) {
		// do the required actions to learn by the reverse path and
		// if possible, perform an optimised forward, else flood the packet
		return;
	}
	
	flood_packet (now, p, iface);
}
```
To achieve this goal, you have to devise a technique of detecting duplicates by keeping the least state possible on the packets previously forwarded.

Your solution must forward the least possible number of packets using configuration [configs/config4.4](configs/config4.4), a network with all links up from the beginning, as shown in the figure below.

![The network used for test configuration config4.4](Figures/assign4.4.png)

After executing that simulation, all application nodes (sender and receiver nodes) only send 3 packets and receive 3. They also drop exactly one packet received by their only link. Can you explain both facts? 

Switching nodes forward the minimal number of packets while using flooding, and drop the received duplicate packets. Recall that switching nodes 3 and 5 only receive the first useless floods and then, as the network learned where destinations nodes are, do not receive any more useless packets. Switching nodes 1, 2, 4 and 5 receive and forward the packets sent and received from the attached application nodes (6 packets), as well as some extra packets also flooded in the first round.

```
Node status for node 6 (sender) time 39000 - sent 3 packet(s) / received 3 packet(s)
Node status for node 7 (receiver) time 39000 - sent 3 packet(s) / received 3 packet(s)
Node status for node 8 (sender) time 39000 - sent 3 packet(s) / received 3 packet(s)
Node status for node 9 (receiver) time 39000 - sent 3 packet(s) / received 3 packet(s)
Pkt stats for node 0 time 39000 -  s 4 r 0 d 2 f 4
  (node:0 ifc:0) r 1 s 1 <--> (node:1 ifc:0) r 1 s 1
  (node:3 ifc:1) r 1 s 2 <--> (node:0 ifc:1) r 2 s 1
  (node:5 ifc:1) r 2 s 1 <--> (node:0 ifc:2) r 1 s 2
Pkt stats for node 1 time 39000 -  s 9 r 0 d 1 f 8
  (node:0 ifc:0) r 1 s 1 <--> (node:1 ifc:0) r 1 s 1
  (node:1 ifc:1) r 4 s 4 <--> (node:2 ifc:0) r 4 s 4
  (node:1 ifc:2) r 3 s 4 <--> (node:6 ifc:0) r 4 s 3
Pkt stats for node 2 time 39000 -  s 9 r 0 d 1 f 8
  (node:1 ifc:1) r 4 s 4 <--> (node:2 ifc:0) r 4 s 4
  (node:2 ifc:1) r 1 s 1 <--> (node:3 ifc:0) r 1 s 1
  (node:2 ifc:2) r 3 s 4 <--> (node:7 ifc:0) r 4 s 3
Pkt stats for node 3 time 39000 -  s 4 r 0 d 1 f 3
  (node:2 ifc:1) r 1 s 1 <--> (node:3 ifc:0) r 1 s 1
  (node:3 ifc:1) r 1 s 2 <--> (node:0 ifc:1) r 2 s 1
  (node:3 ifc:2) r 1 s 1 <--> (node:4 ifc:0) r 1 s 1
Pkt stats for node 4 time 39000 -  s 9 r 0 d 1 f 8
  (node:3 ifc:2) r 1 s 1 <--> (node:4 ifc:0) r 1 s 1
  (node:4 ifc:1) r 4 s 4 <--> (node:5 ifc:0) r 4 s 4
  (node:4 ifc:2) r 3 s 4 <--> (node:8 ifc:0) r 4 s 3
Pkt stats for node 5 time 39000 -  s 9 r 0 d 2 f 9
  (node:4 ifc:1) r 4 s 4 <--> (node:5 ifc:0) r 4 s 4
  (node:5 ifc:1) r 2 s 1 <--> (node:0 ifc:2) r 1 s 2
  (node:5 ifc:2) r 3 s 4 <--> (node:9 ifc:0) r 4 s 3
Pkt stats for node 6 time 39000 -  s 3 r 3 d 1 f 4
  (node:1 ifc:2) r 3 s 4 <--> (node:6 ifc:0) r 4 s 3
Pkt stats for node 7 time 39000 -  s 3 r 3 d 1 f 4
  (node:2 ifc:2) r 3 s 4 <--> (node:7 ifc:0) r 4 s 3
Pkt stats for node 8 time 39000 -  s 3 r 3 d 1 f 4
  (node:4 ifc:2) r 3 s 4 <--> (node:8 ifc:0) r 4 s 3
Pkt stats for node 9 time 39000 -  s 3 r 3 d 1 f 4
  (node:5 ifc:2) r 3 s 4 <--> (node:9 ifc:0) r 4 s 3
```

Your solution may be confronted with other network and application configurations. **If it is correct and with good code quality, its mark will be at most 17. This remains open by the moment and is only valid if the Exrra Assignment applies.**


# Extra Assignment Delivery 

An useful feature in some scenarios is the abality to broadcast a packet to all nodes in a network. In networks that support it, a packet is broadcasted by sending it to the boadcast address. In CNSS a DataPacket can be broadcast using destination address `Packet.BROADCAST`, that special destination has the value `11111`.

Broadcasting requires a special algorithm to implement it. Fortunately enough, that algorithm is based on flooding with duplicate detection. Your switching node forwarding algorithm, if it already supports the two above features, namely flooding and duplicate detection, is very close to implement broadcasting.

A broadcasted packet, if it is not a duplicate, must be flooded and also locally delivered. To locally deliver a packet, its destination address **must** be changed to the **local node address** and delivered using the call

```java
nodeObj.send(p.getCopy(), LOCAL);
```
Your new version of the `Flood` control algorithm should be named `FloodB`.

To complete your extra delivery, you also need to see and test if the `ControlAlgorithm` class used by application nodes requires any change. That class is also avaliable under the name `EndSystemForwarding` in the sources folder. An end system has only one interface and its number is 0.

Finally, you can use a new configuration file to test your solution, see file [configs/config4.5](configs/config4.5). This configuration uses the same network configuration as the one in file [configs/config4.4](configs/config4.4). That network is depicted in the last figure shown above.

**If you complete this modification with success, your work may be graded with 3 extra marks.**
