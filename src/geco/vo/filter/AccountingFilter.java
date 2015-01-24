package geco.vo.filter;

import geco.vo.Customer;
import geco.vo.Supplier;

public class AccountingFilter {
	public String dateFrom;
	public String dateTo;
	public Customer customer;
	public Supplier supplier;
	public boolean paid;
	public boolean expired;
	public boolean isCustomer;
	public boolean isSupplier;
}
