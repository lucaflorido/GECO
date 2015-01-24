package geco.vo.filter.customer;

import geco.vo.CategoryCustomer;
import geco.vo.GroupCustomer;
import geco.vo.filter.PagesFilter;

public class SelectCustomerList {
	private String searchstring;
	private GroupCustomer group;
	private CategoryCustomer category;
	private PagesFilter pagefilter;
	public String getSearchstring() {
		return searchstring;
	}
	public void setSearchstring(String searchstring) {
		this.searchstring = searchstring;
	}
	public GroupCustomer getGroup() {
		return group;
	}
	public void setGroup(GroupCustomer group) {
		this.group = group;
	}
	public CategoryCustomer getCategory() {
		return category;
	}
	public void setCategory(CategoryCustomer category) {
		this.category = category;
	}
	public PagesFilter getPagefilter() {
		return pagefilter;
	}
	public void setPagefilter(PagesFilter pagefilter) {
		this.pagefilter = pagefilter;
	}
	
}
