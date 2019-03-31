package com.topher.relay.circuit;

import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;

public class Wire implements CircuitElement, Cloneable
{
	
	private String id;
	protected String a;
	protected String b;
	
	@Override
	public void fromXML(Element e)
	{
		id = Network.getName(e.getAttributeValue("id"));
		a = e.getAttributeValue("a");
		b = e.getAttributeValue("b");						
	}	

	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();	
	}
		
	public String toString() {
		return "Wire '"+id+"' connecting '"+a+"' and '"+b+"'";
	}
	
	@Override
	public boolean propagate(NetworkState states)
	{			
		return NetworkState.propagateWire(a, b, states);	
	}

	@Override
	public boolean changeState(NetworkState states) {
		// Fixed state
		return false;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public List<String> getNodeNames() {
		// Wires have no nodes of their own. They just connect nodes.
		return new ArrayList<String>();
	}

	@Override
	public void setIdPrefix(String prefix) {
		id = prefix+id;
	}
	
}
