package auction.behavious;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class InitiatorBehaviour extends BaseBehaviour{
	
	public final int INFORM 		= 1;
	public final int CFP			= 2;
	public final int SEND_PROPOSAL	= 3;
	public final int	GET_REPLY		= 4;
	
	private ArrayList<AID>	participants = new ArrayList<>();
	
	int n= 0;
	
	public InitiatorBehaviour()
	{
		this.state = INFORM;

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
           // envia mensagem "pong" inicial a todos os agentes "ping"
           ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
           for(int i=0; i<result.length; ++i)
           {
        	   AID receiver = result[i].getName();   
        	   msg.addReceiver(receiver);
        	   participants.add(receiver);
           }
           msg.setContent("English Auctin is gonna start!");
           this.myAgent.send(msg);
           
        } catch(FIPAException e) { e.printStackTrace(); }
        
        state = CFP;
	}
	
	public void callForProposal() {
		System.out.println("Call For Proposal!");

	}

	@Override
	public boolean done() {
		return n==1;
	}

}
