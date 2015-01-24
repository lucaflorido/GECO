package geco.vo.filter.product;

import geco.pojo.TblBrand;
import geco.pojo.TblCategoryProduct;
import geco.pojo.TblGroupProduct;
import geco.pojo.TblSubCategoryProduct;
import geco.pojo.TblSupplier;
import geco.vo.filter.PagesFilter;
import geco.vo.helpobject.ProductBasicPricesCalculation;

public class SelectProductsFilter {
	private TblBrand brand;
	private TblCategoryProduct category;
	private TblGroupProduct group;
	private TblSupplier supplier;
	private TblSubCategoryProduct subcategory;
	private float increment;
	private boolean isPercentage;
	private String searchstring;
	public String getSearchstring() {
		return searchstring;
	}
	public void setSearchstring(String searchstring) {
		this.searchstring = searchstring;
	}
	private PagesFilter pagefilter;
	
	public PagesFilter getPagefilter() {
		return pagefilter;
	}
	public void setPagefilter(PagesFilter pagefilter) {
		this.pagefilter = pagefilter;
	}
	public TblSupplier getSupplier() {
		return supplier;
	}
	public void setSupplier(TblSupplier supplier) {
		this.supplier = supplier;
	}
	public TblBrand getBrand() {
		return brand;
	}
	public void setBrand(TblBrand brand) {
		this.brand = brand;
	}
	public TblCategoryProduct getCategory() {
		return category;
	}
	public void setCategory(TblCategoryProduct category) {
		this.category = category;
	}
	
	public TblSubCategoryProduct getSubcategory() {
		return subcategory;
	}
	public void setSubcategory(TblSubCategoryProduct subcategory) {
		this.subcategory = subcategory;
	}
	public TblGroupProduct getGroup() {
		return group;
	}
	public void setGroup(TblGroupProduct group) {
		this.group = group;
	}
	public float getIncrement() {
		return increment;
	}
	public void setIncrement(float increment) {
		this.increment = increment;
	}
	public boolean isPercentage() {
		return isPercentage;
	}
	public void setPercentage(boolean isPercentage) {
		this.isPercentage = isPercentage;
	}
	
}
