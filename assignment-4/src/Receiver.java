
import cnss.simulator.*;

import java.nio.charset.StandardCharsets;

import cnss.lib.*;

public class Receiver extends AbstractApplicationAlgorithm {
	
	static final String NAME = "receiver";
	
	private	int countSent;
	private	int countReceived;
	private	int node_id;
	
	public Receiver() {
		super(false, NAME);
	}

	public int initialise(int now, int node_id, Node self, String[] args) {
		super.initialise(now, node_id, self, args);
		this.node_id = node_id;
		log(now, "starting");
		countReceived = 0;
		countSent = 0;
		return 0;
	}

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
} 