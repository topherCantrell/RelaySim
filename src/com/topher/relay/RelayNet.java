package com.topher.relay;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import com.topher.relay.circuit.Network;

public class RelayNet {
	
	public static void main(String [] args) throws Exception
	{
		
		SAXBuilder parser = new SAXBuilder();
        Document doc = parser.build("c:\\projects\\relay\\Memory.xml");
		//Document doc = parser.build(args[0]);
                
        Network root = new Network();
        root.fromXML(doc.getRootElement());          
        root.prepare();
        
        //root.dumpNet();
        
        /*
        System.out.println("--Init");
        root.setSwitchState("C_in", false);
        root.setSwitchState("C_in", false); 
        root.setSwitchState("OE_in", true);
        root.simulate();               
        root.dumpIO();
        System.out.println();
        */
        
        System.out.println("--Set");
        root.setSwitchState("OE_in",true);
        root.setSwitchState("D_in", true);
        root.simulate();             
        root.setSwitchState("C_in", true);
        root.simulate();    
        root.setSwitchState("C_in", false);
        root.simulate(); 
        root.setSwitchState("D_in", false);
        root.simulate();
        root.dumpIO();
        
        System.out.println("--Enable Test");
        root.setSwitchState("OE_in",false);
        root.simulate();
        root.dumpIO();
        root.setSwitchState("OE_in",true);
        root.simulate();
        root.dumpIO();
        
        root.setSwitchState("OE_in",true);
        System.out.println("--Reset");
        root.setSwitchState("C_in", true);
        root.simulate();    
        root.setSwitchState("C_in", false);
        root.simulate(); 
        root.dumpIO();
        
        
        //root.dumpStates();
        
        /*
        root.setSwitchState("C_in", true);
        root.simulate();
        root.setSwitchState("C_in", false);
        root.simulate();
        root.setSwitchState("D_in", false);
        root.simulate();
        root.dumpIO();        
        System.out.println();
        */
        
        /*
        System.out.println("--Init");
        root.setSwitchState("S_in", false);
        root.setSwitchState("R_in", false);        
        root.simulate();               
        root.dumpIO();
        System.out.println();
        
        System.out.println("--Set");
        root.setSwitchState("S_in", true);               
        root.simulate();    
        root.setSwitchState("S_in", false); 
        root.simulate();
        root.dumpIO();
        System.out.println();
        
        System.out.println("--Reset");
        root.setSwitchState("R_in", true);               
        root.simulate();    
        root.setSwitchState("R_in", false); 
        root.simulate();
        root.dumpIO();
        System.out.println();
        */
        
        
        /*
        System.out.println("-----");
        root.setSwitchState("A_input", false);
        root.setSwitchState("B_input", false);        
        root.simulate();               
        root.dumpIO();
        
        System.out.println("-----");
        root.setSwitchState("A_input", false);
        root.setSwitchState("B_input", true);        
        root.simulate();               
        root.dumpIO();
        
        System.out.println("-----");
        root.setSwitchState("A_input", true);
        root.setSwitchState("B_input", false);        
        root.simulate();               
        root.dumpIO();
        
        System.out.println("-----");
        root.setSwitchState("A_input", true);
        root.setSwitchState("B_input", true);        
        root.simulate();               
        root.dumpIO();
        */
		
	}

}
