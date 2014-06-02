package auction.behavious;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class InitiatorBehaviour extends BaseBehaviour{
	
	public final int CFP = 1;
	public final int 	DEFINE_BID		= 2;
	public final int	SEND_PROPOSAL	= 3;
	public final int	GET_REPLY		= 4;
	
	int n= 0;
	
	public InitiatorBehaviour()
	{
		this.state = CFP;
		
		System.out.println("what about here!");
	}
	
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		System.out.println("Chegou!");
		switch(state)
		{
		case CFP:
			callForProposal();
			System.out.println("Here!");
			break;
		
		}
	}

	public void callForProposal() {
		
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
              msg.addReceiver(result[i].getName());
           msg.setContent("English Auctin is gonna start!");
           this.myAgent.send(msg);
           
        } catch(FIPAException e) { e.printStackTrace(); }
        
        n++;
	}

	@Override
	public boolean done() {
		return n==1;
	}

}
