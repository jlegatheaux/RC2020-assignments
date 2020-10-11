package ft20;

import static ft20.FT20Packet.ACK;
import static ft20.FT20Packet.DATA;
import static ft20.FT20Packet.ERROR;
import static ft20.FT20Packet.FIN;
import static ft20.FT20Packet.UPLOAD;

import cnss.lib.AbstractApplicationAlgorithm;
import cnss.simulator.DataPacket;
import cnss.simulator.Node;

abstract public class FT20AbstractApplication extends AbstractApplicationAlgorithm {

	private static final String TRAFFIC = "bytes";
	private static final String TIMEOUTS = "timeouts";

	private FT20_PacketHandler pktHandler;

	FT20Stats stats;

	protected FT20AbstractApplication(boolean logOn, String name) {
		super(logOn, name);
	}

	public int initialise(int now, int node_id, Node self, String[] args, FT20_PacketHandler pktHandler) {
		super.initialise(now, node_id, self, args);
		this.pktHandler = pktHandler;
		this.stats = new FT20Stats(now);
		return 0;
	}

	protected void sendPacket(int now, int dest, FT20Packet pkt) {
		super.log(now, "SENDING: " + pkt);

		DataPacket cnssPkt = self.createDataPacket(dest, pkt.encodeToBytes());
		self.send(cnssPkt);

		stats.out.increment(pkt.getClass().getSimpleName(), 1);
		stats.out.increment(TRAFFIC, cnssPkt.getSize());
	}

	public void on_timeout(int now) {
		stats.timeoutEvents++;
		
		super.log(now, "TIMEOUT...");
	}
	
	public void on_receive(int now, DataPacket cnssPkt) {
		int src = cnssPkt.getSource();

		FT20Packet pkt = FT20Packet.decodeFromBytes(cnssPkt.getPayload());

		stats.in.increment(pkt.getClass().getSimpleName(), 1);
		stats.in.increment(TRAFFIC, cnssPkt.getSize());

		super.log(now, "GOT: " + pkt);
		switch (pkt.opcode) {
		case UPLOAD:
			pktHandler.on_receive_upload(now, src, (FT20_UploadPacket) pkt);
			break;
		case DATA:
			pktHandler.on_receive_data(now, src, (FT20_DataPacket) pkt);
			break;
		case ACK:
			pktHandler.on_receive_ack(now, src, (FT20_AckPacket) pkt);
			break;
		case FIN:
			pktHandler.on_receive_fin(now, src, (FT20_FinPacket) pkt);
			break;
		case ERROR:
		default:
			System.out.println("FATAL ERROR...");
			System.exit(-1);
		}
	}

	public void printReport(int now) {
		System.out.println("\n+++++++++++++++++++++++++++++++++++++++++");
		System.out.println(super.name + " STATS\n");
		stats.printReport(now);
		System.out.println("+++++++++++++++++++++++++++++++++++++++++\n");
	}

	public void tallyRTT(int rttSample) {
		stats.rtt.tally(rttSample);
	}

	public void tallyTimeout(int timeoutSample) {
		stats.timeout.tally(timeoutSample);
	}

}
