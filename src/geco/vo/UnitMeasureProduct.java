package geco.vo;

import geco.pojo.Itbl;
import geco.pojo.TblUnitMeasureProduct;
import geco.properties.GECOParameter;

public class UnitMeasureProduct implements Ivo {
	private int idUnitMeasureProduct;
	private boolean preference;
	private float conversion;
	private Product product;
	private UnitMeasure um;
	private String code;
	private String barcode;
	private String realbarcode;
	private int weightbarcode;
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
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public UnitMeasure getUm() {
		return um;
	}
	public void setUm(UnitMeasure um) {
		this.um = um;
	}
	public void convertFromTable(Itbl obj){
		TblUnitMeasureProduct um = (TblUnitMeasureProduct)obj;
		this.conversion = um.getConversion();
		this.idUnitMeasureProduct = um.getIdUnitMeasureProduct();
		this.preference = um.isPreference();
		this.code = um.getCode();
		this.barcode = um.getBarcode();
		this.weightbarcode = um.getWeightbarcode();
		this.realbarcodenumber = um.getRealbarcodenumber();
		if (um.getUm() != null){
			this.um = new UnitMeasure();
			this.um.convertFromTable(um.getUm());
		}
	}
	public void convertFromTableSearch(Itbl obj){
		convertFromTable(obj);
		TblUnitMeasureProduct um = (TblUnitMeasureProduct)obj;
		if (um.getProduct() != null){
			this.product = new Product();
			this.product.convertFromTable(um.getProduct());
		}
	}
	public GECOObject control(){
		return null;
	}
}
