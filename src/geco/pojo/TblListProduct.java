package geco.pojo;

import geco.hibernate.DataUtilConverter;
import geco.vo.Ivo;
import geco.vo.ListProduct;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="tblList_Product")
public class TblListProduct implements Itbl{
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="idListProduct")
	private int idListProduct;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idList")
	private TblList list;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idProduct")
	private TblProduct product;
	@Column(name="price")
	private float price;
	@Column(name="startdate")
	private Date startdate;
	@Column(name="active")
	private boolean active;
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public int getIdListProduct() {
		return idListProduct;
	}
	public void setIdListProduct(int idListProduct) {
		this.idListProduct = idListProduct;
	}
	public TblList getList() {
		return list;
	}
	public void setList(TblList list) {
		this.list = list;
	}

	public TblProduct getProduct() {
		return product;
	}
	public void setProduct(TblProduct product) {
		this.product = product;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public void convertToTable(Ivo obj){
		ListProduct ltp = (ListProduct)obj;
		this.idListProduct = ltp.getIdListProduct();
		this.price = ltp.getPrice();
		this.product = new TblProduct();
		
		if (ltp.getProduct()!=null){
			this.product.convertToTable(ltp.getProduct());
		}
	}
	public void convertToTableForSaving(Ivo obj,Itbl tbl){
		convertToTable(obj);
		
		this.startdate = DataUtilConverter.convertDateFromString(((ListProduct)obj).getStartdate());
		this.active = ((ListProduct)obj).isActive();
		if (this.startdate == null && this.idListProduct == 0){
			this.startdate = ((TblList)tbl).getStartdate();
			this.active = true;
		}
		this.list = (TblList)tbl;
	}
}
