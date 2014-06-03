package auction.agents;

import java.util.ArrayList;

import utils.Product;
import auction.behavious.InitiatorBehaviour;


@SuppressWarnings("serial")
public class InitiatorAgent extends BaseAgent{

	public final String type = "Iniciator";
	
	public static final int	AUCTION_TIMEOUT		= 500;

	//Product to sell with initial price
	//private static Map<String, Integer> productToSell = new HashMap<String, Integer>();
	
	private ArrayList<Product> productToSell = new ArrayList<>();

	public InitiatorAgent()
	{
		productToSell.add(new Product("Smartphone", 300));
		productToSell.add(new Product("Tablet", 400));
		productToSell.add(new Product("Laptop", 800));

		System.out.println("Iniciator constructor!");
	}
	
	@Override
	protected void setup()
	{
		super.setup();
		for(Product prod : productToSell)
		{
			addBehaviour(new InitiatorBehaviour(prod));
		}
	}

	@Override
	public String getType() {

		return this.type;
	}

}
