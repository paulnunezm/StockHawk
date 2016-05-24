package com.sam_chordas.android.stockhawk.entities;

/**
 * Created by paulnunez on 5/21/16.
 */
public class Quote {

  private String symbol;
  private String bidPrice;
  private String name;
  private String percentChange;
  private String change;
  private String isUp;

  public Quote(){}

  /**
   *
   * @param symbol
   * @param name
   * @param change
   */
  public Quote(String symbol, String name,  String change, String isUp) {
    this.symbol = symbol;
    this.name = name;
    this.change = change;
    this.isUp = isUp;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getBidPrice() {
    return bidPrice;
  }

  public void setBidPrice(String bidPrice) {
    this.bidPrice = bidPrice;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPercentChange() {
    return percentChange;
  }

  public void setPercentChange(String percentChange) {
    this.percentChange = percentChange;
  }

  public String getChange() {
    return change;
  }

  public void setChange(String change) {
    this.change = change;
  }

  public String getIsUp() {
    return isUp;
  }

  public void setIsUp(String isUp) {
    this.isUp = isUp;
  }
}
