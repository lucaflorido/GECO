package geco.pojo;

import geco.vo.Ivo;
import geco.vo.StoreMovement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tblStoreMovement")
public class TblStoreMovement implements Itbl{
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="idStoreMovement")
	private int idstoremovement;
	@Column(name="name")
	private String name;
	@Column(name="genericload")
	private Boolean genericLoad;
	@Column(name="supplierload")
	private Boolean supplierLoad;
	@Column(name="comebackload")
	private Boolean comebackLoad;
	@Column(name="internalload")
	private Boolean internalLoad;
	@Column(name="expiredunload")
	private Boolean expiredUnload;
	@Column(name="customerunload")
	private Boolean customerUnload;
	@Column(name="genericunload")
	private Boolean genericUnload;
	@Column(name="internalunload")
	private Boolean internalUnload;
	@Column(name="genericreserved")
	private Boolean genericreserved;
	public Boolean getGenericreserved() {
		return genericreserved;
	}
	public void setGenericreserved(Boolean genericreserved) {
		this.genericreserved = genericreserved;
	}
	public int getIdstoremovement() {
		return idstoremovement;
	}
	public void setIdstoremovement(int idstoremovement) {
		this.idstoremovement = idstoremovement;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getGenericLoad() {
		return genericLoad;
	}
	public void setGenericLoad(Boolean genericLoad) {
		this.genericLoad = genericLoad;
	}
	public Boolean getSupplierLoad() {
		return supplierLoad;
	}
	public void setSupplierLoad(Boolean supplierLoad) {
		this.supplierLoad = supplierLoad;
	}
	public Boolean getComebackLoad() {
		return comebackLoad;
	}
	public void setComebackLoad(Boolean comebackLoad) {
		this.comebackLoad = comebackLoad;
	}
	public Boolean getInternalLoad() {
		return internalLoad;
	}
	public void setInternalLoad(Boolean internalLoad) {
		this.internalLoad = internalLoad;
	}
	public Boolean getExpiredUnload() {
		return expiredUnload;
	}
	public void setExpiredUnload(Boolean expiredUnload) {
		this.expiredUnload = expiredUnload;
	}
	public Boolean getCustomerUnload() {
		return customerUnload;
	}
	public void setCustomerUnload(Boolean customerUnload) {
		this.customerUnload = customerUnload;
	}
	public Boolean getGenericUnload() {
		return genericUnload;
	}
	public void setGenericUnload(Boolean genericUnload) {
		this.genericUnload = genericUnload;
	}
	public Boolean getInternalUnload() {
		return internalUnload;
	}
	public void setInternalUnload(Boolean internalUnload) {
		this.internalUnload = internalUnload;
	}
	public void convertToTable(Ivo vo){
		StoreMovement sm = (StoreMovement)vo;
		this.name = sm.getName();
		this.comebackLoad = sm.getComebackLoad();
		this.customerUnload = sm.getCustomerUnload();
		this.expiredUnload = sm.getExpiredUnload();
		this.genericLoad = sm.getGenericLoad();
		this.genericUnload = sm.getGenericUnload();
		this.idstoremovement = sm.getIdstoremovement();
		this.internalLoad = sm.getInternalLoad();
		this.internalUnload = sm.getInternalUnload();
		this.supplierLoad = sm.getSupplierLoad();
		this.genericreserved = sm.getGenericreserved();
	}
}
