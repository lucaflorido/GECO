package geco.vo;

import java.util.Set;

public class GECOReportOrderProduct implements Comparable<GECOReportOrderProduct> {
	private String productcode;
	private String productdescription;
	private String productum;
	private Set<GECOReportOrderCustomerQuantity> customers;
	private double total;
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getProductcode() {
		return productcode;
	}
	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}
	public String getProductdescription() {
		return productdescription;
	}
	public void setProductdescription(String productdescription) {
		this.productdescription = productdescription;
	}
	public String getProductum() {
		return productum;
	}
	public void setProductum(String productum) {
		this.productum = productum;
	}
	public Set<GECOReportOrderCustomerQuantity> getCustomers() {
		return customers;
	}
	public void setCustomers(Set<GECOReportOrderCustomerQuantity> customers) {
		this.customers = customers;
	}
	@Override
	public int compareTo(GECOReportOrderProduct p){
		return this.productcode.compareTo(p.getProductcode());
	}
	
}
