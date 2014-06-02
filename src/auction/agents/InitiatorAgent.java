package auction.agents;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import auction.behavious.InitiatorBehaviour;


@SuppressWarnings("serial")
public class InitiatorAgent extends BaseAgent{

	public final String type = "Iniciator";
	
	public static final int	AUCTION_TIMEOUT		= 500;

	//Product to sell with initial price
	private static Map<String, Integer> productToSell = new HashMap<String, Integer>();
	
	public InitiatorAgent()
	{
		productToSell.put("SmartPhone", 300);
		productToSell.put("Tablet", 400);
		productToSell.put("Laptop", 600);

		System.out.println("Iniciator constructor!");
	}
	
	@Override
	protected void setup()
	{
		super.setup();
		addBehaviour(new InitiatorBehaviour());
	}
	
	
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

	@Override
	public String getType() {

		return this.type;
	}

}
