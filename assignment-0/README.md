# Assignment 0
## Setup and use of CNSS (Computer Networks Simple Simulator)

RC2020 Labs will use the CNSS tool for part of the semester and for some of your work-assignments and evaluation. CNSS makes it possible to code and simulate simple networking protocols. The simulation results are deterministic and repeatable, making it easier to reproduce and interpret them.
As you will see, CNSS is a piece of software that you will use in some of your work-assignments. It is a simple Network Simulation tool, allowing the definition of a network topology (defined in configuration files), with nodes interconneted by links. With CNSS you can program the processing behavior of nodes in the defined network and implementing protocolos involving message flows between different nodes.

CNSS is written in Java 8. The source code is publicly available and hosted at GitHub.

*Obs) You have this Assignment-0 (composed by 3 STEPS) also available as a Live-Version using a Jupyter Notebook / Google Collaboratory.
      You can try using the following links:*
    
```
Step 1 - CNSS Fetching and Setup, [here](https://colab.research.google.com/drive/1W4v1QbdbXsLJg0TRIe6gX__gQ5H3eoDH?authuser=1)
Step 2 - More about CNSS and CNSS, [here](https://colab.research.google.com/drive/1UJL8ajF0E0tfi1v79598DIwfjkGmJTXV?authuser=1)
Step 3 - More About CNSS Nodes, [here](https://colab.research.google.com/drive/1dUoIill4aUDSJGamXq4v0vcYu2gnynjp?authuser=1)

## STEP 1) Fetch and Setup the CNSS Simulator

In this asignment you will learn how to use CNSS.

### How can you obtain CNSS ?

Fetching CNSS For those still unfamiliar with GitHub repositories, there are several ways to access the source code.

You can download a zip archive of the entire repository from here (https://github.com/jlegatheaux/cnss)

### Fetching by using the git tool (shell)

You can use git (https://git-scm.com/) to clone the contents of the repository to a local directory, like so:

```
$ git clone https://github.com/smduarte/cnss.git
```
This will create a directory cnss in the current directory with a local repository with the latest version.
You can refresh and update the local repository, by executing the following command in the repository folder:

```
$ git -C cnss pull
```
You must see .... Already up to date.

### For Eclipse Users

CNSS can also be imported directly to Eclipse.

For version 2020-06, the procedure is as follows:

Copy the CNSS repository uri (https://github.com/jlegatheaux/cnss.git) into the clipboard; 
Navigate: File > Import > Git, choose Projects from Git, then Next Choose Clone URI, then Next twice. 
If asked, only select the master branch and press Next until finished.

### Compiling CNSS manually (Console)

CNSS has no external dependencies.

At the root of the repository, the source code can be compiled in the command line (shell), like so:

```
$ javac -d cnss-classes cnss/CNSS/src///*.java
obs) pay attention: The switch -d cnss-classes will place the resulting classes in the cnss-classes directory.
```

### How to run CNSS

To run a CNSS simulation you must create/have a configuration file.
As you will see, the configuration file will be passed as argument to CNSS.
For the next example we we will use an empty configuratuon file ... 
Later you will learn how to use this file to describe a "network topology".

Here is a sequence of (shell) commands you can use:

To create an empty file:
```
$ echo > empty.config.txt
```
Then you are ready to compile and run the simulator using the (emptu) configuration file as a given argument

```
javac -d cnss-classes cnss/CNSS/src///*.java
```
Then you will see as output the following log ...

```
Loading configuration : empty.config.txt Reading file empty.config.txt

simulation starts - first processing step with clock = 0

