package com.topher.relay.circuit;
import org.jdom.Element;


public class Use implements Cloneable
{
	
	String id;
	String type;
	
	public void fromXML(Element e)
	{
		id = Network.getName(e.getAttributeValue("id"));
		type = e.getAttributeValue("network");						
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();	
	}
	
	public String toString() {
		return "Use '"+id+"' of type '"+type+"'";
	}

}
