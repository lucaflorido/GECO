package geco.print;

import geco.vo.Company;
import geco.vo.Head;
import geco.vo.Row;

public class PrintCompany {
	public String azienda_nome;
	public String azienda_indirizzo;
	public String azienda_indirizzo2;
	public String azienda_pi;
	public String azienda_cf;
	public String azienda_telefono;
	public String azienda_fax;
	public String azienda_email;
	public String getAzienda_telefono() {
		return azienda_telefono;
	}
	public void setAzienda_telefono(String azienda_telefono) {
		this.azienda_telefono = azienda_telefono;
	}
	public String getAzienda_fax() {
		return azienda_fax;
	}
	public void setAzienda_fax(String azienda_fax) {
		this.azienda_fax = azienda_fax;
	}
	public String getAzienda_email() {
		return azienda_email;
	}
	public void setAzienda_email(String azienda_email) {
		this.azienda_email = azienda_email;
	}
	public String getAzienda_nome() {
		return azienda_nome;
	}
	public void setAzienda_nome(String azienda_nome) {
		this.azienda_nome = azienda_nome;
	}
	public String getAzienda_indirizzo() {
		return azienda_indirizzo;
	}
	public void setAzienda_indirizzo(String azienda_indirizzo) {
		this.azienda_indirizzo = azienda_indirizzo;
	}
	public String getAzienda_indirizzo2() {
		return azienda_indirizzo2;
	}
	public void setAzienda_indirizzo2(String azienda_indirizzo2) {
		this.azienda_indirizzo2 = azienda_indirizzo2;
	}
	public String getAzienda_pi() {
		return azienda_pi;
	}
	public void setAzienda_pi(String azienda_pi) {
		this.azienda_pi = azienda_pi;
	}
	public String getAzienda_cf() {
		return azienda_cf;
	}
	public void setAzienda_cf(String azienda_cf) {
		this.azienda_cf = azienda_cf;
	}
	public void setFromObject(Company comp){
		if (comp != null){
			this.azienda_cf = comp.getTaxcode();
			this.azienda_indirizzo = comp.getAddress().getAddress()+" "+comp.getAddress().getNumber();
			this.azienda_indirizzo2 = comp.getAddress().getZipcode()+" "+comp.getAddress().getCity()+" ("+comp.getAddress().getZone()+")";
			this.azienda_nome = comp.getCompanyname();
			this.azienda_telefono = comp.getContact().getPhone1();
			this.azienda_fax = comp.getContact().getFax();
			this.azienda_email = comp.getContact().getEmail1();
		}
	}
}
