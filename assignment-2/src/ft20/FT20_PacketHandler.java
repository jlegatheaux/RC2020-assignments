package ft20;

public interface FT20_PacketHandler {
	default public void on_receive_upload(int now, int src, FT20_UploadPacket uploadPkt) {
	}

	default public void on_receive_data(int now, int src, FT20_DataPacket dataPkt) {
	}

	default public void on_receive_ack(int now, int src, FT20_AckPacket ackPkt) {
	}

	default public void on_receive_fin(int now, int src, FT20_FinPacket finPkt) {
	}
};