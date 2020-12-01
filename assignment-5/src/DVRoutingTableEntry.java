
public class DVRoutingTableEntry {
		private int dest;
		private int iface;
		private int metric;
		private int last_update;

		public DVRoutingTableEntry (int d, int i, int m, int t)
		{
			dest = d;
			iface = i;
			metric = m;
			last_update = t;
		}
		
		public int getDestination() { return dest; } 
		public void setDestination(int d) {dest = d;}
		public int getInterface() { return iface; }
		public void setInterface(int i) {iface = i;}
		public int getMetric() { return metric;}
		public void setMetric(int m) {metric = m;} 
		public int getTime() {return last_update;}
		public void setTime(int t) {last_update = t;}

		public String toString() 
		{
			String msg;
			if ( metric == DVControl.INFINITY ) msg = "INFINITY"; else msg =""+metric;
			return "d "+dest+" i "+iface+" m "+msg+" lu "+last_update;
		}
}