package auction.agents;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import utils.Product;
import auction.behavious.InitiatorBehaviour;


@SuppressWarnings("serial")
public class InitiatorAgent extends BaseAgent{

	public final String type = "Iniciator";
	
	public static final int	AUCTION_TIMEOUT		= 500;

	//Product to sell with initial price
	//private static Map<String, Integer> productToSell = new HashMap<String, Integer>();
	
	private ArrayList<Product> productToSell = new ArrayList<>();

	public InitiatorAgent()
	{
		productToSell.add(new Product("Smartphone", 300));
		productToSell.add(new Product("Tablet", 400));
		productToSell.add(new Product("Laptop", 800));

		System.out.println("Iniciator constructor!");
	}
	
	@Override
	protected void setup()
	{
		super.setup();
		for(Product prod : productToSell)
		{
			addBehaviour(new InitiatorBehaviour(prod));
		}
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
