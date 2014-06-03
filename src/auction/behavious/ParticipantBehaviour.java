package auction.behavious;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Map;

import utils.Utils;
import auction.agents.ParticipantAgent;

@SuppressWarnings("serial")
public class ParticipantBehaviour extends BaseBehaviour{

	public final int GETINFORM  = 1;
	public final int GETCFP  	= 2;
	public final int PROPOSE	= 3;
	public final int REFUSE  	= 4;
	public final int END 		= 5;
	
	ParticipantAgent pa;
	String prodName;
	double maxPrice;
	boolean isLastWinner = false;
	double basePrice;
	
	AID initiator;
	
	private Map<String, Integer> interestedProduct = new HashMap<String, Integer>();
	
	public ParticipantBehaviour(ParticipantAgent pa)
	{
		this.pa = pa;
		state = GETINFORM;
		interestedProduct = pa.getInterestedProduct();
	}
	
	
	@Override
	public void action() {
		
		switch(state) {
		case GETINFORM:
			isInterested();
			break;
		case GETCFP:
			getCFP();
			break;
		case PROPOSE:
			propose();
			break;
		case REFUSE:
			refuse();
			break;
		default:
			System.out.println("Working...");
		}
		
	}
	
	public void isInterested() {
		ACLMessage msg = this.myAgent.blockingReceive();
		if(msg.getPerformative() == ACLMessage.INFORM)
		{
			initiator = msg.getSender();
			String message = msg.getContent();
			prodName = message.substring(9,message.length()-2);
		}
		
		if(interestedProduct.get(prodName) == null)
			state = REFUSE;
		else
		{
			maxPrice = interestedProduct.get(prodName);	
			state = GETCFP;
		}
	}
	
	public void getCFP() {
		System.out.println("Get CFP!");
		ACLMessage msg = this.myAgent.blockingReceive();
		if(msg.getPerformative() == ACLMessage.CFP)
		{
			String message = msg.getContent();
			int index = message.indexOf("Winner:"); 
			
			if(index > 0)
			{
				String lastWinnerName = message.substring(index+8, message.length()-1);
				System.out.println("Last Winner Name:"+lastWinnerName);
				System.out.println("Base Price:"+message.substring(11,message.indexOf('€')));
				basePrice = Double.parseDouble(message.substring(11,message.indexOf('€')));
				
				if(pa.getName() != lastWinnerName)
				{			
					if(basePrice >= maxPrice)
						state = REFUSE;
					else
					{
						
						state = PROPOSE;
					}
				}
				else {
					isLastWinner = true;
					state = PROPOSE;
				}
			}	
		}
	}
	
	public void propose() {
		ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
		msg.addReceiver(initiator);
		if(isLastWinner)
			msg.setContent(String.valueOf(basePrice));
		else
			msg.setContent(String.valueOf(basePrice+Utils.getRandomNumber(1, 11)));
		
		this.myAgent.send(msg);
		
		state = GETCFP;
	}
	
	public void refuse() {
		ACLMessage msg = new ACLMessage(ACLMessage.REFUSE);
		msg.addReceiver(initiator);
		this.myAgent.send(msg);
		
		state = END;
	}

	@Override
	public boolean done() {
		
		return (state==END);
	}

}
