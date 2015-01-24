package geco.vo;

import geco.pojo.Itbl;
import geco.pojo.TblUnitMeasure;
import geco.properties.GECOParameter;

public class UnitMeasure implements Ivo {
	private int idUnitMeasure;
	private String description;
	private String code;
	private String name;
	
	public int getIdUnitMeasure() {
		return idUnitMeasure;
	}
	public void setIdUnitMeasure(int idUnitMeasure) {
		this.idUnitMeasure = idUnitMeasure;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public void convertFromTable(Itbl obj){
		TblUnitMeasure um = (TblUnitMeasure)obj;
		this.idUnitMeasure = um.getIdUnitMeasure();
		this.description = um.getDescription();
		this.code = um.getCode();
		this.name = um.getName();
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
