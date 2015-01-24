package geco.vo;

import geco.pojo.Itbl;
import geco.pojo.TblReport;

public class Report implements Ivo {
	private int idReport;
	private String path;
	private String name;
	private String description;
	public int getIdReport() {
		return idReport;
	}
	public void setIdReport(int idReport) {
		this.idReport = idReport;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
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
	public void convertFromTable(Itbl obj){
		TblReport rep = (TblReport)obj;
		this.idReport = rep.getIdReport();
		this.description = rep.getDescription();
		this.name = rep.getName();
		this.path = rep.getPath();
	}
	public GECOObject control(){
		return null;
	}
	public GECOObject setupNewReport(String name,String path){
		String[] filename = name.split("\\.");
		if (filename.length == 2){
			if (filename[1].toLowerCase().equals("jrxml") == false){
				return new GECOError("Name","Estensione del file non conforme. Il report deve essere .jrxml");
			}
		this.name = filename[0];
		}else{
			return new GECOError("Name","Estensione del file non conforme. Il report deve essere .jrxml");
		}
		this.path = path;
		return new GECOSuccess();
	}
}
