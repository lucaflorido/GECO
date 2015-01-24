package geco.print;




import java.util.TreeSet;



public class PrintReportOrder implements Comparable<PrintReportOrder>{
	private String fornitore;
	private String codice_prodotto;
	private String descrizione_prodotto;
	private String unitamisura_prodotto;
	private TreeSet<PrintReportOrderSubreport> customers;
	public String getFornitore() {
		return fornitore;
	}
	public void setFornitore(String fornitore) {
		this.fornitore = fornitore;
	}
	public String getCodice_prodotto() {
		return codice_prodotto;
	}
	public void setCodice_prodotto(String codice_prodotto) {
		this.codice_prodotto = codice_prodotto;
	}
	public String getDescrizione_prodotto() {
		return descrizione_prodotto;
	}
	public void setDescrizione_prodotto(String descrizione_prodotto) {
		this.descrizione_prodotto = descrizione_prodotto;
	}
	public String getUnitamisura_prodotto() {
		return unitamisura_prodotto;
	}
	public void setUnitamisura_prodotto(String unitamisura_prodotto) {
		this.unitamisura_prodotto = unitamisura_prodotto;
	}
	public TreeSet<PrintReportOrderSubreport> getCustomers() {
		return customers;
	}
	public void setCustomers(TreeSet<PrintReportOrderSubreport> customers) {
		this.customers = customers;
	}
	@Override
	public int compareTo(PrintReportOrder p){
		return this.codice_prodotto.compareTo(p.getCodice_prodotto());
	}
	
}
