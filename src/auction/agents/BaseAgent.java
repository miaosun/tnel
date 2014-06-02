package auction.agents;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;


@SuppressWarnings("serial")
public abstract class BaseAgent extends Agent{

	// --- Constructors --------------------------------------------------------

	public BaseAgent() {
	}

	// --- Getters and Setters -------------------------------------------------

	public abstract String getType();

	// --- Methods (Agent) -----------------------------------------------------

	@Override
	protected void setup() {
		
		registerService();
	}

	@Override
	protected void takeDown() {
		System.out.println("AGENT QUIT! Name: " + this.getLocalName() + ", Type: " + this.getType());
		deregisterService();
	}
	

	// --- Methods -------------------------------------------------------------

	/**
	 * Registers the service that this agent offers in the yellow pages.
	 */
	private void registerService() {
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType(getType());
		sd.setName(getName());
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);
		
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			throw new IllegalStateException(
					"Appeared problem during the service registration.", fe);
		}
	}

	/**
	 * Deregisters the service that this agent offers in the yellow pages.
	 */
	protected void deregisterService() {
		try {
			DFService.deregister(this);
		} catch (FIPAException fe) {
			throw new IllegalStateException(
					"Appeared problem during the service deregistration.", fe);
		}
	}
}
