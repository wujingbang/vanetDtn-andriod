package android.geosvr.dtn.servlib.routing.epidemic;

public class BundleOfferEntry {

	public short id;
	public byte flags;
	public byte reserve;
	public int creationTime;
	public int seqNo;

	public String toString() {
		return String.format("ID %d/" + "Flags %d/" + "Reserve %d/"
				+ "Creationtime %d/" + "Seqno %d\n", id, flags, reserve,
				creationTime, seqNo);
	}
}
