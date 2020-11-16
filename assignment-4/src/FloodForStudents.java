// Packet forwarding using a simple and dump flooding algorithm
// To be enhanced by students

import java.util.*;

import cnss.lib.AbstractControlAlgorithm;
import cnss.simulator.GlobalParameters;
import cnss.simulator.Link;
import cnss.simulator.Node;
import cnss.simulator.Packet;

public class FloodForStudents extends AbstractControlAlgorithm {

	private Node nodeObj;
	private Link[] links;
	private boolean filter;
	private boolean drop_duplicates;

	public FloodForStudents() {
		super("flood students version");
	}

	public int initialise(int now, int node_id, Node mynode, GlobalParameters parameters, Link[] links, int nint) {
		super.initialise(now, node_id, mynode, parameters, links, nint);
		super.set_trace(parameters.containsKey("trace"));
		filter = parameters.containsKey("filter");
		drop_duplicates = parameters.containsKey("drop_duplicates");
		trace(now, "starting - drop dup: " + drop_duplicates+ " filter: "+filter);
		nodeObj = mynode;
		this.links = links;
		return 0;
	}
	

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


	public void forward_packet(int now, Packet p, int iface) {
		
		if ( drop_duplicates ) {
			// your code here
		} 
		
		if ( p.getDestination() == nodeObj.getId()) {
			Packet localPacket = p.getCopy();
			nodeObj.send(localPacket, LOCAL);
			trace(now, "forwarded a packet locally sent to this node");
			return; // all done
		}

		if ( p.getDestination() == Packet.BROADCAST ) {
			Packet localPacket = p.getCopy();
			localPacket.setDestination(nodeObj.getId());
			nodeObj.send(localPacket, LOCAL);
			trace(now, "forwarded a copy of a broadcasted packet to this node");
		}
		
		if ( filter ) {
			// your code here
		}
		
		flood_packet (now, p, iface);
	}

}

