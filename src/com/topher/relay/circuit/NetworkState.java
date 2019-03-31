package com.topher.relay.circuit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetworkState 
{
	
	private Map<String,String> states = new HashMap<String,String>();
	
	public NetworkState()
	{
		clear();
	}
	
	public void set(String key, String value)
	{
		//System.out.println("::"+key+":"+value);
		states.put(key, value);
	}
	
	public Set<String> getKeys()
	{
		return states.keySet();
	}
	
	public String get(String key)
	{
		return states.get(key);
	}
	
	public void open(String key)
	{
		states.put(key,"");
	}
	
	public void clear() {
		//System.out.println("----CLEAR----");
		for(String s : states.keySet()) {
			states.put(s, "");
		}	
		states.put("POWER", "POWER");
		states.put("GROUND", "GROUND");
	}
	
	public static boolean propagateWire(String a, String b, NetworkState states)
	{
		
		// Only values are POWER and GROUND. If both are not-null they have to be the same or there
		// is a short. If both are null, nothing. If one is null, propagate the non-null value.
		
		String pa = states.get(a);
		String pb = states.get(b);	
		
		//System.out.println("::"+a+":"+pa);
		//System.out.println("::"+b+":"+pb);
		
		// Nothing to propagate.
		if(pa.equals("") && pb.equals("")) return false;

		// If one is null, propagate one to the other.
		if(pa.equals("") || pb.equals("")) {
			if(pa.equals("")) {
				//System.out.println("::w "+b+"->"+a+" ("+pb+")");
				states.set(a, pb);
			} else {
				//System.out.println("::w "+a+"->"+b+" ("+pa+")");
				states.set(b, pa);
			}
			return true;
		}
		
		// Both have a value. Make sure they are the same.
		if(!pa.equals(pb)) {
			throw new RuntimeException("SHORT: "+a+"("+pa+") to "+b+" ("+pb+")");
		}
		return false;
						
	}
	
	public void dumpStates()
	{
		List<String> ret = new ArrayList<String>();
		for(String s : states.keySet()) {
			ret.add(s+"::"+states.get(s));
		}
		
		Collections.sort(ret);
		for(String s : ret) {
			System.out.println(s);
		}
		
	}

}
