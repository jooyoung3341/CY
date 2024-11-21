package kr.co.cy.gecko.model;

import java.util.List;

public class Coin {
    private String id;
    private String symbol;
    private String marketCap;
    private List<String> meta;
    private String error;
    
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getMarketCap() {
		return marketCap;
	}
	public void setMarketCap(String marketCap) {
		this.marketCap = marketCap;
	}
	
	public List<String> getMeta() {
		return meta;
	}
	public void setMeta(List<String> meta) {
		this.meta = meta;
	}
	@Override
	public String toString() {
		return "Coin [id=" + id + ", symbol=" + symbol + ", marketCap=" + marketCap + ", meta=" + meta + "]";
	}
    

    
    
}
