package auction.behavious;

import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
abstract class BaseBehaviour extends SimpleBehaviour{

	
	
	protected void send(AID receiverAgent, String content, int type)
	{
		ACLMessage msg = new ACLMessage(type);
		msg.setContent(content);
		msg.addReceiver(receiverAgent);
		
		this.myAgent.send(msg);
	}
	
	
	protected void replay(AID receiverAgent, String content, int type, String replayTo)
	{
		ACLMessage replay = new ACLMessage(type);
		replay.setContent(content);
		replay.addReceiver(receiverAgent);
		replay.setInReplyTo(replayTo);
		
		this.myAgent.send(replay);
	}
	
	
	
	
	
	
	
}
