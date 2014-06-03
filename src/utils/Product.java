package utils;

public class Product {

	private String productName;
	private double commonPrice;
	
	public Product(String productName, double commonPrice)
	{
		this.productName = productName;
		this.commonPrice = commonPrice;
	}
	
	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public double getCommonPrice() {
		return commonPrice;
	}
	
	public void setCommonPrice(double commonPrice) {
		this.commonPrice = commonPrice;
	}

}
