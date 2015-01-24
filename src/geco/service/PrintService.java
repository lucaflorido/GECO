package geco.service;

import geco.dao.PrinterDao;
import geco.dao.UserDao;
import geco.pojo.TblUser;
import geco.vo.GECOObject;
import geco.vo.GECOReportOrder;
import geco.vo.GECOSuccess;
import geco.vo.filter.product.SelectProductsFilter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.google.gson.Gson;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;


@Path("print")
public class PrintService {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String print(){
		try{
			//generateAshwinFriends();
			TblUser user = new TblUser();
			user.setUsername("WOWOWOWOOW");
			user.setPassword("pwd");
			TblUser user2 = new TblUser();
			user2.setUsername("WOWOWOWOOW2");
			user2.setPassword("pwd2");
			JasperCompileManager.compileReportToFile(context.getRealPath("report2.jrxml"), context.getRealPath("test.jasper"));
			Map<String, Object> map = new HashMap<String ,Object>();
			map.put("test", user);
			Collection<TblUser> userscoll = new ArrayList<TblUser>();
			userscoll.add(user);
			userscoll.add(user2);
			JRDataSource datasource = new JRBeanCollectionDataSource(new UserDao().getListUser());
			JasperPrint print = JasperFillManager.fillReport(context.getRealPath("test.jasper"),map,datasource );
			FileOutputStream fileOutputStream = new FileOutputStream(context.getRealPath("test.pdf"));
			JasperExportManager.exportReportToPdfStream(print, fileOutputStream); 
		}catch(Exception ex){
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
		
		return "test.pdf";
	}
	@Context
	ServletContext context;
	/*private void generateAshwinFriends() throws IOException,FileNotFoundException, com.lowagie.text.DocumentException {
		//PdfReader pdfTemplate = new PdfReader("http://localhost:8080/GeCoServices/teston.pdf");
		
		FileOutputStream fileOutputStream = new FileOutputStream(context.getRealPath("test.pdf"));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.close();
	
	}*/
	@GET
	@Path("head/{idhead}")
	@Produces(MediaType.TEXT_HTML)
	public String singleHead(@PathParam("idhead") int id) {
		PrinterDao dao = new PrinterDao();
		Gson gson = new Gson();
		//DocumentDao dao = new DocumentDao();
		//Head head = new Head();
		//head = dao.getSingleHead(id);
		return gson.toJson(dao.printSingleDocument(context, id));
	}
	@POST
	@Path("heads")
	@Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	public String multipleHeads(@FormParam("ids") String ids) {
		PrinterDao dao = new PrinterDao();
		Gson gson = new Gson();
		//DocumentDao dao = new DocumentDao();
		//Head head = new Head();
		//head = dao.getSingleHead(id);
		int[] idsHeads = gson.fromJson(ids, int[].class);
		return gson.toJson(dao.printMultipleDocument(context, idsHeads));
	}
	@POST
	@Path("products")
	@Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	public String printPrices(@FormParam("filter") String filter) {
		PrinterDao dao = new PrinterDao();
		Gson gson = new Gson();
		//DocumentDao dao = new DocumentDao();
		//Head head = new Head();
		//head = dao.getSingleHead(id);
		SelectProductsFilter f = gson.fromJson(filter, SelectProductsFilter.class);
		return gson.toJson(dao.printProductList(context,f));
	}
	@POST
	@Path("list/{idlist}")
	@Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	public String printList(@PathParam("idlist") String ids) {
		PrinterDao dao = new PrinterDao();
		Gson gson = new Gson();
		//DocumentDao dao = new DocumentDao();
		//Head head = new Head();
		//head = dao.getSingleHead(id);
		int id = Integer.parseInt(ids);
		return gson.toJson(dao.printList(context, id));
	}
	@POST
	@Path("reportorder")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String singleReport(@FormParam("filter") String filtered) {
		PrinterDao dao = new PrinterDao();
		Gson gson = new Gson();
		GECOReportOrder[] report = gson.fromJson(filtered, GECOReportOrder[].class);
		return gson.toJson(dao.printReportOrder(context, report));
	}
	@POST
	@Path("/upload")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public String uploadFile(
		            @FormDataParam("file") InputStream fileInputStream,
		            @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) {
			Gson gson = new Gson(); 
	        return  gson.toJson(new PrinterDao().uploadFile(fileInputStream, contentDispositionHeader, context));
	 
		    }
		 
	   
}
