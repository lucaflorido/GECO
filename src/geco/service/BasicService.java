package geco.service;

import geco.dao.BasicDao;
import geco.vo.Brand;
import geco.vo.CategoryCustomer;
import geco.vo.CategoryProduct;
import geco.vo.CategorySupplier;
import geco.vo.Counter;
import geco.vo.Document;
import geco.vo.GroupCustomer;
import geco.vo.GroupProduct;
import geco.vo.GroupSupplier;
import geco.vo.Payment;
import geco.vo.Report;
import geco.vo.StoreMovement;
import geco.vo.TaxRate;
import geco.vo.UnitMeasure;




import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

@Path("basic")
public class BasicService {
	@GET
	@Path("taxrate")
	@Produces(MediaType.TEXT_PLAIN)
	public String getTaxrateList(){
		Gson gson = new Gson();
		BasicDao taxratedao = new BasicDao();
		return gson.toJson(taxratedao.getTaxRateList());
	}
	@PUT
	@Path("taxrate")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	public String saveUser(@FormParam("taxrates") String taxrates){
	  Gson gson = new Gson();
	  TaxRate[] taxratesarray = gson.fromJson(taxrates,TaxRate[].class);
	  BasicDao taxratedao = new BasicDao();
	  return gson.toJson(taxratedao.saveUpdatesTaxrate(taxratesarray));
	}
	/***
	Delete user 
   */

	  @DELETE
	  @Path("taxrate")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String deleteTaxRate(@FormParam("taxrateobj") String taxrateobj){
		  Gson gson = new Gson();
		  try{
			  TaxRate taxrate = gson.fromJson(taxrateobj,TaxRate.class);
			  BasicDao taxratedao = new BasicDao();
			  taxratedao.deleteTaxRate(taxrate);
			  return gson.toJson(true);
		  }catch(Exception e){
			  return gson.toJson("");
		  }
	  }
	  /*****
	   * 
	   * Unit Measure
	   * @return
	   */
	  @GET
	  @Path("unitmeasure")
 	  @Produces(MediaType.TEXT_PLAIN)
	  public String getUnitMeasureList(){
		Gson gson = new Gson();
		BasicDao dao = new BasicDao();
		return gson.toJson(dao.getUnitMeasureList());
	  }
	  @PUT
		@Path("unitmeasure")
	    @Produces(MediaType.TEXT_PLAIN)
	    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
		public String saveUM(@FormParam("ums") String ums){
		  Gson gson = new Gson();
		  UnitMeasure[] umsarray = gson.fromJson(ums,UnitMeasure[].class);
		  BasicDao dao = new BasicDao();
		  
		  return gson.toJson(dao.saveUpdatesUnitMeasure(umsarray));
		}
		/***
		Delete user 
	   */

		  @DELETE
		  @Path("unitmeasure")
		  @Produces(MediaType.TEXT_PLAIN)
		  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
		  public String deleteUm(@FormParam("umobj") String umobj){
			  Gson gson = new Gson();
			  try{
				  UnitMeasure um = gson.fromJson(umobj,UnitMeasure.class);
				  BasicDao dao = new BasicDao();
				  dao.deleteUM(um);
				  return gson.toJson(true);
			  }catch(Exception e){
				  return gson.toJson("");
			  }
		  }

	  /*****
	   * 
	   * Store Movement
	   * @return
	   */
	  @GET
	  @Path("storemovement")
 	  @Produces(MediaType.TEXT_PLAIN)
	  public String getStoreMovementList(){
		Gson gson = new Gson();
		BasicDao dao = new BasicDao();
		return gson.toJson(dao.getStoreMovementList());
	  }
	  @PUT
		@Path("storemovement")
	    @Produces(MediaType.TEXT_PLAIN)
	    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
		public String saveSM(@FormParam("sms") String sms){
		  Gson gson = new Gson();
		  StoreMovement[] smsarray = gson.fromJson(sms,StoreMovement[].class);
		  BasicDao dao = new BasicDao();
		  return gson.toJson(dao.saveUpdatesStoreMovement(smsarray));
		}
		/***
		Delete user 
	   */

		  @DELETE
		  @Path("storemovement")
		  @Produces(MediaType.TEXT_PLAIN)
		  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
		  public String deleteSM(@FormParam("smobj") String smobj){
			  Gson gson = new Gson();
			  try{
				  StoreMovement sm = gson.fromJson(smobj,StoreMovement.class);
				  BasicDao dao = new BasicDao();
				  dao.deleteSM(sm);
				  return gson.toJson(true);
			  }catch(Exception e){
				  return gson.toJson("");
			  }
		  }

