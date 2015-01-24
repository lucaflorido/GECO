package geco.vo;

import geco.pojo.Itbl;
import geco.pojo.TblStoreMovement;
import geco.properties.GECOParameter;

public class StoreMovement implements Ivo {
	private int idstoremovement;
	private String name;
	private Boolean genericLoad;
	private Boolean supplierLoad;
	private Boolean comebackLoad;
	private Boolean internalLoad;
	private Boolean expiredUnload;
	private Boolean customerUnload;
	private Boolean genericUnload;
	private Boolean internalUnload;
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
	public void convertFromTable(Itbl itbl){
		TblStoreMovement sm = (TblStoreMovement)itbl;
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
	public GECOObject control(){
		if (this.name == null || this.name.equals("") == true){
			return new GECOError(GECOParameter.ERROR_VALUE_MISSING,"Nome Mancante");
		}
		return null;
	}
	public boolean isLoad(){
		if (this.genericLoad == true || this.internalLoad == true || this.supplierLoad == true || this.comebackLoad == true)
			return true;
		else
			return false;
	}
	public boolean isUnload(){
		if (this.genericUnload == true || this.internalUnload == true || this.customerUnload == true || this.expiredUnload == true)
			return true;
		else
			return false;
	}
	public boolean isStoreMovementActive(){
		return  (isLoad() == true || isUnload() == true);
	}
}
