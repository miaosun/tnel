package auction.agents;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Map;

import auction.behavious.ParticipantBehaviour;
import utils.Utils;

@SuppressWarnings("serial")
public class ParticipantAgent extends BaseAgent{

	public final String type = "Participant";

	//Money the participant has
	private static int money;

	//Interested product with the max price willing to pay
	private static Map<String, Integer> interestedProduct = new HashMap<String, Integer>();

	public ParticipantAgent()
	{
		interestedProduct.put("SmartPhone", Utils.getRandomNumber(300,500));
		interestedProduct.put("Tablet", Utils.getRandomNumber(400,600));
		interestedProduct.put("Laptop", Utils.getRandomNumber(700,900));

		money = Utils.getRandomNumber(800, 2000);

		System.out.println("The money I have: " + money);

	}

	@Override
	protected void setup()
	{
		super.setup();

		addBehaviour(new ParticipantBehaviour());
	}


	@Override
	public String getType() {
		
		return this.type;
	}

}
