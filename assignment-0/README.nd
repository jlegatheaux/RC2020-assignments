About CNSS (Computer Networks Simple Simulator)
The CNSS simulator is piece of software that executes actions in response to events. To make the execution of a simulation deterministic and repeatable, the ordering of events is logical and fixed, and does not depend on the actual time taken to execute the actions, which are treated as instantaneous.
Given the computer networks context, an event in CNSS can be, for example, the delivery of a message at some node or the indication that a timeout has expired...
A more compreenhesive description of CNSS can be found here.
CNSS Networking.

# Programming Application Nodes (continued)


```jav
import java.util.Arrays;
import cnss.simulator.*;
import cnss.utils.*;

public class SelfSenderNode extends AbstractApplicationAlgorithm {

  public SelfSenderNode() {
      super(true, "selfsender-node");
  }

  public int initialise(int now, int node_id, Node self, String[] args) {
    super.initialise(now, node_id, self, args);

		return 1000;
	}

  public void on_clock_tick(int now) {
      self.send(self.createDataPacket( super.nodeId, new byte[0]));
  }

  public void on_receive( int now, Packet p ) {
    log( now, "got: " + p); 
  }
}
In [ ]:
%%writefile self-sender.config.txt

node 0 0 cnss.examples.EndSystemControl SelfSenderNode 


parameter stop 10000
Compiling and Execution...
In [ ]:
%%bash
# Fetch the CNSS repository and compile it
git clone https://github.com/smduarte/cnss.git 2> /dev/null || git -C cnss pull 2> /dev/null
javac -d cnss-classes cnss/CNSS/src/*/*/*.java


javac -cp .:cnss-classes SelfSenderNode.java
java -cp .:cnss-classes cnss.simulator.Simulator self-sender.config.txt
Network Links
To simulate a network we require network links. Links inter-connect nodes by linking two interfaces.
Configuration
In the configuration file, we add links to a simulation, using the following syntax; each in a separate line:
link <id1>.<interface1> <id2>.<interface2> <latency>  <bandwidth> <error_rate> <jitter>
where:
<id1> and <id2> identify the end nodes of the link;
<interface1> and <interface2> identify the two interfaces linked together by the link;
<latency> is the latency of the link is ms;
<bandwidth> is the transmission rate of the link in bps;
<error_rate> is the error rate in percent;
<jitter> is the jitter rate in percent of ???
Example
The example below shows a configuration file for a simulation that will have 2 nodes, connected by a single link.
In [ ]:
%%writefile one-link.config.txt

node 0 1 cnss.examples.EndSystemControl SenderNode 
node 1 1 cnss.examples.EndSystemControl ReceiverNode

link 0.0 1.0 1000000 125 0 0

parameter stop 10000
In [ ]:
%%writefile SenderNode.java

import java.util.Arrays;
import cnss.simulator.*;
import cnss.utils.*;

public class SenderNode extends AbstractApplicationAlgorithm {

  public SenderNode() {
      super(true, "sender-node");
  }

  public int initialise(int now, int node_id, Node self, String[] args) {
    super.initialise(now, node_id, self, args);
		return 1000;
	}

  public void on_clock_tick(int now) {
      self.send(self.createDataPacket( 1, new byte[0]));
  }
}
In [ ]:
%%writefile ReceiverNode.java

import java.util.Arrays;
import cnss.simulator.*;
import cnss.utils.*;

public class ReceiverNode extends AbstractApplicationAlgorithm {

  public ReceiverNode() {
      super(true, "receiver-node");
  }

  public int initialise(int now, int node_id, Node self, String[] args) {
    super.initialise(now, node_id, self, args);
		return 0;
	}

  public void on_receive( int now, Packet p ) {
    log( now, "got: " + p); 
  }
}
Execution
In [ ]:
%%bash
# Fetch the CNSS repository and compile it
git clone https://github.com/smduarte/cnss.git 2> /dev/null || git -C cnss pull 2> /dev/null
javac -d cnss-classes cnss/CNSS/src/*/*/*.java


javac -cp .:cnss-classes SenderNode.java
javac -cp .:cnss-classes ReceiverNode.java
java -cp .:cnss-classes cnss.simulator.Simulator one-link.config.txt
Switches/Router
CNSS can simulate networks where some nodes are switches/routers.
The base CNSS code already provides classes to instantiate a basic router/switch node. Such nodes do not require application-level actions, and can reply on the EmptyApp class. Whereas, the FloodingSwControl class implements the default packet forwarding protocol.
Example configuration
In [ ]:
%%writefile two-links.config.txt

node 0 1 cnss.examples.EndSystemControl SenderNode 
node 1 1 cnss.examples.EndSystemControl ReceiverNode
node 2 2 cnss.examples.FloodingSwControl cnss.examples.EmptyApp

link 0.0 2.0 1000000 125 0 0
link 2.1 1.0 1000000 500 0 0

parameter stop 10000
In [ ]:
%%bash
# Fetch the CNSS repository and compile it
git clone https://github.com/smduarte/cnss.git 2> /dev/null || git -C cnss pull 2> /dev/null
javac -d cnss-classes cnss/CNSS/src/*/*/*.java


javac -cp .:cnss-classes SenderNode.java
javac -cp .:cnss-classes ReceiverNode.java
java -cp .:cnss-classes cnss.simulator.Simulator two-links.config.txt
In [ ]:
!pip install kora
from kora import console
console.start()
