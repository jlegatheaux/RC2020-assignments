

// the control (routing) of an end system with one only interface

import cnss.simulator.*;
import cnss.lib.*;

public class EndSystemForwarding extends AbstractControlAlgorithm {
	
	private Node nodeObj;
	private int nodeId;
	private GlobalParameters parameters;
	private Link[] links;
	private int numInterfaces;
	private String name="end system";
	private boolean tracingOn = false;

	public EndSystemForwarding() {
		super("end system");
	}

	public int initialise(int now, int node_id, Node mynode, GlobalParameters parameters, Link[] links, int nint) {
		super.initialise(now, node_id, mynode, parameters, links, nint);
		if ( parameters.containsKey("trace") ) {
			super.set_trace(true);
		}
		if ( nint > 1 ) {
			super.set_trace(true);
			trace(now,"end system has more than one interface");
			System.exit(-1);
		}
		trace(now, "starting");
		nodeId = node_id;
		nodeObj = mynode;
		this.parameters=parameters;
		this.links=links;
		numInterfaces=nint;
		
		tracingOn = parameters.containsKey("trace");
		return 0;
	}
	
	
	public void forward_packet(int now, Packet p, int iface) {
		if ( iface == LOCAL && links[0].isUp() ) { // locally sent packet
		    // always sends a copy, not the Packet object itself
		    nodeObj.send(p.getCopy(),0);
			trace(now, "forwarded a packet sent by the local interface");
			return;
		} 
		if ( iface == LOCAL && ! links[0].isUp() ) {
			nodeObj.send(p,UNKNOWN);
			trace(now, "network interface is down");
			return;
		}
		// allows the node to count dropped packets
		nodeObj.send(p,UNKNOWN);
	}

}


