package android.geosvr.dtn.servlib.routing.epidemic;

import android.geosvr.dtn.servlib.routing.epidemic.HelloTLV.HelloFunctionType;

class Hello {
	String eid;
	byte eid_length;
	HelloFunctionType function;
	short length;
	byte timer;

	@Override
	public String toString() {
		return String.format("Function %s\n" + "Length %x\n" + "Timer %d\n"
				+ "Eid_length %d\n" + "EID %s", function.getCaption(), length,
				timer, eid_length, eid);
	}
}