simulation ended - last processing step with clock = 0
```
Ok, great, if you see the above log, you concluded the setup of CNSS.
You are ready to go to STEP 2, to know more about the CNSS simulations and programming with CNSS.


## STEP 2) More About CNSS (Computer Networks Simple Simulator)

The CNSS simulator is piece of software that executes actions in response to events. To make the execution of a simulation deterministic and repeatable, the ordering of events is logical and fixed, and does not depend on the actual time taken to execute the actions, which are treated as instantaneous.
Given the computer networks context, an event in CNSS can be, for example, the delivery of a message at some node (when the node send a message to a destination) or the indication that a timeout has expired...
A more compreenhesive description of CNSS can be found [here](https://github.com/jlegatheaux/cnss).

### CNSS Nodes

A simulation will comprise of a number of nodes (in the simulated network). Nodes can be used to simulate simple end-systems (application hosts) and other lower-level networking components, such as *routers* and *switches*.

### Programming Application Nodes

The processing behavior end-system application nodes is coded in a Java class that **implements** the [ApplicationAlgorithm](https://github.com/jlegatheaux/cnss/blob/master/src/cnss/simulator/ApplicationAlgorithm.java) interface.

The CNSS codebase also provides a convenience class [AbstractAplicationAlgorithm](https://github.com/jlegatheaux/cnss/blob/master/src/cnss/lib/AbstractApplicationAlgorithm.java) that can be extended as needed. Using this class we avoid the need to provide implementions for interface methods that are not used. 
As a bare minimum, we only need to provide a public no-args constructor and implement the **initialize** method of our node.

As an example, consider the following class:

```
%%writefile MinimalNode.java

import java.util.Arrays;
import cnss.simulator.*;
import cnss.lib.*;

public class MinimalNode extends AbstractApplicationAlgorithm {

  public MinimalNode() {
      super(true, "minimal-node");
  }

  public int initialise(int now, int node_id, Node self, String[] args) {
    super.initialise(now, node_id, self, args);

    super.log( now, "args: " + Arrays.asList(args));
		return 0;
	}
} 
```

In the example, the public no-args constructor calls the super constructor with true, requesting logging to be enabled and providing the string minimal-node to be used as prefix identifying this node in log messages, in your simulation runtime.
In the initialize method we pass the arguments to the superclass to be stored in superclass fields. The superclass provides a logging method we call to print that information.

### More About Configurations

As mentioned in Step1, in the configuration file we add nodes to a simulation, using the following syntax; each in a separate line:

```
node <id> <interfaces> <control-class> <application-class> <arg1> ... <argn>

where:
<id> is an integer id of the node;
<interfaces is the number of network interfaces attached to the node;
<control-class> is the class providing the default routing logic for the interfaces;
<application-class>is the class that implements the node high-level logic;
<arg1> ... is a space separated list of string arguments
```

### Example of a Configuration File

The example below shows a configuration file for a simulation that will have 2 nodes. EndSystemControl is a sample control-class already provided by the CNSS simulator.

```
%%writefile minimal-node.config.txt

node 0 0 cnss.lib.EndSystemControl MinimalNode arg1 arg2 
node 1 0 cnss.lib.EndSystemControl MinimalNode arg3 arg4
```

### Simulation Execution

The following Unix commands compile and execute the simulator
(in this case we use the Terminal/Shell environment:

Remembering: to fetch and compile the simulator (do you have it already done in Step 1 ?)

```
$ git clone https://github.com/jlegatheaux/cnss.git 2> /dev/null || git -C cnss pull
$ javac -d cnss-classes cnss/src/*/*/*.java
```
Now you can compile your Minimal Node (programmed above)
```
$ javac -cp .:cnss-classes MinimalNode.java
```
And then you can run the simulation with the node
```
java -cp .:cnss-classes cnss.simulator.Simulator minimal-node.config.txt
```
### Periodic Actions

Nodes can request a periodic action to be executed, periodically, ie., at regular intervals. 
This is done by:
- Returning a positive value (in milliseconds) in the initialize method;
- Implementing the on_clock_tick method with the desired behavior to be executed periodically.

Example:
```
%%writefile PeriodicActionNode.java

import java.util.Arrays;
import cnss.simulator.*;
import cnss.lib.*;

public class PeriodicActionNode extends AbstractApplicationAlgorithm {

  public PeriodicActionNode() {
      super(true, "periodic-node");
  }

  public int initialise(int now, int node_id, Node self, String[] args) {
    super.initialise(now, node_id, self, args);
		return 1500;
	}

  public void on_clock_tick(int now) {
    super.log( now, "on_clock_tick");      
  }

}
```
In the updated example above, we request that on_clock_tick to be called every 1500 milliseconds, as measured in simulation virtual time (not realtime).

The updated configuration below, adds a stop parameter line to terminate the simulation after 10 000 milliseconds of virtual time.
```
%%writefile periodic-action.config.txt

