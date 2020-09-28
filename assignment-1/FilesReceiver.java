package assignment1;

import java.util.Arrays;
import cnss.simulator.*;
import cnss.lib.*;

public class FilesReceiver extends AbstractApplicationAlgorithm {
	
	int totReceived = 0;

  public FilesReceiver() {
      super(true, "files-receiver");
  }

  public int initialise(int now, int node_id, Node self, String[] args) {
	  super.initialise(now, node_id, self, args);
	  log(0, "starting");
	  return 0;
	}
  
	public void on_clock_tick(int now) {
		log(now, "clock tick");
	}

	public void on_timeout(int now) {
		log(now, "timeout");
	}

  public void on_receive( int now, DataPacket p ) {
	  totReceived++;
	  log(now, "got: " + p + " n. "+totReceived);
  }
  
	public void showState(int now) {
		System.out.println(name + " received "+totReceived+" packets with blocks");
	}
} 