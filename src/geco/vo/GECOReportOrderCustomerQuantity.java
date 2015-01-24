package geco.vo;

public class GECOReportOrderCustomerQuantity {
	private String customername;
	private double quantity;
	private int idRow;
	private String um;
	private double total;
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getUm() {
		return um;
	}
	public void setUm(String um) {
		this.um = um;
	}
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public int getIdRow() {
		return idRow;
	}
	public void setIdRow(int idRow) {
		this.idRow = idRow;
	}
	
}