node 0 0 cnss.lib.EndSystemControl PeriodicActionNode 
node 1 0 cnss.lib.EndSystemControl PeriodicActionNode

parameter stop 10000

Try to understand what is defined in the configuration file.
```
### Compilation and execution 

The same as above, but now for the new configuration file and for the programmed Periodic Actions.

%%bash
```
CNSS Fetching and compilation (as already done)
$git clone https://github.com/jlegatheaux/cnss.git 2> /dev/null || git -C cnss pull
$javac -d cnss-classes cnss/src/*/*/*.java

$javac -cp .:cnss-classes PeriodicActionNode.java
$java -cp .:cnss-classes cnss.simulator.Simulator periodic-action.config.txt
```
### Timeout Events

Application nodes can also schedule an operation to execute in the future, by setting a timeout. 
This is achieved via the `set_timeout( int )` method in the [Node](https://github.com/jlegatheaux/cnss/blob/master/src/cnss/simulator/Node.java) class.

When the timeout deadline expires, the `on_timeout()` method, the node implements to satisfy the [ApplicationAlgorithm](https://github.com/jlegatheaux/cnss/blob/master/src/cnss/simulator/ApplicationAlgorithm.java) interface that is called by the simulator. 
Timeout events are automatically cancelled in some cases that will be discussed later on...

### Example using Timeout events
```
%%writefile TimeoutHandlingNode.java

import java.util.Arrays;
import cnss.simulator.*;
import cnss.lib.*;

public class TimeoutHandlingNode extends AbstractApplicationAlgorithm {

  public TimeoutHandlingNode() {
      super(true, "timeout-handling-node");
  }

  public int initialise(int now, int node_id, Node self, String[] args) {
    super.initialise(now, node_id, self, args);
    self.set_timeout(1000 + node_id);
		return 0;
	}

  public void on_timeout(int now) {
    super.log( now, "on_timeout");      
  }

}
```

Now we define the configuration file:
```
%%writefile timeout-handling.config.txt

node 0 0 cnss.lib.EndSystemControl TimeoutHandlingNode 
node 1 0 cnss.lib.EndSystemControl TimeoutHandlingNode

parameter stop 10000
```
And then we can execute the simulation to see the effect ...
```
%%bash

Fetching and compiling the simulator (the same as above)
$git clone https://github.com/jlegatheaux/cnss.git 2> /dev/null || git -C cnss pull
$javac -d cnss-classes cnss/src/*/*/*.java

$javac -cp .:cnss-classes TimeoutHandlingNode.java
$java -cp .:cnss-classes cnss.simulator.Simulator timeout-handling.config.txt
```

See the output log. You must interpret and analyze what is there ...

Ok, now you concluded the STEP 2 for the Assignment 0. Is time to take a break amd then you go for the last STEP 3

## STEP 3) More About Programming Nodes in CNSS

Now we will learn a little bit more on Nodes and Links.

Let's go for the following eaxmple of a SelfSenderNode.

```
%%writefile SelfSenderNode.java

import java.util.Arrays;
import cnss.simulator.*;
import cnss.lib.*;

public class SelfSenderNode extends AbstractApplicationAlgorithm {

  public SelfSenderNode() {
      super(true, "selfsender-node");
  }

  public int initialise(int now, int node_id, Node self, String[] args) {
    super.initialise(now, node_id, self, args);

		return 500;
	}

  public void on_clock_tick(int now) {
      self.send(self.createDataPacket( super.nodeId, new byte[0]));
  }

  public void on_receive( int now, Packet p ) {
    log( now, "got: " + p); 
  }
} 
```
As "usually" you need a Configuration file (that now you know what is there):
```
%%writefile self-sender.config.txt

node 0 0 cnss.lib.EndSystemControl SelfSenderNode 

parameter stop 8000
```

You will compile and run following what you have learned in STEPS 1 and 2.
```
$git clone https://github.com/jlegatheaux/cnss.git 2> /dev/null || git -C cnss pull
$javac -d cnss-classes cnss/src/*/*/*.java

