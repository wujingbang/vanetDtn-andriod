package android.geosvr.dtn.servlib.routing.epidemic;

public class RIBInformationBaseEntry {
	short flags;
	float pValue;
	short stringID;

	@Override
	public String toString() {
		return String.format("StringID %d/PValue %f/Flags %x\n", stringID,
				pValue, flags);
	}
}
