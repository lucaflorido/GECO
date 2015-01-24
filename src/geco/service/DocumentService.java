package geco.service;

import java.util.Arrays;
import java.util.HashSet;

import geco.dao.DocumentDao;
import geco.exception.GecoException;
import geco.vo.GECOError;
import geco.vo.GECOObject;
import geco.vo.GenerateDocsObject;
import geco.vo.GenerateObject;
import geco.vo.Head;
import geco.vo.Row;
import geco.vo.filter.GenerateDocsFilter;
import geco.vo.filter.HeadFilter;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
@Path("head")
public class DocumentService {
	/*****
	   * 
	   * Product
	   * @return
	   */
	  @POST
	  @Path("pages/{size}")
	  @Produces(MediaType.TEXT_HTML)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	  public String pages(@PathParam("size") int size,@FormParam("filter") String filter) {
		Gson gson = new Gson();
		DocumentDao dao = new DocumentDao();
		HeadFilter filterDoc = gson.fromJson(filter,HeadFilter.class);
		return gson.toJson(dao.getPagesNumber(size,filterDoc));
	  }
	  @POST
	  @Path("head")
	  @Produces(MediaType.TEXT_HTML)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String getHeadList(@FormParam("filter") String filter){
		Gson gson = new Gson();
		DocumentDao dao = new DocumentDao();
		HeadFilter filterDoc = gson.fromJson(filter,HeadFilter.class);
		return gson.toJson(dao.getHeadList(filterDoc));
	  }
	  /***
		Get Single user
  */
	  @GET
	  @Path("head/{idhead}")
	  @Produces(MediaType.TEXT_HTML)
	  public String singleHead(@PathParam("idhead") int id) {
		Gson gson = new Gson();
		DocumentDao dao = new DocumentDao();
		Head head = new Head();
		head = dao.getSingleHead(id);
		return gson.toJson(head);
	  }
	  @PUT
	  @Path("head")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String saveHead(@FormParam("heads") String head){
		  Gson gson = new Gson();
		  Head sms = gson.fromJson(head,Head.class);
		  DocumentDao dao = new DocumentDao();
		  GECOObject ge = null;
		  try{
			   ge = dao.saveUpdatesHead(sms);
		  }catch(GecoException e){
			  return gson.toJson(e);
		  }
		  return gson.toJson(ge);
	  }
		/***
		Delete user 
	   */
	  @DELETE
	  @Path("head")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String deleteHead(@FormParam("headobj") String headobj){
		  Gson gson = new Gson();
		  try{
			  Head sm = gson.fromJson(headobj,Head.class);
			  DocumentDao dao = new DocumentDao();
			  return gson.toJson(dao.deleteHead(sm));
		  }catch(Exception e){
			  return gson.toJson(new GECOError("DELETE", "Errore nell'eliminazione dei dati"));
		  }
	  }
	  @POST
	  @Path("generationdocs/filterdocs")
	  @Produces(MediaType.TEXT_HTML)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String singleHead(@FormParam("filter") String filter) {
		Gson gson = new Gson();
		DocumentDao dao = new DocumentDao();
		GenerateDocsFilter filterDoc = gson.fromJson(filter,GenerateDocsFilter.class);
		return gson.toJson(dao.getHeadRowsGenerateList(filterDoc));
	  }
	  @POST
	  @Path("generationdocs/objectdocs")
	  @Produces(MediaType.TEXT_HTML)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String generateHeads(@FormParam("generateobj") String generateobj) {
		Gson gson = new Gson();
		DocumentDao dao = new DocumentDao();
		GenerateDocsObject generateDoc = gson.fromJson(generateobj,GenerateDocsObject.class);
		return gson.toJson(dao.createHeadRowsGenerateList(generateDoc));
	  }
	  @POST
	  @Path("copyrows/objectdocs")
	  @Produces(MediaType.TEXT_HTML)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String copyRows(@FormParam("generateobj") String generateobj) {
		Gson gson = new Gson();
		DocumentDao dao = new DocumentDao();
		GenerateDocsObject generateDoc = gson.fromJson(generateobj,GenerateDocsObject.class);
		return gson.toJson(dao.copyHeadRows(generateDoc));
	  }
	  @POST
	  @Path("createorders")
	  @Produces(MediaType.TEXT_HTML)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String createDailyOrders(@FormParam("orders") String generateobj) {
		Gson gson = new Gson();
		DocumentDao dao = new DocumentDao();
		GenerateObject generateDoc = gson.fromJson(generateobj,GenerateObject.class);
		return gson.toJson(dao.createDailyOrder(generateDoc));
	  }
	  @POST
	  @Path("fillserialnumber/{idhead}")
	  @Produces(MediaType.TEXT_HTML)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String fillSerialNumber(@PathParam("idhead") int idhead) {
		Gson gson = new Gson();
		DocumentDao dao = new DocumentDao();
		return gson.toJson(dao.fillSerialNumbers(idhead));
	  }
	  @POST
	  @Path("serialnumberList/{idhead}")
	  @Produces(MediaType.TEXT_HTML)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String getSerialNumberRows(@PathParam("idhead") int idhead) {
		Gson gson = new Gson();
		DocumentDao dao = new DocumentDao();
		return gson.toJson(dao.getListOfSerialNumbers(idhead));
	  }
	  @POST
	  @Path("copyrowserialnumber/")
	  @Produces(MediaType.TEXT_HTML)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String copyRow(@FormParam("row") String rowStr) {
		Gson gson = new Gson();
		DocumentDao dao = new DocumentDao();
		Row r = gson.fromJson(rowStr, Row.class);
		return gson.toJson(dao.copyRowSerialNumber(r));
	  }
	  @PUT
	  @Path("saverowsserialnumber/{idhead}")
	  @Produces(MediaType.TEXT_HTML)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String saveRowsSerialNumber(@FormParam("row") String rowStr,@PathParam("idhead") int idhead) {
		Gson gson = new Gson();
		DocumentDao dao = new DocumentDao();
		Row[] r = gson.fromJson(rowStr, Row[].class);
		HashSet<Row> rs = new HashSet<Row>();
		rs.addAll(Arrays.asList(r));
		return gson.toJson(dao.saveSerialNumbersRows(idhead, rs));
	  }
	  @POST
	  @Path("createreport")
	  @Produces(MediaType.TEXT_HTML)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String createReport(@FormParam("filter") String filter) {
		Gson gson = new Gson();
		DocumentDao dao = new DocumentDao();
		GenerateObject r = gson.fromJson(filter, GenerateObject.class);
		return gson.toJson(dao.getOrderReport(r));
	  }
	  @POST
	  @Path("checkhead")
	  @Produces(MediaType.TEXT_HTML)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String checkHead(@FormParam("head") String headfilter) {
		Gson gson = new Gson();
		DocumentDao dao = new DocumentDao();
		Head h = gson.fromJson(headfilter, Head.class);
		return gson.toJson(dao.headValidation(h));
	  }
	  @DELETE
	  @Path("removerow/{idrow}")
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String deleteRow(@PathParam("idrow") int id){
		  Gson gson = new Gson();
		  try{
			  DocumentDao dao = new DocumentDao();
			  return gson.toJson(dao.removeRow(id));
		  }catch(Exception e){
			  return gson.toJson("");
		  }
	  }
	  @Context
		ServletContext context;
	  @POST
	  @Path("head/conad")
	  @Produces(MediaType.TEXT_HTML)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	  public String getConadList(@FormParam("filter") String filter){
		Gson gson = new Gson();
		DocumentDao dao = new DocumentDao();
		HeadFilter filterDoc = gson.fromJson(filter,HeadFilter.class);
		return gson.toJson(dao.createConadTrack(context, filterDoc));
	  }
}
