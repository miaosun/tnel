package auction.agents;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.util.Vector;


public class InitiatorAgent extends BaseAgent{



	protected Vector<ACLMessage> prepareRequests(ACLMessage msg) {
		Vector<ACLMessage> v = new Vector<ACLMessage>();
		// ...
		return v;
	}

	protected void handleAgree(ACLMessage agree) {
		// ...
	}

	protected void handleRefuse(ACLMessage refuse) {
		// ...
	}

	protected void handleInform(ACLMessage inform) {
		// ...
	}

	protected void handleFailure(ACLMessage failure) {
		// ...
	}

}
