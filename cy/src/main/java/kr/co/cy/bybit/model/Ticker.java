package kr.co.cy.bybit.model;

public class Ticker {
    private String symbol;
    private double price;
    private double volume;
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}
	@Override
	public String toString() {
		return "Ticker [symbol=" + symbol + ", price=" + price + ", volume=" + volume + "]";
	}
    
}
