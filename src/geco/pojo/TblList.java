package geco.pojo;

import geco.hibernate.DataUtilConverter;
import geco.vo.Ivo;
import geco.vo.List;
import geco.vo.ListProduct;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tblList")
public class TblList implements Itbl {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="idList")
	private int idList;
	@Column(name="code")
	private String code;
	@Column(name="description")
	private String description;
	@Column(name="name")
	private String name;
	@OneToMany(fetch= FetchType.LAZY,mappedBy = "list",cascade = CascadeType.ALL)
	private Set<TblListProduct> listproduct;
	@Column(name="startdate")
	private Date startdate;
	@Column(name="active")
	private boolean active;
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public int getIdList() {
		return idList;
	}
	public void setIdList(int idList) {
		this.idList = idList;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	public Set<TblListProduct> getListproduct() {
		return listproduct;
	}
	public void setListproduct(Set<TblListProduct> listproduct) {
		this.listproduct = listproduct;
	}
	public void convertToTable(Ivo obj){
		List lt = (List)obj;
		this.code = lt.getCode();
		this.description = lt.getDescription();
		this.idList = lt.getIdList();
		this.name  = lt.getName();
		this.startdate = DataUtilConverter.convertDateFromString(lt.getStartdate());
	}
	public void convertToTableSingle(Ivo obj){
		List lt = (List)obj;
		this.code = lt.getCode();
		this.description = lt.getDescription();
		this.idList = lt.getIdList();
		this.name  = lt.getName();
		this.startdate = DataUtilConverter.convertDateFromString(lt.getStartdate());
		this.active = lt.isActive();
		this.listproduct = new HashSet<TblListProduct>();
		if (lt.getListproduct() != null){
			for (Iterator<ListProduct> iterator = lt.getListproduct().iterator(); iterator.hasNext();){
				ListProduct listproduct = iterator.next();
				TblListProduct listp = new TblListProduct();
				listp.convertToTable(listproduct);
				this.listproduct.add(listp);
			}
		}
	}
	public void convertToTableForSaving(Ivo obj){
		List lt = (List)obj;
		this.code = lt.getCode();
		this.description = lt.getDescription();
		this.idList = lt.getIdList();
		this.name  = lt.getName();
		this.active = lt.isActive();
		this.startdate = DataUtilConverter.convertDateFromString(lt.getStartdate());
		this.listproduct = new HashSet<TblListProduct>();
		if (lt.getListproduct() != null){
			for (Iterator<ListProduct> iterator = lt.getListproduct().iterator(); iterator.hasNext();){
				ListProduct listproduct = iterator.next();
				TblListProduct listp = new TblListProduct();
				listp.convertToTableForSaving(listproduct,this);
				/*if (listp.getStartdate() == null){
					listp.setStartdate(this.startdate);
					listp.setActive(true);
				}*/
				this.listproduct.add(listp);
			}
		}
	}
}
