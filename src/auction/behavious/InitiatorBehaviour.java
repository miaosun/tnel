package auction.behavious;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

import auction.agents.InitiatorAgent;
import utils.Product;

@SuppressWarnings("serial")
public class InitiatorBehaviour extends BaseBehaviour{

	public final int INFORM 		= 1;
	public final int CFP			= 2;
	public final int GET_PROPOSE	= 3;
	public final int REQUEST		= 4;
	public final int END			= 5;

	//private final int TIMEOUT	= 60;

	private ArrayList<AID>	participants = new ArrayList<>();
	private double priceIteration = 0;
	private int round = 0;
	InitiatorAgent ia;
	
	private AID winner = null;
	Product prod = null;

	//private int lastCFPInSeconds;

	public InitiatorBehaviour(InitiatorAgent ia, Product prod)
	{
		this.ia = ia;
		this.state = INFORM;
		this.prod = prod;
		this.priceIteration = prod.getCommonPrice() * 0.2;
	}


	@Override
	public void action() {

		switch(state)
		{
		case INFORM:
			informAuction();
			break;
		case CFP:
			callForProposal();
			break;
		case GET_PROPOSE:
			getPropose();
			break;
		case REQUEST:
			requestPayment();
			break;
		default:
			System.out.println("   Ini: Working...");
		}
	}

	public void informAuction() {

		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd1 = new ServiceDescription();
		sd1.setType("Participant");
		template.addServices(sd1);

		try {
			DFAgentDescription[] result = DFService.search(ia, template);

			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			for(int i=0; i<result.length; ++i)
			{
				AID receiver = result[i].getName();   
				System.out.println("   Ini: inform Auction receiver name: "+receiver.getLocalName());
				msg.addReceiver(receiver);
				//Iniciar a lista de participantes
				participants.add(receiver);
			}
			msg.setContent("Product: " + prod.getProductName() + " !");
			System.out.println("   Ini: \n\t\tProduct: " + prod.getProductName() + " !");
			ia.send(msg);

		} catch(FIPAException e) { e.printStackTrace(); }
		
		System.out.println("\n   Ini: End of Inform Auction!\n");
		state = CFP;
	}

	public void callForProposal() {
		System.out.println("\n   Ini: Inicial of Call for proposal!\n");
		
		round++;
		System.out.println("\n   Ini: ROUND "+ round + ": [Call for proposal]\n");
		if(round == 1)
		{
			send(participants, "Base Price: "+priceIteration, ACLMessage.CFP);
			System.out.println("   Ini: Base Price: "+priceIteration+"\n");
		}
		else
		{
			send(participants, "Base Price: "+priceIteration+ ", Winner: " + winner.getLocalName() + "!", ACLMessage.CFP);
			System.out.println("   Ini: Base Price: "+priceIteration+ ", Winner: " + winner.getLocalName() + "!\n");
		}
		

		//Calendar calendar = Calendar.getInstance();
		//lastCFPInSeconds = calendar.get(Calendar.SECOND);
		System.out.println("   Ini: End of Call for proposal!\n");
		state = GET_PROPOSE;
	}

	public void getPropose() {
		System.out.println("   Ini: Inicial of getPropose!\n");
		//se passar timeout, state = END;
		//se nao, defina o preco base da iteracao como o valor vencidor da iteracao anterior, e state = CFP
		/*
		boolean b = true;
		for(int i=0; i<participants.size(); i++)
		{
			ACLMessage msg = ia.blockingReceive();
			if(b)
			{
				if(Calendar.getInstance().get(Calendar.SECOND) - lastCFPInSeconds > TIMEOUT)
				{
					state = END;
					break;
				}
				b = false;
			}
		 */		

		for(int i=0; i<participants.size(); i++)
		{
			ACLMessage msg = ia.blockingReceive();
			if(msg.getPerformative() == ACLMessage.REFUSE)
			{
				participants.remove(msg.getSender());
			}

			else if(msg.getPerformative() == ACLMessage.PROPOSE) {

				double bidPrice = Double.parseDouble(msg.getContent());
				System.out.println("   Ini: " + ia.getLocalName() + ": received propose from " + msg.getSender().getLocalName() +" with price "+ bidPrice+"\n");

				if(bidPrice > priceIteration)
				{
					priceIteration = bidPrice;
					winner = msg.getSender();
				}
			}
		}
		if(participants.size() == 0)
		{
			state = REQUEST;
		}
		else if(participants.size() == 1)
		{
			winner = participants.get(0);
			state = REQUEST;
		}
		else
			state = CFP;
	}
	

	private void requestPayment() {
		send(participants, "Auction for product " + prod.getProductName() + " ended, the winner is "+ winner.getLocalName() + " with price " + priceIteration, ACLMessage.INFORM);
		System.out.println("\n   Ini: Auction for product " + prod.getProductName() + " ended, the winner is "+ winner.getLocalName() + " with price " + priceIteration+"\n");
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);

        msg.addReceiver(winner);
        msg.setContent("pong");
        ia.send(msg);
        
        System.out.println("\n   Ini: The Winner is " + winner.getLocalName()+"\n");
        state = END;
	}



	@Override
	public boolean done() {
		return(state == END);
	}

}
