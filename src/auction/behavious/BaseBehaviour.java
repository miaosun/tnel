package auction.behavious;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
abstract class BaseBehaviour extends SimpleBehaviour{
	
	protected int state;
	
	public BaseBehaviour() {
		
	}
	
	public abstract void action();
	
	protected void send(ArrayList<AID> receiverAgent, String content, int type)
	{			
		ACLMessage msg = new ACLMessage(type);
		msg.setContent(content);
		for(AID rcv : receiverAgent)
		{
			msg.addReceiver(rcv);
		}
		this.myAgent.send(msg);
	}

}
