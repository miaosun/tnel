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
		//productToSell.add(new Product("SmartPhone", 200));
		productToSell.add(new Product("Tablet", 300));
		//productToSell.add(new Product("Laptop", 600));

		System.out.println("Iniciator started working!");
	}
	
	@Override
	protected void setup()
	{
		super.setup();
		for(Product prod : productToSell)
		{
			System.out.println("[English] Auction for Product " + prod.getProductName() + " started!\n");
			addBehaviour(new InitiatorBehaviour(this, prod));
		}
	}

	@Override
	public String getType() {

		return this.type;
	}

}
