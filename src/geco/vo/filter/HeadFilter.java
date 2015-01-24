package geco.vo.filter;

import geco.vo.Customer;
import geco.vo.Document;
import geco.vo.Supplier;

public class HeadFilter {
	public int pageSize;
	public int startelement;
	public String fromDate;
	public String toDate;
	public boolean isCustomer;
	public boolean isSupplier;
	public boolean isOrder;
	public boolean isInvoice;
	public boolean isTransport;
	public Customer customer;
	public Supplier supplier;
	public Document doc;
	public boolean active;
	public boolean inactive;
	public String number;
}
