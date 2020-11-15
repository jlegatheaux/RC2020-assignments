
import cnss.simulator.*;

import java.nio.charset.StandardCharsets;

import cnss.lib.*;

public class Sender extends AbstractApplicationAlgorithm {

	static final String NAME = "sender";
	
	private	int countSent;
	private	int countReceived;
	private	int node_id;
	private	int dest = 0;  // packet destination

	public Sender() {
		super(false, NAME);
	}

	public int initialise(int now, int node_id, Node self, String[] args) {
		super.initialise(now, node_id, self, args);
		this.node_id = node_id;
		log(now, "starting");
		countReceived = 0;
		countSent = 0;
		if ( args.length != 1 ) {
			System.err.println("sender: missing destination \n\n");
			System.exit(-1);
		}
		dest = Integer.parseInt(args[0]);
		return 10000;
	}

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
} 