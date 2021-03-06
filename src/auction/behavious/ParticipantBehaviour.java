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
		
		System.out.println(pa.getLocalName()+": [Participant] The money I have: "+pa.getMoney());
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
			System.out.println(pa.getLocalName()+": Working...");
		}
		
	}
	
	public void isInterested() {
		ACLMessage msg = pa.blockingReceive();
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

		ACLMessage msg = pa.blockingReceive();

		if(msg.getPerformative() == ACLMessage.CFP)
		{
			System.out.println("[Product: "+prodName+"]\n"+pa.getLocalName()+": [Participant] Geting Call For Proposal...");
			String message = msg.getContent();
						
			int index = message.indexOf("Winner:"); 
			if(index > 0)
			{
				basePrice = Double.parseDouble(message.substring(12,message.indexOf(',')));
				//System.out.println(pa.getLocalName()+": Base Price:"+basePrice);
				
				String lastWinnerName = message.substring(index+8, message.length()-1);
				//System.out.println(pa.getLocalName()+": Last Winner Name:"+lastWinnerName);
				
				if(pa.getLocalName() != lastWinnerName)
				{			
					if(basePrice >= maxPrice || basePrice >= pa.getMoney())
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
			else {
				basePrice = Double.parseDouble(message.substring(12,message.length()));
				//System.out.println(pa.getLocalName()+": Base Price:"+basePrice);
				state = PROPOSE;
			}
		}
		else if(msg.getPerformative() == ACLMessage.INFORM)
			System.out.println(pa.getLocalName()+": I'M THE WINNER, YES!");
		else if(msg.getPerformative() == ACLMessage.REQUEST)
		{
			pa.setMoney(pa.getMoney() - basePrice);
			System.out.println("[Product: "+prodName+"]\n"+pa.getLocalName()+" [WINNER]: Product purchased successfully...  Money left: "+pa.getMoney());
		}
		else
			System.out.println("[Product: "+prodName+"]\n"+pa.getLocalName()+" [WINNER]: Participant Behaviour->getCFP: Not INFORM message!");
	}
	
	public void propose() {
		try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		System.out.println(pa.getLocalName()+": Proposing...");
		ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
		msg.addReceiver(initiator);
		if(isLastWinner)
		{
			msg.setContent(String.valueOf(basePrice));
			System.out.println("[Product: "+prodName+"]\n"+pa.getLocalName()+": [Participant],  Price: "+basePrice);
		}
		else
		{
			basePrice += Utils.getRandomNumber(1, 11);
			msg.setContent(String.valueOf(basePrice));
			System.out.println("[Product: "+prodName+"]\n"+pa.getLocalName()+": [Participant],  Price: "+basePrice);
		}
		
		pa.send(msg);
		
		state = GETCFP;
	}
	
	public void refuse() {
		ACLMessage msg = new ACLMessage(ACLMessage.REFUSE);
		msg.addReceiver(initiator);
		System.out.println("[Product: "+prodName+"]\n"+pa.getLocalName()+": Not interested or don't have enough money or don't want to pay that much!\n");
		pa.send(msg);
		
		state = END;
	}

	@Override
	public boolean done() {
		
		return (state==END);
	}

}
