package geco.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import geco.vo.Ivo;
import geco.vo.PaidSuspended;
@Entity
@Table(name="tblPaidSuspended")
public class TblPaidSuspended implements Itbl {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="idPaidSuspended")
	private int idPaidSuspended;
	@ManyToOne
	@JoinColumn(name = "idPaid")
	private TblPaid paid;
	@ManyToOne
	@JoinColumn(name = "idSuspended")
	private TblSuspended suspended;
	@Column(name="amount")
	private double amount;
	public int getIdPaidSuspended() {
		return idPaidSuspended;
	}
	public void setIdPaidSuspended(int idPaidSuspended) {
		this.idPaidSuspended = idPaidSuspended;
	}
	public TblPaid getPaid() {
		return paid;
	}
	public void setPaid(TblPaid paid) {
		this.paid = paid;
	}
	public TblSuspended getSuspended() {
		return suspended;
	}
	public void setSuspended(TblSuspended suspended) {
		this.suspended = suspended;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public void convertToTable(Ivo obj){
		PaidSuspended ps = (PaidSuspended)obj;
		this.amount = ps.getAmount();
		this.idPaidSuspended = ps.getIdPaidSuspended();
		if (ps.getSuspended() != null){
			this.suspended = new TblSuspended();
			this.suspended.convertToTable(ps.getSuspended());
		}
	}
}
