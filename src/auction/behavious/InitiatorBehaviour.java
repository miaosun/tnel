package auction.behavious;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

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
	
	private AID winner = null;
	Product prod = null;

	//private int lastCFPInSeconds;

	public InitiatorBehaviour(Product prod)
	{
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
			System.out.println("Working...");
		}
	}

	public void informAuction() {

		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd1 = new ServiceDescription();
		sd1.setType("Participant");
		template.addServices(sd1);

		try {
			DFAgentDescription[] result = DFService.search(this.myAgent, template);

			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			for(int i=0; i<result.length; ++i)
			{
				AID receiver = result[i].getName();   
				msg.addReceiver(receiver);
				//Iniciar a lista de participantes
				participants.add(receiver);
			}
			msg.setContent("Product: " + prod.getProductName() + " !");
			System.out.println("Product: " + prod.getProductName() + " !");
			this.myAgent.send(msg);

		} catch(FIPAException e) { e.printStackTrace(); }
		System.out.println("End of Inform Auction!");
		state = CFP;
	}

	public void callForProposal() {
		System.out.println("Inicial of Call for proposal!");
		round++;
		System.out.println("ROUND "+ round + ": Call for proposal");
		if(round == 1)
			send(participants, "Base Price: "+priceIteration, ACLMessage.CFP);
		else
			send(participants, "Base Price: "+priceIteration+ ", Winner: " + winner.getName() + "!", ACLMessage.CFP);

		//Calendar calendar = Calendar.getInstance();
		//lastCFPInSeconds = calendar.get(Calendar.SECOND);
		state = GET_PROPOSE;
	}

	public void getPropose() {

		//se passar timeout, state = END;
		//se nao, defina o preco base da iteracao como o valor vencidor da iteracao anterior, e state = CFP
		/*
		boolean b = true;
		for(int i=0; i<participants.size(); i++)
		{
			ACLMessage msg = this.myAgent.blockingReceive();
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
			ACLMessage msg = this.myAgent.blockingReceive();
			if(msg.getPerformative() == ACLMessage.REFUSE)
			{
				participants.remove(msg.getSender());
			}

			else if(msg.getPerformative() == ACLMessage.PROPOSE) {

				double bidPrice = Double.parseDouble(msg.getContent());
				System.out.println("   " + this.myAgent.getLocalName() + ": recebi proposta do " + msg.getSender().getName() +" com valor "+ bidPrice);

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
		send(participants, "Auction for product " + prod.getProductName() + " ended, the winner is "+ winner.getName() + " with price " + priceIteration, ACLMessage.INFORM);
		
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);

        msg.addReceiver(winner);
        msg.setContent("pong");
        this.myAgent.send(msg);
        
        state = END;
	}



	@Override
	public boolean done() {
		return(state == END);
	}

}
