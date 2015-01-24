package geco.vo;

import geco.hibernate.HibernateUtils;

import java.util.Iterator;
import java.util.Set;

public class HeadTotalCalculation {
	private float amount4;
	private float taxamount4;
	private float total4;
	private float amount10;
	private float taxamount10;
	private float total10;
	private float amount20;
	private float taxamount20;
	private float total20;
	private float amount;
	private float taxamount;
	private float total;
	private Set<Row> rows;
	private double value4;
	private double value10;
	private double value22;
	public float getAmount4() {
		return amount4;
	}
	public void setAmount4(float amount4) {
		this.amount4 = amount4;
	}
	public float getTaxamount4() {
		return taxamount4;
	}
	public void setTaxamount4(float taxamount4) {
		this.taxamount4 = taxamount4;
	}
	public float getTotal4() {
		return total4;
	}
	public void setTotal4(float total4) {
		this.total4 = total4;
	}
	public float getAmount10() {
		return amount10;
	}
	public void setAmount10(float amount10) {
		this.amount10 = amount10;
	}
	public float getTaxamount10() {
		return taxamount10;
	}
	public void setTaxamount10(float taxamount10) {
		this.taxamount10 = taxamount10;
	}
	public float getTotal10() {
		return total10;
	}
	public void setTotal10(float total10) {
		this.total10 = total10;
	}
	public float getAmount20() {
		return amount20;
	}
	public void setAmount20(float amount20) {
		this.amount20 = amount20;
	}
	public float getTaxamount20() {
		return taxamount20;
	}
	public void setTaxamount20(float taxamount20) {
		this.taxamount20 = taxamount20;
	}
	public float getTotal20() {
		return total20;
	}
	public void setTotal20(float total20) {
		this.total20 = total20;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public float getTaxamount() {
		return taxamount;
	}
	public void setTaxamount(float taxamount) {
		this.taxamount = taxamount;
	}
	public float getTotal() {
		return total;
	}
	public void setTotal(float total) {
		this.total = total;
	}
	public Set<Row> getRows() {
		return rows;
	}
	public void setRows(Set<Row> rows) {
		this.rows = rows;
	}
	public void calculation(){
		for(Iterator<Row> itr = rows.iterator();itr.hasNext();){
			Row row = itr.next();
			this.setTaxesAmount(row.getTaxrate(), row);
		}
		this.amount = this.amount4 + this.amount10+ this.amount20;
		this.taxamount = this.taxamount4 + this.taxamount10+ this.taxamount20;
		this.total = this.total4 + this.total10+ this.total20;
		this.rows = null;
	}
	public void calculation(Head head){
		this.rows = head.getRows();
		for(Iterator<Row> itr = rows.iterator();itr.hasNext();){
			Row row = itr.next();
			this.setTaxesAmount(row.getTaxrate(), row);
		}
		this.amount = this.amount4 + this.amount10+ this.amount20;
		this.taxamount = this.taxamount4 + this.taxamount10+ this.taxamount20;
		this.total = this.total4 + this.total10+ this.total20;
		head.setAmount(amount);
		head.setTaxamount(taxamount);
		head.setTotal(total);
		
		head.setAmount4(amount4);
		double tot4floor = Double.parseDouble(new Float(amount4).toString());
		tot4floor = tot4floor/100*this.value4;
		tot4floor = Math.ceil(tot4floor*100)/100;
		head.setTaxamount4(Float.parseFloat(new Double(tot4floor).toString()));
		head.setTotal4(head.getAmount4()+head.getTaxamount4());
		
		head.setAmount10(amount10);
		double tot10floor = Double.parseDouble(new Float(amount10).toString());
		tot10floor = tot10floor/100*this.value10;
		tot10floor = Math.ceil(tot10floor*100)/100;
		head.setTaxamount10(Float.parseFloat(new Double(tot10floor).toString()));
		head.setTotal10(head.getAmount10()+head.getTaxamount10());
		
		head.setAmount20(amount20);
		double tot20floor = Double.parseDouble(new Float(amount20).toString());
		tot20floor = tot20floor/100*this.value22;
		tot20floor = Math.ceil(tot20floor*100)/100;
		head.setTaxamount20(Float.parseFloat(new Double(tot20floor).toString()));
		head.setTotal20(head.getAmount20()+head.getTaxamount20());
		head.setTotal(head.getTotal10()+head.getTotal4()+head.getTotal20());
		
	}
	private void setTaxesAmount(TaxRate t,Row r){
		if (r.getType().endsWith("V")){
			if (r.getTaxrate().getValue() < 10){
				this.value4 = r.getTaxrate().getValue();
				this.taxamount4 = this.taxamount4 + r.getTaxamount();
				this.amount4 = this.amount4 + r.getAmount();
				this.total4 = this.total4 + r.getTotal();
			}
			if (r.getTaxrate().getValue() >= 10 && r.getTaxrate().getValue() < 20){
				this.value10 = r.getTaxrate().getValue();
				this.taxamount10 = this.taxamount10 + r.getTaxamount();
				this.amount10 = this.amount10 + r.getAmount();
				this.total10 = this.total10 + r.getTotal();
			}
			if (r.getTaxrate().getValue() >= 20){
				this.value22 = r.getTaxrate().getValue();
				this.taxamount20 = this.taxamount20 + r.getTaxamount();
				this.amount20 = this.amount20 + r.getAmount();
				this.total20 = this.total20 + r.getTotal();
			}
		}
	}
}
