package com.topher.relay.circuit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom.Element;

/**
 * This class manages all the components of a network. A network can be referenced (used)
 * in another network. These "building block networks" have pads that link them to the
 * surrounding network.
 */
public class Network 
{
	
	String id;
		
	List<CircuitElement> elements = new ArrayList<CircuitElement>();	
	List<Wire> wires = new ArrayList<Wire>();	
	
	List<Use> uses = new ArrayList<Use>();	
	Map<String,String> pads = new HashMap<String,String>();
	Map<String,Network> subNets = new HashMap<String,Network>();
	private List<String> names = new ArrayList<String>();	
	
	private static int nextId = 0;
	
	private NetworkState states = new NetworkState();
	
	Network(Map<String,Network> subNets)
	{
		this.subNets = subNets;
	}
	
	public Network()
	{
		this(new HashMap<String,Network>());
	}
	
	public void simulate()
	{
		int pc = 0;
        while(true) {
        	++pc;
        	if(pc==1000) {
        		throw new RuntimeException("Network would not stablize");
        	}
        	propagate();
        	boolean c = changeStates();
        	if(!c) break;
        }
	}
	
	public boolean changeStates()
	{
		boolean ret = false;		
		for(CircuitElement e : elements) {
			ret |= e.changeState(states);
		}		
		return ret;
	}
	
	public void propagate()
	{
		states.clear();		
		int pc = 0;		
		
		while(true) {						
			++pc;				
			boolean changed = false;
			for(CircuitElement e : elements) {
				changed |= e.propagate(states);
			}			
			for(Wire w : wires) {
				changed |= w.propagate(states);
			}				
			if(!changed) break;			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void fromXML(Element e)
	{
		
		id = getName(e.getAttributeValue("id"));
		
		List<Element> children = e.getChildren();
		for(Element child : children) {
			if(child.getName().equals("pad")) {
				String name = getName(child.getAttributeValue("name"));				
				String is = child.getAttributeValue("is");
				pads.put(name, is);
				check(name);
			} else if(child.getName().equals("wire")) {
				Wire o = new Wire();
				o.fromXML(child);
				wires.add(o);
				check(o.getId());
			}  else if(child.getName().equals("network")) {
				Network t = new Network(subNets);
				t.fromXML(child);
				subNets.put(t.id,t);
				check(t.id);
			} else if(child.getName().equals("use")) {
				Use o = new Use();
				o.fromXML(child);
				uses.add(o);
				check(o.id);
			} 			
			
			else {
				try {
					Class cc = getClass().getClassLoader().loadClass("com.topher.relay.circuit."+child.getName());
					CircuitElement ce = (CircuitElement)cc.newInstance();
					ce.fromXML(child);
					elements.add(ce);
					check(ce.getId());
				} catch (Exception er) {
					er.printStackTrace();
				}
			}
		}
		
	}	
	
	public void dumpNet()
	{
		for(CircuitElement o : elements) System.out.println(o);
		for(Wire o : wires) System.out.println(o);		
	}
	
	public void dumpFlow(Map<String,String> propValues)
	{
		for(String s : propValues.keySet()) {
			System.out.println(s+":"+propValues.get(s));
		}
	}
	
	public void dumpIO()
	{
		for(CircuitElement e : elements) {
			if(e instanceof Probe || e instanceof Switch) {
				System.out.println(e);
			}
		}
	}
	
	public void dumpProbes()
	{
		for(CircuitElement e : elements) {
			if(e instanceof Probe) {
				System.out.println(e);
			}
		}		
	}
	
	public void dumpStates()
	{
		states.dumpStates();
		
	}
	
	public void setSwitchState(String id, boolean closed)
	{
		for(CircuitElement e : elements) {
			if(e.getId().equals(id) && e instanceof Switch) {
				Switch s = (Switch)e;
				s.setClosed(closed);
				return;
			}
		}
		throw new RuntimeException("Switch '"+id+"' not found");
	}
	
	public void prepare()
	{
		expandUses(this,"");	
		for(CircuitElement e : elements) {
			List<String> names = e.getNodeNames();
			for(String s : names) {
				//System.out.println(">"+s+"<");
				states.set(s, "");
			}
		}
	}
	
	void expandUses(Network net, String prefix)
	{
		try {
			
			for(Use use : uses) {
				
				// Find the parts in the subnetwork
				Network subNet = subNets.get(use.type);
				
				String predot = prefix+use.id+".";
				
				// If there are wires in the using network that reference pads of the
				// used network then rename those endpoints.
				for(String k : subNet.pads.keySet()) {
					String v = subNet.pads.get(k);
					for(Wire w : net.wires) {
						if(w.a.equals(predot+k)) {
							w.a = predot+v;
						}
						if(w.b.equals(predot+k)) {
							w.b = predot+v;
						}
					}					
				}

				// Populate the net with new elements and wires from the subNetwork template
				for(CircuitElement e : subNet.elements) {
					CircuitElement ne = (CircuitElement)e.clone();
					ne.setIdPrefix(predot);		
					net.elements.add(ne);
				}				
				for(Wire w : subNet.wires) {
					Wire uw = (Wire)w.clone();
					if(!uw.a.equals("POWER") && !uw.a.equals("GROUND")) uw.a = predot+uw.a;
					if(!uw.b.equals("POWER") && !uw.b.equals("GROUND")) uw.b = predot+uw.b;
					uw.setIdPrefix(predot);					
					net.wires.add(uw);
				}
				
				// Recursively expand uses
				if(subNet.uses.size()>0) {
					subNet.expandUses(net,predot);					
				}				
			}			
			
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	private void check(String name)
	{
		if(names.contains(name)) {
			throw new RuntimeException("Duplicate name '"+name+"'");
		}
		names.add(name);				
	}
	
	public static String getName(String e) 
	{
		if(e==null) {
			e = "_"+nextId;
			++nextId;
		}
		return e;
	}
	
	public String toString() {
		return "Network '"+id+"'";
	}
	
}
