package com.topher.relay.circuit;

import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;

public class Probe implements CircuitElement, Cloneable
{
		
	private String id;	
	private boolean lastState;
		
	public boolean isPower()
	{
		return lastState;
	}
	
	@Override
	public void fromXML(Element e)
	{
		id = Network.getName(e.getAttributeValue("id"));		
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();	
	}
	
	public String toString() {
		return "Probe '"+id+"' power="+isPower();
	}

	@Override
	public boolean changeState(NetworkState states) {
		// No state
		return false;
	}

	@Override
	public boolean propagate(NetworkState states) {
		// Single node ... nothing to propagate		
		String s = states.get(id);
		lastState=false;
		if(s!=null && s.equals("POWER")) lastState=true;
		return false;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public List<String> getNodeNames() {
		List<String> ret = new ArrayList<String>();
		ret.add(id);
		return ret;
	}

	@Override
	public void setIdPrefix(String prefix) {
		id = prefix+id;
	}
		
}
