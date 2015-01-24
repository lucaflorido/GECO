package geco.vo;

import geco.pojo.Itbl;
import geco.pojo.TblCustomer;
import geco.pojo.TblDestination;
import geco.pojo.TblListCustomer;
import geco.properties.GECOParameter;
import it.progess.transport.check.ProgessCheck;
import it.progess.transport.vo.ProgessError;
import it.progess.validator.CFPIValidator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class Customer implements Ivo{
	private int idCustomer;
	private String customername;
	private String customercode;
	private boolean active;
	private Contact contact;
	private Address address;
	private String taxcode;
	private String serialnumber;
	private GroupCustomer group;
	private CategoryCustomer category;
	private BankContact bankcontact;
	private Set<ListCustomer> lists;
	private Set<Destination> destinations;
	private double suspended;
	private Payment payment;
	private Transporter transporter;
	
	public Transporter getTransporter() {
		return transporter;
	}
	public void setTransporter(Transporter transporter) {
		this.transporter = transporter;
	}
	public Payment getPayment() {
		return payment;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	public double getSuspended() {
		return suspended;
	}
	public void setSuspended(double suspended) {
		this.suspended = suspended;
	}
	public Set<Destination> getDestinations() {
		return destinations;
	}
	public void setDestinations(Set<Destination> destinations) {
		this.destinations = destinations;
	}
	public int getIdCustomer() {
		return idCustomer;
	}
	public void setIdCustomer(int idCustomer) {
		this.idCustomer = idCustomer;
	}
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public String getCustomercode() {
		return customercode;
	}
	public void setCustomercode(String customercode) {
		this.customercode = customercode;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public String getTaxcode() {
		return taxcode;
	}
	public void setTaxcode(String taxcode) {
		this.taxcode = taxcode;
	}
	public String getSerialnumber() {
		return serialnumber;
	}
	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}
	public GroupCustomer getGroup() {
		return group;
	}
	public void setGroup(GroupCustomer group) {
		this.group = group;
	}
	public CategoryCustomer getCategory() {
		return category;
	}
	public void setCategory(CategoryCustomer category) {
		this.category = category;
	}
	public BankContact getBankcontact() {
		return bankcontact;
	}
	public void setBankcontact(BankContact bankcontact) {
		this.bankcontact = bankcontact;
	}
	public Set<ListCustomer> getLists() {
		return lists;
	}
	public void setLists(Set<ListCustomer> lists) {
		this.lists = lists;
	}
	public void convertFromTable(Itbl obj){
		TblCustomer c = (TblCustomer)obj;
		this.active = c.isActive();
		this.suspended = c.getSuspended();
		if (c.getAddress() != null){
			this.address = new Address();
			this.address.convertFromTable(c.getAddress());
		}
		
		if (c.getBankcontact()!= null){
			this.bankcontact = new BankContact();
			this.bankcontact.convertFromTable(c.getBankcontact());
		}
		
		if(c.getCategory() != null){
			this.category = new CategoryCustomer();
			this.category.convertFromTable(c.getCategory());
		}
		
		if(c.getContact() != null){
			this.contact = new Contact();
			this.contact.convertFromTable(c.getContact());
		}
		this.customercode = c.getCustomercode();
		this.customername = c.getCustomername();
		
		if (c.getGroup() != null){
			this.group = new GroupCustomer();
			this.group.convertFromTable(c.getGroup());
		}
		if (c.getPayment() != null){
			this.payment = new Payment();
			this.payment.convertFromTable(c.getPayment());
		}
		if (c.getTransporter() != null){
			this.transporter = new Transporter();
			this.transporter.convertFromTable(c.getTransporter());
		}
		this.idCustomer = c.getIdCustomer();
		this.serialnumber = c.getSerialnumber();
		this.taxcode = c.getTaxcode();
		this.lists = new HashSet<ListCustomer>();
	}
	public void convertFromTableSingle(Itbl obj){
		TblCustomer c = (TblCustomer)obj;
		this.convertFromTable(obj);
		this.lists = new HashSet<ListCustomer>();
		if (c.getLists() != null){
			for (Iterator<TblListCustomer> iterator = c.getLists().iterator(); iterator.hasNext();){
				TblListCustomer listproduct = iterator.next();
				ListCustomer listp = new ListCustomer();
				listp.convertFromTable(listproduct);
				this.lists.add(listp);
			}
		}
		this.destinations = new HashSet<Destination>();
		if (c.getDestinations() != null){
			for (Iterator<TblDestination> iterator = c.getDestinations().iterator(); iterator.hasNext();){
				TblDestination destination = iterator.next();
				Destination dest = new Destination();
				dest.convertFromTable(destination);
				this.destinations.add(dest);
			}
		}
	}
	public GECOError control(){
		GECOError er = null;
		if (this.customercode == null || this.customercode.equals("") ){
			er = new GECOError(GECOParameter.ERROR_VALUE_MISSING,"Codice mancante");
		}
		if (this.customername == null || this.customername ==""){
			er = new GECOError(GECOParameter.ERROR_VALUE_MISSING,"Ragione Sociale mancante");
		}
		
		if (this.payment == null){
			er = new GECOError(GECOParameter.ERROR_VALUE_MISSING,"Pagamento Mancante");
		}
		
		if (ProgessCheck.basicCheck(CFPIValidator.checkCFPI(this.taxcode, this.serialnumber, true)) == false){
			ProgessError pe = (ProgessError)CFPIValidator.checkCFPI(this.taxcode, this.serialnumber, true);
			er =  new GECOError(GECOParameter.ERROR_VALUE_MISSING,pe.getErrorMessage());
		}
		
		return er;
	}
}
