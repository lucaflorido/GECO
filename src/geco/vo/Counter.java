package geco.vo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import geco.pojo.Itbl;
import geco.pojo.TblCounter;
import geco.pojo.TblCounterYear;
import geco.properties.GECOParameter;


public class Counter implements Ivo {
	private int idCounter;
	private String code;
	private String name;
	private String description;
	private Set<CounterYear> yearsvalue;
	public Set<CounterYear> getYearsvalue() {
		return yearsvalue;
	}
	public void setYearsvalue(Set<CounterYear> yearsvalue) {
		this.yearsvalue = yearsvalue;
	}
	public int getIdCounter() {
		return idCounter;
	}
	public void setIdCounter(int idCounter) {
		this.idCounter = idCounter;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void convertFromTable(Itbl tbl){
		TblCounter co = (TblCounter)tbl;
		this.code = co.getCode();
		this.description = co.getDescription();
		this.idCounter = co.getIdCounter();
		this.name = co.getName();
		if (co.getYearsvalue() != null){
			this.yearsvalue = new HashSet<CounterYear>();
			for (Iterator<TblCounterYear> iterator = co.getYearsvalue().iterator(); iterator.hasNext();){
				CounterYear cy = new CounterYear();
				TblCounterYear tcy = iterator.next(); 
				cy.convertFromTable(tcy);
				this.yearsvalue.add(cy);
			}
		}
		
	}
	public GECOObject control(){
		if (this.code == null || this.code.equals("") == true){
			return new GECOError(GECOParameter.ERROR_VALUE_MISSING,"Codice Mancante");
		}
		if (this.name == null || this.name.equals("") == true){
			return new GECOError(GECOParameter.ERROR_VALUE_MISSING,"Nome Mancante");
		}
		return null;
	}
}
