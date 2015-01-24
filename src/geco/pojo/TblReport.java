package geco.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import geco.vo.Ivo;
import geco.vo.Report;
@Entity
@Table(name="tblReport")
public class TblReport implements Itbl {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="idReport")
	private int idReport;
	@Column(name="path")
	private String path;
	@Column(name="name")
	private String name;
	@Column(name="description")
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
	public void convertToTable(Ivo obj){
		Report rep = (Report)obj;
		this.idReport = rep.getIdReport();
		this.description = rep.getDescription();
		this.name = rep.getName();
		this.path = rep.getPath();
	}
}