$javac -cp .:cnss-classes SelfSenderNode.java
$java -cp .:cnss-classes cnss.simulator.Simulator self-sender.config.txt
```
### Network Links

To simulate a network we need network links. Links interconnect nodes by linking two interfaces.

In the following configuration file we add links to a simulation, using the following syntax; each in a separate line:
```
link <id1>.<interface1> <id2>.<interface2> <latency> <bandwidth> <error_rate> <jitter>
where:
<id1> and <id2> identify the end nodes of the link;
<interface1> and <interface2> identify the two interfaces linked together by the link;
<latency> is the latency of the link is ms;
<bandwidth> is the transmission rate of the link in bps;
<error_rate> is the error rate in percent;
<jitter> is the jitter rate in percentage 
```
Remember the latency, bandwidth, error_rate and jitter notions from your background/lectures/theretical classes.

### Example with a Sender node and Receiver node

The example below shows a configuration file for a simulation that will have 2 nodes, connected by a single link
with certain defined characteristics:
```
%%writefile one-link.config.txt

node 0 1 cnss.lib.EndSystemControl SenderNode 
node 1 1 cnss.lib.EndSystemControl ReceiverNode

link 0.0 1.0 1000000 125 0 0

parameter stop 10000
	
```
Now we will have a Node (SenderNode) that will send messages in the network

```
%%writefile SenderNode.java

import java.util.Arrays;
import cnss.simulator.*;
import cnss.lib.*;

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
```
And we have a Receiver node - the node that will receive the messages from the sender:

```
%%writefile ReceiverNode.java

import java.util.Arrays;
import cnss.simulator.*;
import cnss.lib.*;

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
```
Now we will compile, and run the somulation

```
%%bash
Fetchig the CNSS repository and compile it (or already done, Steps 1,2)
$git clone https://github.com/jlegatheaux/cnss.git 2> /dev/null || git -C cnss pull
4javac -d cnss-classes cnss/src/*/*/*.java

Let us go to compile the Nodes:

$javac -cp .:cnss-classes SenderNode.java
$javac -cp .:cnss-classes ReceiverNode.java
$java -cp .:cnss-classes cnss.simulator.Simulator one-link.config.txt
```
See the log and try to understand what is going on.

### Switches

CNSS can simulate networks where some nodes are defines to act as switches. 
For this purpose base CNSS code already provides classes to instantiate a basic switch node. 
Switch nodes do not require application-level actions, and can reply on the 
[EmptyApp](https://github.com/jlegatheaux/cnss/blob/master/src/cnss/library/EmptyApp.java) class. 
Whereas, the [FloodingSwControl](https://github.com/jlegatheaux/cnss/blob/master/src/cnss/library/FloodingSwControl.java) class implements the default packet forwarding protocol.

The configuration file we will use in this example is:

```
%%writefile two-links.config.txt

node 0 1 cnss.lib.EndSystemControl SenderNode 
node 1 1 cnss.lib.EndSystemControl ReceiverNode
node 2 2 cnss.lib.FloodingSwitch cnss.lib.EmptyApp

link 0.0 2.0 1000000 125 0 0
link 2.1 1.0 1000000 500 0 0

parameter stop 10000
```

And its done. You will have one switchinterconneting the Sender and Receiver Nodes in your Network.
There is a link interconnecting the Sender node to the Swith and another link interconnecting the
Switch and the Receiver node.

Go ahead: Cpmpile and Run ... 

```
$git clone https://github.com/jlegatheaux/cnss.git 2> /dev/null || git -C cnss pull
$javac -d cnss-classes cnss/src/*/*/*.java

and the new stuff:
$javac -cp .:cnss-classes SenderNode.java
$javac -cp .:cnss-classes ReceiverNode.java
$java -cp .:cnss-classes cnss.simulator.Simulator two-links.config.txt
```
#  Complementary tool

The Python code below allows for opening a text console in another browser tab to access the machine where this notebook is running. It is useful to check the filesystem or run Unix commands interactively...

So if you have python you can run it ...
```
!pip install kora
from kora import console
console.start()
```
Is it nice ?

If you concluded everything suucessfully in Steps 1, 2 and 3 you did a well done job. 
Congratulations you are a "mini" expert on the CNSS simulator. You are ready to go for the next week activities
and to program your future simulations and programming exercises.

At the same time you are ready to answer to the Evaluation Form abojut your Findings and Results (See in the Webpage of the course
(https://sites.google.com/campus.fct.unl.pt/rc-2020) about the dates to deliver you work and to submitt your evaluatuion form.




