import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import auction.agents.InitiatorAgent;
import auction.agents.ParticipantAgent;

public class JADELauncher {

	public static void main(String[] args) {
		Runtime rt = Runtime.instance();

		Profile p1 = new ProfileImpl();
		//p1.setParameter(...);
		ContainerController mainContainer = rt.createMainContainer(p1);

		AgentController ac1;
		try {
			ac1 = mainContainer.acceptNewAgent("Par1", new ParticipantAgent());
			ac1.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}

		AgentController ac2;
		try {
			ac2 = mainContainer.acceptNewAgent("Par2", new ParticipantAgent());
			ac2.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}

		AgentController ac3;
		try {
			ac3 = mainContainer.acceptNewAgent("Par3", new ParticipantAgent());
			ac3.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
		
		AgentController ac4;
		try {
			ac4 = mainContainer.acceptNewAgent("Ini", new InitiatorAgent());
			ac4.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}

}
