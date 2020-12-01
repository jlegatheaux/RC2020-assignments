// the skeleton control algorithm (routing) of a node that will
// use the distance-vector (DV) routing algorithm

import cnss.simulator.*;
import cnss.lib.*;

public class DVControl extends AbstractControlAlgorithm {

	// Do not modify
	static int INFINITY = 1000000; 
	static int REFERENCE_BAND = 100000000; // reference bandwidth = 100 Mbps
	static int UPDATE_INTERVAL = 1000;

	// variables that will contain the parameters transmitted by the CNSS node code and
	// assigned by the initialise upcall()
	private Node nodeObj;					// the CNSS node code that supports this node algorithm 
	private int nodeId;						// my Id or address
	private GlobalParameters parameters;	// configuration file parameters
	private Link[] links;					// this node links, numbered from 0 .. nInterfaces - 1
	private int nInterfaces;				// this node number of interfaces, each with a link 
	 										// link attached

	// variables containing the flags that drive traces - calls to the trace() method are
	// shown or not and stages executed by the algorithm besides the base line solution
	// these flags are set by the initialise() upcall and their values are driven by the
	// configuration file content
	private boolean tracingOn;
	private boolean triggered; // if true we should use triggered updates
	private boolean preverse; // if true split horizon with poison reverse should be implemented
	private boolean expire;  // if true it should expire useless routing table entries
	// that is, those with metric or cost infinity for more 3 times the update interval

	// YOUR VARIABLES HERE, NAMELY THE ROUTING TABLE



	public DVControl() {
		super("dv control");
	}



	/**
	 * Initializes the control algorithm and returns the required
	 * control_clock_tick_period. If control_clock_tick_period == 0, no clock_ticks
	 * will be submitted; Interfaces are numbered 0 to nint-1. Each has a link
	 * attached: links[i] Interface; -1 is virtual and denotes, when needed, the
	 * local loop interface.
	 * 
	 * DV code: initialises the routing table 
	 * DV Code: if ( triggered ) sends first announcements
	 * 
	 * @param now        the current virtual clock value
	 * @param id         this node id
	 * @param nodeObj    a reference to the node object executing this algorithm
	 * @param parameters the collection of global parameters
	 * @param links      the nodes links array
	 * @param nint       the number of interfaces (or links) of this node, not including the LOCAL one
	 * @return the requested clock_tick_period
	 */
	public int initialise(int now, int node_id, Node mynode, GlobalParameters parameters, Link[] links, int nint) {
		super.initialise(now, node_id, mynode, parameters, links, nint);
		this.parameters=parameters;
		nodeId = node_id;
		nodeObj = mynode;
		this.links=links;
		nInterfaces=nint;
		
		tracingOn = parameters.containsKey("trace");
		preverse = parameters.containsKey("preverse");
		expire = parameters.containsKey("expire");
		triggered = parameters.containsKey("triggered");

		super.set_trace(tracingOn);
		trace(now, "starting w/ period / trig. upds / shpr / expire: "+UPDATE_INTERVAL+","+triggered+","+preverse+","+expire);
		
		// DV CODE HERE:
		
		return UPDATE_INTERVAL; // do not touch this line
	}



	/**
	 * A periodic clock interrupt. 
	 * 
	 * DV code: sends announcements to neighbors
	 * DV code: if ( expire ) expires staled entries in the routing table
	 * 
	 * @param now the current virtual clock value
	 */
	public void on_clock_tick(int now) { 
		// trace(now, "on clock tick ");
		
		// DV CODE HERE:
		
	}


	/**
	 * Given a packet from another node, forward it to the appropriate interfaces
	 * using nodeObj.control_send(Packet p, int iface); Packet ttl has already been
	 * decreased and controlled. If the algorithm has no solution to route this packet,
	 * send it to the UNKNOWN interface using nodeObj.send(p,UNKNOWN)
	 * 
	 * DV code: use the routing table contents to forward packets
	 * 
	 * @param now   the current virtual clock value
	 * @param p     the packet to process
	 * @param iface the interface it was received by the node
	 */
	public void forward_packet(int now, Packet p, int iface) {
		
		// DV CODE HERE:
	}
	

	/**
	 * Signals a link up event; links[iface].isUp() becomes true
	 * 
	 * DV code: if ( triggered ) sends announcements to neighbors
	 * 
	 * @param now   the current virtual clock value
	 * @param iface interface id where this link is connected
	 */
	public void on_link_up(int now, int iface) {
		trace(now, "interface "+iface + " link up");
		
		// DV CODE HERE:
	}

	/**
	 * Signals a link down event; links[iface].isUp() becomes false
	 * 
	 * DV code: update routing table entries
	 * DV code: if ( triggered ) sends announcements to neighbors
	 * 
	 * @param now   the current virtual clock value
	 * @param iface interface id where this link is connected
	 */
	public void on_link_down(int now, int iface) {
		trace(now, "interface "+iface + " link down");

		// DV CODE HERE:
	}

	
	/**
	 * Given a control packet from another node, process it
	 * 
	 * DV code: process the DV announcement contained in packet p
	 * DV code: if ( triggered && routing table updated ) sends announcements to neighbors
	 * 
	 * @param now   the current virtual clock value
	 * @param p     the packet to process
	 * @param iface the interface it came in from
	 */
	public void on_receive(int now, Packet p, int iface) {
		
		// DV CODE HERE:
		
	}


	public void showControlState(int now) {
		trace(now, "has no extra state to show");
	}


	/**
	 * Prints the routing table to the screen.
	 * The format is :
	 * <blank line>
	 * Node <id> routing table at time stamp
	 * d <destination> i <interface> m <metric> lu <last_update>
	 * d <destination> i <interface> m <metric> lu <last_update>
	 * d <destination> i <interface> m <metric> lu <last_update>
	 */
	public void showRoutingTable(int now) {
		System.out.println("\nRouter "+nodeId+" time "+now);
		
		// DV CODE HERE:

	}
	
	
	/**
	 * Auxiliary method
	 */
	
	
	/**
	 * Given the link connected to an interface, returns its weight or cost
	 * When the protocol metric is the cost all links is equal to 1 this method is useless
	 * 
	 * @param iface
	 * @return metric
	 */
	private int getInterfaceMetric(int iface) {
		// loopback interfaces are virtual with cost = 0
		// the array of interfaces has no entry for the loopback interface
		if (iface == LOCAL) return 0;
		return (int) ( REFERENCE_BAND / links[iface].getBandWidth()); 
	}
	

}


