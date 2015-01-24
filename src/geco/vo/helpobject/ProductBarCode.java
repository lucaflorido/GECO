package geco.vo.helpobject;

import geco.vo.Product;

public class ProductBarCode {
	private Product product;
	private String code;
	private String barcode;
	private int barCodeWeight;
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public ProductBarCode(){
		
	}
	public ProductBarCode(Product product,String code,int barCodeWeight,String barcode){
		this.product = product;
		this.code = code;
		this.barCodeWeight = barCodeWeight;
		this.barcode = barcode;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getBarCodeWeight() {
		return barCodeWeight;
	}
	public void setBarCodeWeight(int barCodeWeight) {
		this.barCodeWeight = barCodeWeight;
	}
	public double getWeight(){
		double weight = 0;
		if (this.barCodeWeight > 0){
			String bcwc = this.barcode;
			if (bcwc.length() > this.barCodeWeight){
				int l = bcwc.length();
				l = l-this.barCodeWeight;
				String weightStr = this.barcode.substring(l-1, this.barcode.length()-1);
				if (this.barCodeWeight > 3){
					int decSep = weightStr.length() - 3;
					String decimal = weightStr.substring(decSep,weightStr.length());
					String integer = weightStr.substring(0,decSep);
					String di = integer+"."+decimal;
					weight = Double.parseDouble(di);
				}
			}
		}
		return weight;
	}
}
