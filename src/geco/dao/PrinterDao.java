package geco.dao;


import geco.print.PrintPriceList;
import geco.print.PrintProductList;
import geco.print.PrintProductStorage;
import geco.print.PrintReportOrder;
import geco.print.PrintReportOrderSubreport;
import geco.print.PrintSingleHead;
import geco.properties.GECOParameter;
import geco.vo.Company;
import geco.vo.Document;
import geco.vo.GECOError;
import geco.vo.GECOObject;
import geco.vo.GECOReportOrder;
import geco.vo.GECOReportOrderCustomerQuantity;
import geco.vo.GECOReportOrderProduct;
import geco.vo.GECOSuccess;
import geco.vo.Head;
import geco.vo.ListProduct;
import geco.vo.Product;
import geco.vo.Report;
import geco.vo.Row;
import geco.vo.Storage;
import geco.vo.filter.StoreFilter;
import geco.vo.filter.product.SelectProductsFilter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.print.attribute.standard.PrinterResolution;
import javax.servlet.ServletContext;










import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class PrinterDao {
	public String printSingleDocument(ServletContext context,int id){
		String documentType ="";
		try{
			//generateAshwinFriends();
			Company comp = new RegistryDao().getCompany();
			Head head = new DocumentDao().getSingleHead(id);
		    documentType = getReportName(head.getDocument());
			File f = new File(context.getRealPath("report/"+documentType+".jasper"));
			if(f.exists() == false){
				JasperCompileManager.compileReportToFile(context.getRealPath("report/"+documentType+".jrxml"), context.getRealPath("report/"+documentType+".jasper"));
			}
		    
			Collection<PrintSingleHead> headcoll = new ArrayList<PrintSingleHead>();
			Map<String, Object> map = new HashMap<String ,Object>();
			map.put("title","Fattura");
			double totqta = 0;
			Double totnecks = 0.0;
			for (Iterator<Row> it = head.getRows().iterator();it.hasNext();){
				Row r = it.next();
				PrintSingleHead ph = new PrintSingleHead();
				ph.setFromObject(comp,head, r);
				headcoll.add(ph);
				totqta = totqta + r.getQuantity();
				totnecks = totnecks + r.getNecks();
			}
			if (head.getDocument().isOrder() && head.getDocument().getSupplier()){
				for (Iterator<PrintSingleHead> itp = headcoll.iterator();itp.hasNext();){
					PrintSingleHead p = itp.next();
					p.setTot_colli(String.valueOf(totnecks));
					p.setTot_qta(String.valueOf(totqta));
				}
			}
			JRDataSource datasource = new JRBeanCollectionDataSource(headcoll);
			JasperPrint print = JasperFillManager.fillReport(context.getRealPath("report/"+documentType+".jasper"),map,datasource );
			FileOutputStream fileOutputStream = new FileOutputStream(context.getRealPath("report/"+documentType+".pdf"));
			JasperExportManager.exportReportToPdfStream(print, fileOutputStream);
		}catch(Exception ex){
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
		
		return "/GeCoServices/report/"+documentType+".pdf";
	}
	private String getReportName(Document d){
		if (d.getReport() == null){
			if (d.getCustomer() == true){
				
				if (d.isOrder() == true){
						return "order";
				}
				if (d.isInvoice() == true){
					return "invoice";
				}
				if (d.isTransport() == true){
					return "ddt";
				}
			}else if (d.getSupplier()){
				if (d.isOrder() == true){
					return "order";
				}
				if (d.isInvoice() == true){
					return "invoice";
				}
				if (d.isTransport() == true){
					return "ddt";
				}
			}
		}else{
			return d.getReport().getName();
		}
		return "document";
	}
	public String printMultipleDocument(ServletContext context,int[] ids){
		try{
			//generateAshwinFriends();
			File f = new File(context.getRealPath("report/document.jasper"));
			if(f.exists() == false){
				JasperCompileManager.compileReportToFile(context.getRealPath("report/document.jrxml"), context.getRealPath("report/document.jasper"));
		    }
			Map<String, Object> map = new HashMap<String ,Object>();
			map.put("title","Fattura");
			
			Company comp = new RegistryDao().getCompany();
		    JasperPrint print = null; 
		    for(int i=0;i<ids.length;i++){
		    	Head head = new DocumentDao().getSingleHead(ids[i]);
				JasperCompileManager.compileReportToFile(context.getRealPath("report/"+getReportName(head.getDocument())+".jrxml"), context.getRealPath("report/"+getReportName(head.getDocument())+""+ids[i]+".jasper"));
				Collection<PrintSingleHead> headcoll = new ArrayList<PrintSingleHead>();
				
				for (Iterator<Row> it = head.getRows().iterator();it.hasNext();){
					PrintSingleHead ph = new PrintSingleHead();
					ph.setFromObject(comp,head, it.next());
					headcoll.add(ph);
				}
				JRDataSource datasource = new JRBeanCollectionDataSource(headcoll);
				if (i == 0){
				print = JasperFillManager.fillReport(context.getRealPath("report/"+getReportName(head.getDocument())+""+ids[i]+".jasper"),map,datasource );
				}else{
					JasperPrint print2 = JasperFillManager.fillReport(context.getRealPath("report/"+getReportName(head.getDocument())+""+ids[i]+".jasper"),map,datasource );
					List pages = print2 .getPages();
		            for (int j = 0; j < pages.size(); j++) {
			            JRPrintPage object = (JRPrintPage)pages.get(j);
			            print.addPage(object);
		            }
				}
		    }
			FileOutputStream fileOutputStream = new FileOutputStream(context.getRealPath("report/multidocument.pdf"));
			JasperExportManager.exportReportToPdfStream(print, fileOutputStream);
			for(int y=0;y<ids.length;y++){
				Head head = new DocumentDao().getSingleHead(ids[y]);
				File fd = new File(context.getRealPath("report/"+getReportName(head.getDocument())+""+ids[y]+".jasper"));
				 
			    if(fd.exists() == true){
					fd.delete();
			    }

			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
		return "/GeCoServices/report/multidocument.pdf";
	}
	@SuppressWarnings("unchecked")
	public String printProductList(ServletContext context,SelectProductsFilter filter){
		try{
			//generateAshwinFriends();
			File f = new File(context.getRealPath("report/productlist.jasper"));
			 
		    if(f.exists() == false){
				JasperCompileManager.compileReportToFile(context.getRealPath("report/productlist.jrxml"), context.getRealPath("report/productlist.jasper"));
			}
		    Company comp = new RegistryDao().getCompany();
		    GECOObject obj = new RegistryDao().getProductList(filter);
		    TreeSet<Product> prods = new TreeSet<Product>();
		    if (obj.type == GECOParameter.SUCCESS_TYPE){
		    	prods = (TreeSet<Product>)((GECOSuccess)obj).success;
		    }
			 
			Collection<PrintProductList> headcoll = new ArrayList<PrintProductList>();
			Map<String, Object> map = new HashMap<String ,Object>();
			map.put("title","Lista");
			for (Iterator<Product> it = prods.iterator();it.hasNext();){
				PrintProductList ph = new PrintProductList();
				ph.setFromObject(comp, it.next());
				headcoll.add(ph);
			}
			JRDataSource datasource = new JRBeanCollectionDataSource(headcoll);
			JasperPrint print = JasperFillManager.fillReport(context.getRealPath("report/productlist.jasper"),map,datasource );
			FileOutputStream fileOutputStream = new FileOutputStream(context.getRealPath("report/productlist.pdf"));
			JasperExportManager.exportReportToPdfStream(print, fileOutputStream);
		}catch(Exception ex){
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
		
		return "/GeCoServices/report/productlist.pdf";
	}
	public String printList(ServletContext context,int id){
		try{
			//generateAshwinFriends();
			File f = new File(context.getRealPath("report/pricelist.jasper"));
			 
		    if(f.exists() == false){
				JasperCompileManager.compileReportToFile(context.getRealPath("report/pricelist.jrxml"), context.getRealPath("report/pricelist.jasper"));
			}
		    Company comp = new RegistryDao().getCompany();
			geco.vo.List list  = new RegistryDao().getSingleList(id);
			Collection<PrintPriceList> headcoll = new ArrayList<PrintPriceList>();
			Map<String, Object> map = new HashMap<String ,Object>();
			map.put("title","Fattura");
			for (Iterator<ListProduct> it = list.getListproduct().iterator();it.hasNext();){
				PrintPriceList ph = new PrintPriceList();
				ListProduct lp = it.next();
				ph.setFromObject(comp,lp.getProduct(), list,lp);
				headcoll.add(ph);
			}
			JRDataSource datasource = new JRBeanCollectionDataSource(headcoll);
			JasperPrint print = JasperFillManager.fillReport(context.getRealPath("report/pricelist.jasper"),map,datasource );
			FileOutputStream fileOutputStream = new FileOutputStream(context.getRealPath("report/pricelist.pdf"));
			JasperExportManager.exportReportToPdfStream(print, fileOutputStream);
		}catch(Exception ex){
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
		
		return "/GeCoServices/report/pricelist.pdf";
	}
	public String printReportOrder(ServletContext context,GECOReportOrder[] report){
		try{
			//generateAshwinFriends();
			File f = new File(context.getRealPath("report/reportOrder.jasper"));
			 
		    if(f.exists() == false){
				JasperCompileManager.compileReportToFile(context.getRealPath("report/reportOrder.jrxml"), context.getRealPath("report/reportOrder.jasper"));
			}
		    
		   
			Collection<PrintReportOrder> headcoll = new TreeSet<PrintReportOrder>();
			
			Map<String, Object> map = new HashMap<String ,Object>();
			map.put("title","Fattura");
			for(int i=0;i<report.length;i++){
				GECOReportOrder rep = report[i];
				for (Iterator<GECOReportOrderProduct> irg = rep.getProducts().iterator();irg.hasNext();){
					GECOReportOrderProduct grop = irg.next();
					PrintReportOrder ro = new PrintReportOrder();
					ro.setFornitore(report[i].getSuppliername());
					ro.setCodice_prodotto(grop.getProductcode()+" "+grop.getProductdescription());
					for (Iterator<GECOReportOrderCustomerQuantity> irc = grop.getCustomers().iterator();irc.hasNext();){
						GECOReportOrderCustomerQuantity cust = irc.next();
						if (ro.getCustomers() == null)
						ro.setCustomers(new TreeSet<PrintReportOrderSubreport>());
						PrintReportOrderSubreport prs = new PrintReportOrderSubreport();
						prs.setCliente(cust.getCustomername());
						prs.setQuantita(String.valueOf(cust.getQuantity()));
						prs.setUm(cust.getUm());
						prs.setTotale(String.valueOf(grop.getTotal()));
						ro.getCustomers().add(prs);
					}
					headcoll.add(ro);
				}
			}
			
			JRDataSource datasource = new JRBeanCollectionDataSource(headcoll);
			JasperPrint print = JasperFillManager.fillReport(context.getRealPath("report/reportOrder.jasper"),map,datasource );
			FileOutputStream fileOutputStream = new FileOutputStream(context.getRealPath("report/reportOrder.pdf"));
			JasperExportManager.exportReportToPdfStream(print, fileOutputStream);
		}catch(Exception ex){
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
		
		return "/GeCoServices/report/reportOrder.pdf";
	}
	public GECOObject uploadFile(InputStream fileInputStream,FormDataContentDisposition contentDispositionHeader,ServletContext context) {
 	
	    String filePath = "report/" + contentDispositionHeader.getFileName();
		Report rep = new Report();
		GECOObject obj = rep.setupNewReport(contentDispositionHeader.getFileName(), filePath);
		if (obj.type.equals(GECOParameter.ERROR_TYPE)){
			return obj;
		}
	    // save the file to the server
	    obj = saveFile(fileInputStream, context.getRealPath(filePath));
	    if (obj.type.equals(GECOParameter.ERROR_TYPE)){
			return obj;
		}else{
			obj = new BasicDao().saveUpdatesReport(rep);
		}
	    if (obj.type.equals(GECOParameter.SUCCESS_TYPE)){
	    	try{
		    	File f = new File(context.getRealPath("report/"+rep.getName()+".jasper"));
				if(f.exists() == true){
					JasperCompileManager.compileReportToFile(context.getRealPath("report/"+rep.getName()+".jrxml"), context.getRealPath("report/"+rep.getName()+".jasper"));
				}
		    }catch(Exception ex){
				ex.printStackTrace();
				obj = new GECOError("CANC JASPER",ex.getMessage());
			}
	    }
	    
	    
 
    return obj;

    }
	private GECOObject saveFile(InputStream uploadedInputStream,
            String serverLocation) {
		 
        try {
	            OutputStream outpuStream = new FileOutputStream(new File(serverLocation));
	            int read = 0;
            byte[] bytes = new byte[1024];
	 
            outpuStream = new FileOutputStream(new File(serverLocation));
	            while ((read = uploadedInputStream.read(bytes)) != -1) {
	                outpuStream.write(bytes, 0, read);
	            }
            outpuStream.flush();
	            outpuStream.close();
        } catch (IOException e) {
 
	            e.printStackTrace();
	            return new GECOError("SAVE REPORT", e.getMessage());
        }
        return new GECOSuccess();
	 }
	@SuppressWarnings("unchecked")
	public String printProductListStorage(ServletContext context,StoreFilter filter){
		try{
			//generateAshwinFriends();
			File f = new File(context.getRealPath("report/productliststorage.jasper"));
			 
		    if(f.exists() == false){
				JasperCompileManager.compileReportToFile(context.getRealPath("report/productliststorage.jrxml"), context.getRealPath("report/productliststorage.jasper"));
			}
		    Company comp = new RegistryDao().getCompany();
		    ArrayList<Storage> prodsNoOrdered = new  StoreDao().getListStorage(filter);
		    TreeSet<Storage> prods = new TreeSet<Storage>();
		    prods.addAll(prodsNoOrdered);
			 
			Collection<PrintProductStorage> headcoll = new ArrayList<PrintProductStorage>();
			Map<String, Object> map = new HashMap<String ,Object>();
			map.put("title","Lista");
			for (Iterator<Storage> it = prods.iterator();it.hasNext();){
				PrintProductStorage ph = new PrintProductStorage();
				ph.setFromObject(comp, it.next());
				headcoll.add(ph);
			}
			JRDataSource datasource = new JRBeanCollectionDataSource(headcoll);
			JasperPrint print = JasperFillManager.fillReport(context.getRealPath("report/productliststorage.jasper"),map,datasource );
			FileOutputStream fileOutputStream = new FileOutputStream(context.getRealPath("report/productliststorage.pdf"));
			JasperExportManager.exportReportToPdfStream(print, fileOutputStream);
		}catch(Exception ex){
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
		
		return "/GeCoServices/report/productliststorage.pdf";
	}
}
