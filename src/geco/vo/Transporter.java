package geco.vo;

import it.progess.transport.check.ProgessCheck;
import it.progess.transport.vo.ProgessError;
import it.progess.validator.CFPIValidator;
import geco.pojo.Itbl;
import geco.pojo.TblTransporter;
import geco.properties.GECOParameter;

public class Transporter implements Ivo {
	private int idTransporter;
	private String transportercode;
	private String transportername;
	private String transportersurname;
	private String transporterdescription;
	private String platenumber;
	private String taxcode;
	private String serialnumber;
	private Address address;
	private Contact contact;
	private BankContact bankcontact;
	public int getIdTransporter() {
		return idTransporter;
	}
	public void setIdTransporter(int idTransporter) {
		this.idTransporter = idTransporter;
	}
	public String getTransportercode() {
		return transportercode;
	}
	public void setTransportercode(String transportercode) {
		this.transportercode = transportercode;
	}
	public String getTransportername() {
		return transportername;
	}
	public void setTransportername(String transportername) {
		this.transportername = transportername;
	}
	public String getTransportersurname() {
		return transportersurname;
	}
	public void setTransportersurname(String transportersurname) {
		this.transportersurname = transportersurname;
	}
	public String getTransporterdescription() {
		return transporterdescription;
	}
	public void setTransporterdescription(String transporterdescription) {
		this.transporterdescription = transporterdescription;
	}
	public String getPlatenumber() {
		return platenumber;
	}
	public void setPlatenumber(String platenumber) {
		this.platenumber = platenumber;
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
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public BankContact getBankcontact() {
		return bankcontact;
	}
	public void setBankcontact(BankContact bankcontact) {
		this.bankcontact = bankcontact;
	}
	public void convertFromTable(Itbl obj){
		TblTransporter t = (TblTransporter)obj;
	    this.idTransporter = t.getIdTransporter();
	    this.transportercode = t.getTransportercode();
	    this.transportername = t.getTransportername();
	    this.transportersurname = t.getTransportersurname();
	    this.transporterdescription = t.getTransporterdescription();
	    this.platenumber = t.getPlatenumber();
	    this.taxcode  = t.getTaxcode();
	    this.serialnumber = t.getSerialnumber();
	    if (t.getAddress() != null){
			this.address = new Address();
			this.address.convertFromTable(t.getAddress());
		}
		if (t.getBankcontact()!= null){
			this.bankcontact = new BankContact();
			this.bankcontact.convertFromTable(t.getBankcontact());
		}
		if(t.getContact() != null){
			this.contact = new Contact();
			this.contact.convertFromTable(t.getContact());
		}
	}
	public GECOError control(){
		GECOError er = null;
		if (this.transportername == null || this.transportername ==""){
			er = new GECOError(GECOParameter.ERROR_VALUE_MISSING,"Nome mancante");
		}
		if (this.transportersurname == null || this.transportersurname ==""){
			er = new GECOError(GECOParameter.ERROR_VALUE_MISSING,"Cognome mancante");
		}
		if (this.platenumber == null || this.platenumber ==""){
			er = new GECOError(GECOParameter.ERROR_VALUE_MISSING,"Targa mancante");
		}

		if (this.transportercode == null || this.transportercode ==""){
			er = new GECOError(GECOParameter.ERROR_VALUE_MISSING,"Codice mancante");
		}
		if (ProgessCheck.basicCheck(CFPIValidator.checkCFPI(this.taxcode, this.serialnumber, false,false)) == false){
			ProgessError pe = (ProgessError)CFPIValidator.checkCFPI(this.taxcode, this.serialnumber, false,false);
			er =  new GECOError(GECOParameter.ERROR_VALUE_MISSING,pe.getErrorMessage());
		}
		
		return er;
	}
}
