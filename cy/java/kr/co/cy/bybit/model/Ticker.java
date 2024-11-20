package kr.co.cy.bybit.model;

public class Ticker {
    private String symbol;
    private String volume24h;
    private String openInterest;
    private String openInterestValue;
    
    private String openPrice;
    private String highPrice;
    
    private String lowPrice;
    private String closePrice;
    
    private String volume;
    private String percent;

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getVolume24h() {
		return volume24h;
	}

	public void setVolume24h(String volume24h) {
		this.volume24h = volume24h;
	}

	public String getOpenInterest() {
		return openInterest;
	}

	public void setOpenInterest(String openInterest) {
		this.openInterest = openInterest;
	}

	public String getOpenInterestValue() {
		return openInterestValue;
	}

	public void setOpenInterestValue(String openInterestValue) {
		this.openInterestValue = openInterestValue;
	}

	public String getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(String openPrice) {
		this.openPrice = openPrice;
	}

	public String getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(String highPrice) {
		this.highPrice = highPrice;
	}

	public String getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(String lowPrice) {
		this.lowPrice = lowPrice;
	}

	public String getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(String closePrice) {
		this.closePrice = closePrice;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	@Override
	public String toString() {
		return "Ticker [symbol=" + symbol + ", volume24h=" + volume24h + ", openInterest=" + openInterest
				+ ", openInterestValue=" + openInterestValue + ", openPrice=" + openPrice + ", highPrice=" + highPrice
				+ ", lowPrice=" + lowPrice + ", closePrice=" + closePrice + ", volume=" + volume + "]";
	}
    

    
}
