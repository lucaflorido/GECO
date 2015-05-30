package geco.print;

import geco.vo.Company;
import geco.vo.Product;
import geco.vo.Storage;

public class PrintProductStorage extends PrintCompany{
	public PrintProductStorage(){
		super();
	}
	public String prodotto_carico ="";
	public String prodotto_codice= "";
	public String prodotto_desc = "";
	public String prodotto_um= "";
	public String prodotto_scarico= "";
	public String prodotto_giacenza;
	
	public String getProdotto_carico() {
		return prodotto_carico;
	}
	public void setProdotto_carico(String prodotto_carico) {
		this.prodotto_carico = prodotto_carico;
	}
	public String getProdotto_codice() {
		return prodotto_codice;
	}
	public void setProdotto_codice(String prodotto_codice) {
		this.prodotto_codice = prodotto_codice;
	}
	public String getProdotto_desc() {
		return prodotto_desc;
	}
	public void setProdotto_desc(String prodotto_desc) {
		this.prodotto_desc = prodotto_desc;
	}
	public String getProdotto_um() {
		return prodotto_um;
	}
	public void setProdotto_um(String prodotto_um) {
		this.prodotto_um = prodotto_um;
	}
	public String getProdotto_scarico() {
		return prodotto_scarico;
	}
	public void setProdotto_scarico(String prodotto_scarico) {
		this.prodotto_scarico = prodotto_scarico;
	}
	
	public String getProdotto_giacenza() {
		return prodotto_giacenza;
	}
	public void setProdotto_giacenza(String prodotto_giacenza) {
		this.prodotto_giacenza = prodotto_giacenza;
	}
	public void setFromObject(Company comp,Storage prod){
		super.setFromObject(comp);
		setFromObject(prod);
	}
	
	public void setFromObject(Storage prod){
		this.prodotto_carico =String.valueOf(prod.getLoad());
		this.prodotto_codice = prod.getProduct().getCode();
		this.prodotto_desc = prod.getProduct().getDescription();
		this.prodotto_scarico = String.valueOf(prod.getUnload());
		this.prodotto_um = prod.getProduct().getUmselected().getCode();
		this.prodotto_giacenza = String.valueOf(prod.getStock());
	}

}
