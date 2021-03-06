package geco.vo;

import java.util.Date;

import geco.hibernate.DataUtilConverter;
import geco.pojo.Itbl;
import geco.pojo.TblProduct;
import geco.pojo.TblRow;
import geco.pojo.TblTaxrate;
import geco.pojo.TblUnitMeasure;
import geco.properties.GECOParameter;


public class Row implements Ivo{
	private int idRow;
	private String productcode;
	private String productdescription;
	private String productum;
	private double quantity;
	private float total;
	private float amount;
	private float taxamount;
	private float price;
	private String type;
	private Product product;
	private TaxRate taxrate;
	private Head head;
	private UnitMeasure um;
	private String serialnumber;
	private String expiredate;
	private boolean generate;
	private double necks;
	public double getNecks() {
		return necks;
	}
	public void setNecks(double necks) {
		this.necks = necks;
	}
	public int getIdRow() {
		return idRow;
	}
	public void setIdRow(int idRow) {
		this.idRow = idRow;
	}
	public String getProductcode() {
		return productcode;
	}
	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}
	public String getProductdescription() {
		return productdescription;
	}
	public void setProductdescription(String productdescription) {
		this.productdescription = productdescription;
	}
	public String getProductum() {
		return productum;
	}
	public void setProductum(String productum) {
		this.productum = productum;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public float getTotal() {
		return total;
	}
	public void setTotal(float total) {
		this.total = total;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public float getTaxamount() {
		return taxamount;
	}
	public void setTaxamount(float taxamount) {
		this.taxamount = taxamount;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public TaxRate getTaxrate() {
		return taxrate;
	}
	public void setTaxrate(TaxRate taxrate) {
		this.taxrate = taxrate;
	}
	public Head getHead() {
		return head;
	}
	public void setHead(Head head) {
		this.head = head;
	}
	public String getSerialnumber() {
		return serialnumber;
	}
	public void setSerialnumber(String serialcode) {
		this.serialnumber = serialcode;
	}
	public String getExpiredate() {
		return expiredate;
	}
	public void setExpiredate(String expiredate) {
		this.expiredate = expiredate;
	}
	public boolean isGenerate() {
		return generate;
	}
	public void setGenerate(boolean generate) {
		this.generate = generate;
	}
	public UnitMeasure getUm() {
		return um;
	}
	public void setUm(UnitMeasure um) {
		this.um = um;
	}
	public void convertFromTable(Itbl obj){
		TblRow h = (TblRow)obj;
		this.amount = h.getAmount();
		this.idRow = h.getIdRow();
		this.price = h.getPrice();
		this.productcode = h.getProductcode();
		this.productdescription = h.getProductdescription();
		this.productum = h.getProductum();
		this.quantity = h.getQuantity();
		this.taxamount = h.getTaxamount();
		this.total = h.getTotal();
		this.type = h.getType();
		this.serialnumber = h.getSerialnumber();
		this.expiredate = DataUtilConverter.convertStringFromDate(h.getExpiredate());
		this.necks = h.getNecks();
		if (h.getProduct()!= null){
			this.product = new Product();
			this.product.convertFromTable(h.getProduct());
		}
		if (h.getTaxrate()!= null){
			this.taxrate = new TaxRate();
			this.taxrate.convertFromTable(h.getTaxrate());
		}
		if (h.getUm() != null){
			this.um = new UnitMeasure();
			this.um.convertFromTable(h.getUm());
		}
	}
	public void copy(Row h){
		this.idRow = h.getIdRow();
		this.productcode = h.getProductcode();
		this.productdescription = h.getProductdescription();
		this.productum = h.getProductum();
		this.quantity = h.getQuantity();
		this.taxamount = h.getTaxamount();
		this.total = h.getTotal();
		this.type = h.getType();
		this.serialnumber = h.getSerialnumber();
		this.expiredate = h.getExpiredate();
		this.necks = h.getNecks();
		if (h.getProduct()!= null){
			this.product = h.getProduct();
		}
		if (h.getTaxrate()!= null){
			this.taxrate = h.getTaxrate();
		}
		if (h.getUm() != null){
			this.um = h.getUm();
		}
	}
	public GECOObject control(){
		return null;
	}
}
