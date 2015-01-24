package geco.vo.filter;

import geco.vo.Brand;
import geco.vo.CategoryProduct;
import geco.vo.Customer;
import geco.vo.Destination;
import geco.vo.Document;
import geco.vo.GroupProduct;
import geco.vo.SubCategoryProduct;
import geco.vo.Supplier;
import geco.vo.Transporter;

public class StoreFilter {
	private Brand brand;
	private GroupProduct group;
	private CategoryProduct category;
	private SubCategoryProduct subcategory;
	private String searchString;
	private String fromDate;
	private String toDate;
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	public GroupProduct getGroup() {
		return group;
	}
	public void setGroup(GroupProduct group) {
		this.group = group;
	}
	public CategoryProduct getCategory() {
		return category;
	}
	public void setCategory(CategoryProduct category) {
		this.category = category;
	}
	public SubCategoryProduct getSubcategory() {
		return subcategory;
	}
	public void setSubcategory(SubCategoryProduct subcategory) {
		this.subcategory = subcategory;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	
}
