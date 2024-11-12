package com.example.demo.pojo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Item {

	  private String name;
	  @JsonDeserialize(using = QuantityDeserializer.class)

	    private int quantity;
	    private double rate;
	    private double amount;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getQuantity() {
			return quantity;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		public double getRate() {
			return rate;
		}
		public void setRate(double rate) {
			this.rate = rate;
		}
		public double getAmount() {
			return amount;
		}
		public void setAmount(double amount) {
			this.amount = amount;
		}
	    
	    
}