	  /*****
	   * 
	   * Counter
	   * @return
	   */
	  @GET
	  @Path("counter")
 	  @Produces(MediaType.TEXT_PLAIN)
	  public String getCounterList(){
		Gson gson = new Gson();
		BasicDao dao = new BasicDao();
		return gson.toJson(dao.getCounterList());
	  }
	  @PUT
		@Path("counter")
	    @Produces(MediaType.TEXT_PLAIN)
	    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
		public String saveCounter(@FormParam("counters") String counters){
		  Gson gson = new Gson();
		  Counter[] smsarray = gson.fromJson(counters,Counter[].class);
		  BasicDao dao = new BasicDao();
		  return gson.toJson(dao.saveUpdatesCounter(smsarray));
		}
		/***
		Delete user 
	   */

		  @DELETE
		  @Path("counter")
		  @Produces(MediaType.TEXT_PLAIN)
		  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
		  public String deleteCounter(@FormParam("counterobj") String counterobj){
			  Gson gson = new Gson();
			  try{
				  Counter sm = gson.fromJson(counterobj,Counter.class);
				  BasicDao dao = new BasicDao();
				  dao.deleteCounter(sm);
				  return gson.toJson(true);
			  }catch(Exception e){
				  return gson.toJson("");
			  }
		  }

	  /*****
	   * 
	   * Payment
	   * @return
	   */
	  @GET
	  @Path("payment")
 	  @Produces(MediaType.TEXT_PLAIN)
	  public String getPaymentList(){
		Gson gson = new Gson();
		BasicDao dao = new BasicDao();
		return gson.toJson(dao.getPaymentList());
	  }
	  @PUT
	  @Path("payment")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String savePayment(@FormParam("payments") String payments){
		  Gson gson = new Gson();
		  Payment[] smsarray = gson.fromJson(payments,Payment[].class);
		  BasicDao dao = new BasicDao();
		  return gson.toJson(dao.saveUpdatesPayment(smsarray));
	  }
		/***
		Delete user 
	   */

	  @DELETE
	  @Path("payment")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String deletePayment(@FormParam("paymentobj") String paymentobj){
		  Gson gson = new Gson();
		  try{
			  Payment sm = gson.fromJson(paymentobj,Payment.class);
			  BasicDao dao = new BasicDao();
			  dao.deletePayment(sm);
			  return gson.toJson(true);
		  }catch(Exception e){
			  return gson.toJson("");
		  }
	  }
	  /*****
	   * 
	   * Document
	   * @return
	   */
	  @GET
	  @Path("document")
 	  @Produces(MediaType.TEXT_PLAIN)
	  public String getDocumentList(){
		Gson gson = new Gson();
		BasicDao dao = new BasicDao();
		return gson.toJson(dao.getDocumentList());
	  }
	  @PUT
	  @Path("document")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String saveDocument(@FormParam("documents") String documents){
		  Gson gson = new Gson();
		  Document[] smsarray = gson.fromJson(documents,Document[].class);
		  BasicDao dao = new BasicDao();
		  return gson.toJson(dao.saveUpdatesDocument(smsarray));
	  }
		/***
		Delete user 
	   */

	  @DELETE
	  @Path("document")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String deleteDocument(@FormParam("documentobj") String documentobj){
		  Gson gson = new Gson();
		  try{
			  Document sm = gson.fromJson(documentobj,Document.class);
			  BasicDao dao = new BasicDao();
			  dao.deleteDocument(sm);
			  return gson.toJson(true);
		  }catch(Exception e){
			  return gson.toJson("");
		  }
	  }
	  
	  
	  /*****
	   * 
	   * GroupProduct
	   * @return
	   */
	  @GET
	  @Path("groupproduct")
 	  @Produces(MediaType.TEXT_PLAIN)
	  public String getGroupProductList(){
		Gson gson = new Gson();
		BasicDao dao = new BasicDao();
		return gson.toJson(dao.getGroupProductList());
	  }
	  @GET
	  @Path("groupproduct/prompt")
 	  @Produces(MediaType.TEXT_PLAIN)
	  public String getGroupProductListPromt(){
		Gson gson = new Gson();
		BasicDao dao = new BasicDao();
		return gson.toJson(dao.getGroupProductList(true));
	  }
	  @PUT
	  @Path("groupproduct")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String saveGroupProduct(@FormParam("groupproducts") String groupproducts){
		  Gson gson = new Gson();
		  GroupProduct[] smsarray = gson.fromJson(groupproducts,GroupProduct[].class);
		  BasicDao dao = new BasicDao();
		  return gson.toJson(dao.saveUpdatesGroupProduct(smsarray));
	  }
		/***
		Delete user 
	   */

