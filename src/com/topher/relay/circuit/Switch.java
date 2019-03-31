package com.topher.relay.circuit;

import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;

public class Switch implements CircuitElement, Cloneable
{
	
	private boolean closed;
	private String id;
	
	public void setClosed(boolean closed) {this.closed=closed;}	
	public boolean isClosed() {return this.closed;}
		
	@Override
	public void fromXML(Element e)
	{
		id = Network.getName(e.getAttributeValue("id"));		
		String s = e.getAttributeValue("closed");
		if(s!=null && s.toUpperCase().equals("TRUE")) {
			closed = true;
		}
	}
			
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();	
	}
	
	public String toString() {
		return "Switch '"+id+"' closed="+closed;
	}
	
	@Override
	public boolean propagate(NetworkState states)
	{		
		String s = states.get(id);
		//System.out.println(">>"+id+":"+s);
		if(closed) {
			states.set(id,"POWER");
			if(s.equals("")) return true;
		} 		
		return false;
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
		List<String> ret = new ArrayList<String>();
		ret.add(id);
		return ret;
	}

	@Override
	public void setIdPrefix(String prefix) {
		id = prefix+id;		
	}

}
