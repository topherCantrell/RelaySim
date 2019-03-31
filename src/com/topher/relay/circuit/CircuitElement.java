package com.topher.relay.circuit;

import java.util.List;
import org.jdom.Element;

/**
 * This interface describes the methods expected of a circuit element like
 * Relays and Switches and Probes.
 * 
 */
public interface CircuitElement 
{
	
	/**
	 * This method returns the id of the element.
	 * @return the id of the element
	 */
	public String getId();
	
	/**
	 * This method extracts element specific configuration from the XML.
	 * @param e the XML element
	 */
	public void fromXML(Element e);
		
	/**
	 * This method adds a prefix to the circuit elements id.
	 * @param prefix the prefix
	 */
	public void setIdPrefix(String prefix);
	
	/**
	 * This method returns the list of full names of all the nodes in the element. Call
	 * this AFTER the setIdPrefix.
	 * @return the list of nodes
	 */
	public List<String> getNodeNames();
	
	/**
	 * This method propagates values between the circuit element's nodes.
	 * @param states the states of the network nodes
	 * @return true if one or more nodes changed values
	 */
	public boolean propagate(NetworkState states);
	
	/**
	 * This method changes internal circuit element states after the propagation has
	 * settled down.
	 * @param states the states of the network nodes
	 * @return true if the internal state of the element changed
	 */
	public boolean changeState(NetworkState states);

	public Object clone() throws CloneNotSupportedException;

}
