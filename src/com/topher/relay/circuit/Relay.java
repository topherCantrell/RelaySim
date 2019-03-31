package com.topher.relay.circuit;

import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;

public class Relay implements CircuitElement, Cloneable
{
	
	private String id;
	private int switches;	
	private int state;
	
	@Override
	public void fromXML(Element e)
	{
		id = Network.getName(e.getAttributeValue("id"));
		switches = Integer.parseInt(e.getAttributeValue("switches"));				
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();	
	}
	
	public String toString() {
		return "Relay '"+id+"' with "+switches+" switches (state="+state+")";
	}
	
	@Override
	public boolean changeState(NetworkState states)
	{
		boolean ret = false;
		
		String ca = ""+states.get(id+".ca");
		//String cb = ""+states.get(id+".cb");
		String cb = "GROUND";
		
		int os = state;
		
		if(ca.equals("POWER") && cb.equals("GROUND")) {
			if(state<2) {
				++state;
				ret = true;				
			}
		} else {
			if(state>0) {
				--state;
				ret = true;				
			}
		}
		
		//if(ret) {
		//	System.out.println("Relay '"+getId()+"' state "+os+"->"+state);
		//}
		
		return ret;		
	}
	
	@Override
	public boolean propagate(NetworkState states)
	{		
		// Between states ... nothing to propagate 
		if(state==1) return false;
		
		boolean ret = false;
		
		// Propagate switches of relay (normally-closed or normally-open)
		for(int s=0;s<switches;++s) {
			String s_co = id+".s"+s+"_co";
			String s_o = null;			
			
			// We tested for state==1 above. Must be 0 or 2 here.
			if(state==0) {
				s_o = id+".s"+s+"_nc";
				ret |= NetworkState.propagateWire(s_co, s_o, states);
			} else {
				s_o = id+".s"+s+"_no";
				ret |= NetworkState.propagateWire(s_co, s_o, states);
			}
			//System.out.println("--"+s_co+" "+s_o);
			
		}
		
		return ret;
		
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public List<String> getNodeNames() {
		List<String> ret = new ArrayList<String>();
		ret.add(id+".ca");
		//ret.add(id+".cb");
		for(int s=0;s<switches;++s) {
			ret.add(id+".s"+s+"_co");
			ret.add(id+".s"+s+"_nc");
			ret.add(id+".s"+s+"_no");			
		}
		return ret;
	}

	@Override
	public void setIdPrefix(String prefix) {
		id = prefix+id;
	}

}
