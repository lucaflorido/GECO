package geco.print;

public class PrintReportOrderSubreport implements Comparable<PrintReportOrderSubreport> {
	private String cliente;
	private String quantita;
	private String um;
	private String totale;
	public String getTotale() {
		return totale;
	}
	public void setTotale(String totale) {
		this.totale = totale;
	}
	public String getUm() {
		return um;
	}
	public void setUm(String um) {
		this.um = um;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getQuantita() {
		return quantita;
	}
	public void setQuantita(String quantita) {
		this.quantita = quantita;
	}
	@Override
	public int compareTo(PrintReportOrderSubreport p){
		return this.cliente.compareTo(p.getCliente());
	}
}
