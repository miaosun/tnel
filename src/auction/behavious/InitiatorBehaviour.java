package auction.behavious;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.Calendar;

import utils.Product;

@SuppressWarnings("serial")
public class InitiatorBehaviour extends BaseBehaviour{

	public final int INFORM 		= 1;
	public final int CFP			= 2;
	public final int GET_PROPOSE	= 3;
	public final int END			= 4;

	private final int TIMEOUT	= 60;

	private ArrayList<AID>	participants = new ArrayList<>();
	private double priceIteration = 0;
	private int round = 0;

	private int lastCFPInSeconds;

	public InitiatorBehaviour(Product prod)
	{
		this.state = INFORM;
		this.priceIteration = prod.getCommonPrice() * 0.2;
		round = 1;
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
			System.out.println("Nr of participants: "+result.length);

			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			for(int i=0; i<result.length; ++i)
			{
				AID receiver = result[i].getName();   
				msg.addReceiver(receiver);
				//Iniciar a lista de participantes
				participants.add(receiver);
			}
			msg.setContent("English Auctin is gonna start!");
			this.myAgent.send(msg);

		} catch(FIPAException e) { e.printStackTrace(); }

		state = CFP;
	}

	public void callForProposal() {

		System.out.println("ROUND "+ round + ": Call for proposal");
		send(participants, "Previous winning price: "+priceIteration+ "€", ACLMessage.CFP);

		Calendar calendar = Calendar.getInstance();
		lastCFPInSeconds = calendar.get(Calendar.SECOND);
	}

	public void getPropose() {

		//se passar timeout, state = END;
		//se nao, defina o preco base da iteracao como o valor vencidor da iteracao anterior, e state = CFP

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
			

			if(msg.getPerformative() == ACLMessage.PROPOSE) {

				double bidPrice = Double.parseDouble(msg.getContent());
				System.out.println("   " + this.myAgent.getLocalName() + ": recebi proposta do " + msg.getSender().getName() +" com valor "+ bidPrice + "€");
				
				if(bidPrice > priceIteration)
					priceIteration = bidPrice;
			}
		}
		state = CFP;
	}

	@Override
	public boolean done() {
		return(state == END);
	}

}
