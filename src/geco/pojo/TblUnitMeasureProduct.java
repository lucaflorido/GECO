package geco.pojo;

import geco.vo.Ivo;
import geco.vo.UnitMeasureProduct;

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
@Table(name="tblUnitMeasure_Product")
public class TblUnitMeasureProduct implements Itbl {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="idUnitMeasure_Product")
	private int idUnitMeasureProduct;
	@Column(name="preference")
	private boolean preference;
	@Column(name="conversion")
	private float conversion;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idProduct")
	private TblProduct product;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idUnitMeasure")
	private TblUnitMeasure um;
	@Column(name="code")
	private String code;
	@Column(name="barcode")
	private String barcode;
	@Column(name="realbarcode")
	private String realbarcode;
	@Column(name="weightbarcode")
	private int weightbarcode;
	@Column(name="realbarcodenumber")
	private int realbarcodenumber;
	public int getRealbarcodenumber() {
		return realbarcodenumber;
	}
	public void setRealbarcodenumber(int realbarcodenumber) {
		this.realbarcodenumber = realbarcodenumber;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getRealbarcode() {
		return realbarcode;
	}
	public void setRealbarcode(String realbarcode) {
		this.realbarcode = realbarcode;
	}
	public int getWeightbarcode() {
		return weightbarcode;
	}
	public void setWeightbarcode(int weightbarcode) {
		this.weightbarcode = weightbarcode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public TblProduct getProduct() {
		return product;
	}
	public void setProduct(TblProduct product) {
		this.product = product;
	}
	public int getIdUnitMeasureProduct() {
		return idUnitMeasureProduct;
	}
	public void setIdUnitMeasureProduct(int idUnitMeasureProduct) {
		this.idUnitMeasureProduct = idUnitMeasureProduct;
	}
	public boolean isPreference() {
		return preference;
	}
	public void setPreference(boolean preference) {
		this.preference = preference;
	}
	public float getConversion() {
		return conversion;
	}
	public void setConversion(float conversion) {
		this.conversion = conversion;
	}
	public TblUnitMeasure getUm() {
		return um;
	}
	public void setUm(TblUnitMeasure um) {
		this.um = um;
	}
	public void convertToTable(Ivo obj){
		UnitMeasureProduct um = (UnitMeasureProduct)obj;
		this.conversion = um.getConversion();
		this.idUnitMeasureProduct = um.getIdUnitMeasureProduct();
		this.preference = um.isPreference();
		this.code = um.getCode();
		this.barcode = um.getBarcode();
		this.weightbarcode = um.getWeightbarcode();
		this.realbarcode = um.getBarcode();
		this.realbarcodenumber = um.getRealbarcodenumber();
		if (um.getUm() != null){
			this.um = new TblUnitMeasure();
			this.um.convertToTable(um.getUm());
		}
		if (this.barcode != null && this.barcode.equals("") == false){
			if (this.realbarcodenumber == 0){
				if (this.weightbarcode >0){
					if (this.barcode.length() > this.weightbarcode){
						int l = this.barcode.length();
						l = l-1-this.weightbarcode;
						this.realbarcode = this.barcode.substring(0,l);
					}
				}else{
					int l = this.barcode.length();
					this.realbarcode = this.barcode.substring(0,l-1);
				}
			}else{
				this.realbarcode = this.barcode.substring(0,this.realbarcodenumber);
			}
		}
		
	}
	public void convertToTableForSaving(Ivo obj,Itbl itbl){
		
		this.convertToTable(obj);
		this.product = (TblProduct)itbl;
	}
}