	  @DELETE
	  @Path("groupproduct")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String deleteGroupProduct(@FormParam("groupproductobj") String groupproductobj){
		  Gson gson = new Gson();
		  try{
			  GroupProduct sm = gson.fromJson(groupproductobj,GroupProduct.class);
			  BasicDao dao = new BasicDao();
			  dao.deleteGroupProduct(sm);
			  return gson.toJson(true);
		  }catch(Exception e){
			  return gson.toJson("");
		  }
	  }
	  
	  /*****
	   * 
	   * CategoryProduct
	   * @return
	   */
	  @GET
	  @Path("categoryproduct")
 	  @Produces(MediaType.TEXT_PLAIN)
	  public String getCategoryProductList(){
		Gson gson = new Gson();
		BasicDao dao = new BasicDao();
		return gson.toJson(dao.getCategoryProductList());
	  }
	  @PUT
	  @Path("categoryproduct")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String saveCategoryProduct(@FormParam("categoryproducts") String categoryproducts){
		  Gson gson = new Gson();
		  CategoryProduct[] smsarray = gson.fromJson(categoryproducts,CategoryProduct[].class);
		  BasicDao dao = new BasicDao();
		  return gson.toJson(dao.saveUpdatesCategoryProduct(smsarray));
	  }
		/***
		Delete user 
	   */

	  @DELETE
	  @Path("categoryproduct")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String deleteCategoryProduct(@FormParam("categoryproductobj") String categoryproductobj){
		  Gson gson = new Gson();
		  try{
			  CategoryProduct sm = gson.fromJson(categoryproductobj,CategoryProduct.class);
			  BasicDao dao = new BasicDao();
			  dao.deleteCategoryProduct(sm);
			  return gson.toJson(true);
		  }catch(Exception e){
			  return gson.toJson("");
		  }
	  }
	  
	  @DELETE
	  @Path("subcategoryproduct/{idsubcategory}")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String deleteSubCategoryProduct(@PathParam("idsubcategory") int idsubcategory){
		  Gson gson = new Gson();
		  try{
			 BasicDao dao = new BasicDao();
			 return gson.toJson(dao.deleteSubCategoryProduct(idsubcategory));
		  }catch(Exception e){
			  return gson.toJson("");
		  }
	  }
	  
	  /*****
	   * 
	   * CategoryProduct
	   * @return
	   */
	  @GET
	  @Path("categorycustomer")
 	  @Produces(MediaType.TEXT_PLAIN)
	  public String getCategoryCustomerList(){
		Gson gson = new Gson();
		BasicDao dao = new BasicDao();
		return gson.toJson(dao.getCategoryCustomerList());
	  }
	  @PUT
	  @Path("categorycustomer")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String saveCategoryCustomer(@FormParam("categorycustomers") String categorycustomers){
		  Gson gson = new Gson();
		  CategoryCustomer[] smsarray = gson.fromJson(categorycustomers,CategoryCustomer[].class);
		  BasicDao dao = new BasicDao();
		  return gson.toJson(dao.saveUpdatesCategoryCustomer(smsarray));
	  }
		/***
		Delete user 
	   */

	  @DELETE
	  @Path("categorycustomer")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String deleteCategoryCustomer(@FormParam("categorycustomerobj") String categorycustomerobj){
		  Gson gson = new Gson();
		  try{
			  CategoryCustomer sm = gson.fromJson(categorycustomerobj,CategoryCustomer.class);
			  BasicDao dao = new BasicDao();
			  dao.deleteCategoryCustomer(sm);
			  return gson.toJson(true);
		  }catch(Exception e){
			  return gson.toJson("");
		  }
	  }
	  
	  
	  
	  /*****
	   * 
	   * CategoryProduct
	   * @return
	   */
	  @GET
	  @Path("groupcustomer")
 	  @Produces(MediaType.TEXT_PLAIN)
	  public String getGroupCustomerList(){
		Gson gson = new Gson();
		BasicDao dao = new BasicDao();
		return gson.toJson(dao.getGroupCustomerList());
	  }
	  @PUT
	  @Path("groupcustomer")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String saveGroupCustomer(@FormParam("groupcustomers") String groupcustomers){
		  Gson gson = new Gson();
		  GroupCustomer[] smsarray = gson.fromJson(groupcustomers,GroupCustomer[].class);
		  BasicDao dao = new BasicDao();
		  return gson.toJson(dao.saveUpdatesGroupCustomer(smsarray));
	  }
		/***
		Delete user 
	   */

	  @DELETE
	  @Path("groupcustomer")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String deleteGroupCustomer(@FormParam("groupcustomerobj") String groupcustomerobj){
		  Gson gson = new Gson();
		  try{
			  GroupCustomer sm = gson.fromJson(groupcustomerobj,GroupCustomer.class);
			  BasicDao dao = new BasicDao();
			  dao.deleteGroupCustomer(sm);
			  return gson.toJson(true);
		  }catch(Exception e){
			  return gson.toJson("");
		  }
	  }
	  
