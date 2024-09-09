package com.websyh.domain;

public class ProductDTO {
	private String no;
	private String name;
	private String price;
	
	public ProductDTO(String no, String name, String price) {
		super();
		this.no = no;
		this.name = name;
		this.price = price;
	}
	
	public ProductDTO() {
		
	}
	
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return "ProductDTO [no=" + no + ", name=" + name + ", price=" + price + "]";
	}
}
