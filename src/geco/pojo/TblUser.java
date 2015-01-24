package geco.pojo;

import geco.vo.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



@Entity
@Table(name="tblUser")
public class TblUser {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="idUser")
	private int iduser;
	@Column(name="username")
	private String username;
	@Column(name="password")
	private String password;
	@Column(name="name")
	private String name;
	@Column(name="surname")
	private String surname;
	@Column(name="phone")
	private String phone;
	@Column(name="mobile")
	private String mobile;
	@Column(name="email")
	private String email;
	@ManyToOne
	@JoinColumn(name="idRole")
	private TblRole role;
	@Column(name="ACTIVE")
	private Boolean active;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public TblRole getRole() {
		return role;
	}
	public void setRole(TblRole role) {
		this.role = role;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public int getIduser() {
		return iduser;
	}
	public void setIduser(int idtbluser) {
		this.iduser = idtbluser;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String tblusercol) {
		this.username = tblusercol;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void convertToTable(User user){
		this.setIduser(user.get_iduser());
		this.setUsername(user.getUsername());
		this.setPassword(user.getPassword());
		this.setName(user.getName());
		this.setSurname(user.getSurname());
		this.setPhone(user.getPhone());
		this.setMobile(user.getMobile());
		this.setEmail(user.getEmail());
		this.setActive(user.getActive());
		TblRole roleTo = new TblRole();
		roleTo.convertToTable(user.getRole());
		this.setRole(roleTo);
	}
}