	  /*****
	   * 
	   * CategoryProduct
	   * @return
	   */
	  @GET
	  @Path("categorysupplier")
 	  @Produces(MediaType.TEXT_PLAIN)
	  public String getCategorySupplierList(){
		Gson gson = new Gson();
		BasicDao dao = new BasicDao();
		return gson.toJson(dao.getCategorySupplierList());
	  }
	  @PUT
	  @Path("categorysupplier")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String saveCategorySupplier(@FormParam("categorysuppliers") String categorysuppliers){
		  Gson gson = new Gson();
		  CategorySupplier[] smsarray = gson.fromJson(categorysuppliers,CategorySupplier[].class);
		  BasicDao dao = new BasicDao();
		  return gson.toJson(dao.saveUpdatesCategorySupplier(smsarray));
	  }
		/***
		Delete user 
	   */

	  @DELETE
	  @Path("categorysupplier")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String deleteCategorySupplier(@FormParam("categorysupplierobj") String categorysupplierobj){
		  Gson gson = new Gson();
		  try{
			  CategorySupplier sm = gson.fromJson(categorysupplierobj,CategorySupplier.class);
			  BasicDao dao = new BasicDao();
			  dao.deleteCategorySupplier(sm);
			  return gson.toJson(true);
		  }catch(Exception e){
			  return gson.toJson("");
		  }
	  }
	  
	  
	  
	  /*****
	   * 
	   * CategoryProduct
	   * @return
	   */
	  @GET
	  @Path("groupsupplier")
 	  @Produces(MediaType.TEXT_PLAIN)
	  public String getGroupSupplierList(){
		Gson gson = new Gson();
		BasicDao dao = new BasicDao();
		return gson.toJson(dao.getGroupSupplierList());
	  }
	  @PUT
	  @Path("groupsupplier")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String saveGroupSupplier(@FormParam("groupsuppliers") String groupsuppliers){
		  Gson gson = new Gson();
		  GroupSupplier[] smsarray = gson.fromJson(groupsuppliers,GroupSupplier[].class);
		  BasicDao dao = new BasicDao();
		  return gson.toJson(dao.saveUpdatesGroupSupplier(smsarray));
	  }
		/***
		Delete user 
	   */

	  @DELETE
	  @Path("groupsupplier")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String deleteGroupSupplier(@FormParam("groupsupplierobj") String groupsupplierobj){
		  Gson gson = new Gson();
		  try{
			  GroupSupplier sm = gson.fromJson(groupsupplierobj,GroupSupplier.class);
			  BasicDao dao = new BasicDao();
			  dao.deleteGroupSupplier(sm);
			  return gson.toJson(true);
		  }catch(Exception e){
			  return gson.toJson("");
		  }
	  }
	  
	  
	  
	  /*****
	   * 
	   * CategoryProduct
	   * @return
	   */
	  @GET
	  @Path("brand")
 	  @Produces(MediaType.TEXT_PLAIN)
	  public String getBrandList(){
		Gson gson = new Gson();
		BasicDao dao = new BasicDao();
		return gson.toJson(dao.getBrandList());
	  }
	  @PUT
	  @Path("brand")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String saveBrand(@FormParam("brands") String brands){
		  Gson gson = new Gson();
		  Brand[] smsarray = gson.fromJson(brands,Brand[].class);
		  BasicDao dao = new BasicDao();
		  return gson.toJson(dao.saveUpdatesBrand(smsarray));
	  }
		/***
		Delete user 
	   */

	  @DELETE
	  @Path("brand")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String deleteBrand(@FormParam("brandobj") String brandobj){
		  Gson gson = new Gson();
		  try{
			  Brand sm = gson.fromJson(brandobj,Brand.class);
			  BasicDao dao = new BasicDao();
			  dao.deleteBrand(sm);
			  return gson.toJson(true);
		  }catch(Exception e){
			  return gson.toJson("");
		  }
	  }
	  @GET
		@Path("report")
		@Produces(MediaType.TEXT_PLAIN)
		public String getReportList(){
			Gson gson = new Gson();
			BasicDao basicdao = new BasicDao();
			return gson.toJson(basicdao.getReportList());
		}
		
		/***
		Delete user 
	   */

		  @DELETE
		  @Path("report")
		  @Produces(MediaType.TEXT_PLAIN)
		  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
		  public String deleteReport(@FormParam("reportobj") String reportobj){
			  Gson gson = new Gson();
			  try{
				  Report report = gson.fromJson(reportobj,Report.class);
				  BasicDao basicdao = new BasicDao();
				  return gson.toJson(basicdao.deleteReport(report));
			  }catch(Exception e){
				  return gson.toJson("");
			  }
		  }
}
