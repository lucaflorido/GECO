package geco.dao;

import geco.exception.GecoException;
import geco.hibernate.DataUtilConverter;
import geco.hibernate.HibernateUtils;
import geco.pojo.TblCustomer;
import geco.pojo.TblDocument;
import geco.pojo.TblGenerateHeadRow;
import geco.pojo.TblHead;
import geco.pojo.TblRow;
import geco.pojo.TblSupplier;
import geco.properties.GECOParameter;
import geco.util.accounting.AccountingHelper;
import geco.util.document.DocumentHelper;
import geco.util.store.StoreManager;
import geco.vo.Counter;
import geco.vo.CounterYear;
import geco.vo.Customer;
import geco.vo.Document;
import geco.vo.GECOError;
import geco.vo.GECOObject;
import geco.vo.GECOReportOrder;
import geco.vo.GECOReportOrderCustomerQuantity;
import geco.vo.GECOReportOrderProduct;
import geco.vo.GECOSuccess;
import geco.vo.GenerateDocsObject;
import geco.vo.GenerateObject;
import geco.vo.Head;
import geco.vo.NeededObject;
import geco.vo.Product;
import geco.vo.Row;
import geco.vo.RowTotalCalculator;
import geco.vo.Storage;
import geco.vo.StorageSerialCode;
import geco.vo.Supplier;
import geco.vo.TaxRate;
import geco.vo.Transporter;
import geco.vo.UnitMeasure;
import geco.vo.filter.GenerateDocsFilter;
import geco.vo.filter.HeadFilter;
import geco.vo.filter.NeededFilter;
import geco.vo.filter.document.DocumentsInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.sql.rowset.serial.SerialClob;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class DocumentDao {
	public DocumentsInfo getPagesNumber(int size,HeadFilter filter){
		int pages = 0;
		int count =0;
		DocumentsInfo di = new DocumentsInfo();
		Session session = HibernateUtils.getSessionFactory().openSession();
		try{
			Criteria cr = session.createCriteria(TblHead.class,"head");
			setCriteriaHead(cr, filter);
			count = ((Long) cr.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			di.count = count;
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		if (count > size){
			pages = count / size;
		}else{
			pages = 0;
		}
		di.pages = pages;
		return di;
	}
	
	/*****
	 * Get List of Head 
	 */
	public ArrayList<Head> getHeadList(HeadFilter filter){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Head> list = new ArrayList<Head>();
		try{
			Criteria cr = session.createCriteria(TblHead.class,"head");
			cr.setFirstResult(filter.startelement);
			cr.setMaxResults(filter.pageSize);
			setCriteriaHead(cr, filter);
			List<TblHead> heads = cr.list();
			if (heads.size() > 0){
				for (Iterator<TblHead> iterator = heads.iterator(); iterator.hasNext();){
					TblHead tblhead = iterator.next();
					Head head = new Head();
					head.convertFromTable(tblhead);
					list.add(head);
				}
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return list;
	}
	private void setCriteriaHead(Criteria cr,HeadFilter filter){
		
		cr.addOrder(Order.desc("date"));
		cr.addOrder(Order.desc("number"));
		cr.createAlias("head.document", "document");
		if (filter.isCustomer == true){
			
			cr.add(Restrictions.eq("document.customer", true));
		}else{
			if (filter.isSupplier == true){
				
				cr.add(Restrictions.eq("document.supplier", true));
			}
		}
		if (filter.isOrder == true){
			
			cr.add(Restrictions.eq("document.order", true));
		}else{
			if (filter.isInvoice == true){
				cr.add(Restrictions.eq("document.invoice", true));
			}else{
				if (filter.isTransport == true){
					cr.add(Restrictions.eq("document.transport", true));
				}
			}
		}
		if (filter.customer != null){
			cr.createAlias("head.customer", "customer");
			cr.add(Restrictions.eq("customer.idCustomer", filter.customer.getIdCustomer()));
		}
		if (filter.supplier != null){
			cr.createAlias("head.supplier", "supplier");
			cr.add(Restrictions.eq("supplier.idSupplier", filter.supplier.getIdSupplier()));
		}
		if (filter.doc != null){
			
			cr.add(Restrictions.eq("document.idDocument", filter.doc.getIdDocument()));
		}
		if (filter.fromDate != null && filter.fromDate.equals("") != true && filter.toDate != null &&   filter.toDate.equals("") != true  ){
			cr.add(Restrictions.between("head.date", DataUtilConverter.convertDateFromString(filter.fromDate), DataUtilConverter.convertDateFromString(filter.toDate)));
		}
		if (filter.active != filter.inactive){
			cr.add(Restrictions.eq("head.disable",filter.inactive));
		}
		if (filter.number != null && filter.number != ""){
			int num = Integer.parseInt(filter.number);
			cr.add(Restrictions.eq("head.number",num));
		}
	}
	/***
	 * Save update Heads
	 * **/
	public GECOObject saveUpdatesHead(Head sm) throws GecoException{
		if (sm.control() == null){
			boolean found = false;
			found = sm.calculateNumber();
			setUpCounter(sm, found);
		}else{
			return sm.control();
		}
		return saveHead(sm);
	}
	private void setUpCounter(Head sm,boolean found){
		if (found == false){
			Counter c = sm.getDocument().getCounter();
			CounterYear cy = new CounterYear();
			Calendar cal = Calendar.getInstance();
			cal.setTime(DataUtilConverter.convertDateFromString(sm.getDate()));
			int year = cal.get(Calendar.YEAR);
			cy.setYear(year);
			cy.setValue(sm.getNumber()+1);
			c.getYearsvalue().add(cy);
			Counter[] sc = {c};
			new BasicDao().saveUpdatesCounter(sc);
			c = new BasicDao().getCounter(c.getIdCounter());
			sm.getDocument().setCounter(c);
		}
	}
	public GECOObject saveUpdatesHead(Head sm,int index) throws GecoException{
		if (sm.control() == null){
			boolean found = false;
			found = sm.calculateNumber(index);
			setUpCounter(sm, found);
		}else{
			return sm.control();
		}
		return saveHead(sm);
	}
	public GECOObject saveUpdatesHead(Head sm,int index,boolean forceSerialNumber) throws GecoException{
		if (sm.control(forceSerialNumber) == null){
			boolean found = false;
			found = sm.calculateNumber(index);
			setUpCounter(sm, found);
		}else{
			return sm.control();
		}
		return saveHead(sm);
	}
	private GECOObject saveHead(Head sm) throws GecoException{
		int id=0;
			
			if (this.checkHeadNumber(sm) == true){	
				if (sm.getNumber() != 0  ){
					DocumentHelper.totalHeadCalculation(sm);
					Session session = HibernateUtils.getSessionFactory().openSession();
					Transaction tx = null;
					
					try{
						tx = session.beginTransaction();
						//Store Management
						StoreManager.calculateHead(sm,session);
						//Account Management
						
						TblHead tblsm = new TblHead();
						tblsm.convertToTableSingleToSave(sm);
						session.saveOrUpdate(tblsm);
						id=tblsm.getIdHead();
						sm.setIdHead(id);
						sm.convertFromTableSingle(tblsm);
						AccountingHelper.generateAccount(sm, session);
						tx.commit();
					}catch(HibernateException e){
						
						System.err.println("ERROR IN LIST!!!!!!");
						if (tx!= null) tx.rollback();
						e.printStackTrace();
						return  new GECOError(GECOParameter.ERROR_HIBERNATE,"Errore nel salvataggio saveHead");
					}catch(GecoException e){
						
						System.err.println("GECO ERROR");
						if (tx!= null) tx.rollback();
						e.printStackTrace();
						return  new GECOError(GECOParameter.ERROR_HIBERNATE,e.GECOmessage);
					}finally{
						session.close();
					}
				}
			}else{
				return  new GECOError(GECOParameter.ERROR_DUPLICATE,"Numero Documento già inserito");
			}
		BasicDao bd = new BasicDao();
		bd.saveUpdatesSingleCounter(sm.getDocument().getCounter());
			
		return new GECOSuccess(sm);
	}
	/***
	 * DELETE A SINGLE Tblhead
	 * **/
	public GECOObject deleteHead(Head sm){
		sm = getSingleHead(sm.getIdHead());
		TblHead tblsm = new TblHead();
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;
		boolean forceDelete = false;
		try{
			tblsm.convertToTable(sm);
			tx = session.beginTransaction();
			if (sm.getDocument().isDeleteable() != true){
				tblsm.setDisable(true);
				session.saveOrUpdate(tblsm);
				deleteGeneratedRowList(sm,session);
				StoreManager.deleteMovements(sm, session);
			}else{
				forceDelete = true;
				deleteGeneratedRowList(sm,session);
			}
			tx.commit();
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			if (tx!= null) tx.rollback();
			e.printStackTrace();
			session.close();
			return new GECOError("DELETE", "Impossibile eliminare il documento selezionato");
		}finally{
			session.close();
		}
		if (forceDelete == true){
			deleteHeadFunction(sm);
		}
		return new GECOSuccess(true);
		
	}
	public GECOObject deleteHeadFunction(Head sm){
		sm = getSingleHead(sm.getIdHead());
		TblHead tblsm = new TblHead();
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;
		try{
			tblsm.convertToTable(sm);
			tx = session.beginTransaction();
			session.delete(tblsm);
			tx.commit();
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			if (tx!= null) tx.rollback();
			e.printStackTrace();
			session.close();
			return new GECOError("DELETE", "Impossibile eliminare il documento selezionato");
		}finally{
			session.close();
		}
		return new GECOSuccess(true);
		
	}
	private void deleteGeneratedRowList(Head head,Session session){
		try{
			Criteria cr = session.createCriteria(TblGenerateHeadRow.class,"genhead");
			cr.add(Restrictions.eq("genhead.headGenerate.idHead", head.getIdHead()));
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			@SuppressWarnings("unchecked")
			List<TblGenerateHeadRow> heads = cr.list();
			for (Iterator<TblGenerateHeadRow> i = heads.iterator();i.hasNext();){
				TblGenerateHeadRow ghr = i.next();
				deleteGeneratedRowExecute(ghr);
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
			
		}
	}
	private void deleteGeneratedRowExecute(TblGenerateHeadRow tblGenerateRow){
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.delete(tblGenerateRow);
			tx.commit();
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			if (tx!= null) tx.rollback();
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
	}
	/**
	 * GET A SINGLE CUSTOMER
	 * **/
	public Head getSingleHead(int idhead){
		Session session = HibernateUtils.getSessionFactory().openSession();
		Head head = new Head();
		//head = getMockHead();
		try{
			
			Criteria cr = session.createCriteria(TblHead.class,"head");
			cr.add(Restrictions.eq("idHead", idhead));
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			@SuppressWarnings("rawtypes")
			List heads = cr.list();
			if (heads.size() > 0){
				
				head.convertFromTableSingle((TblHead)heads.get(0));
				
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
			
		}finally{
			session.close();
		}
		
		return head;
	}
	public Head getSingleHead(int idhead,Session session){
		
		Head head = new Head();
		//head = getMockHead();
		try{
			
			Criteria cr = session.createCriteria(TblHead.class,"head");
			cr.add(Restrictions.eq("idHead", idhead));
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			@SuppressWarnings("rawtypes")
			List heads = cr.list();
			if (heads.size() > 0){
				
				head.convertFromTableSingle((TblHead)heads.get(0));
				
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
			
		}finally{
			session.close();
		}
		
		return head;
	}
	public Row getSingleRow(int idrow){
		Session session = HibernateUtils.getSessionFactory().openSession();
		Row row = new Row();
		//head = getMockHead();
		try{
			
			Criteria cr = session.createCriteria(TblRow.class,"row");
			cr.add(Restrictions.eq("idRow", idrow));
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List<TblRow> rows = cr.list();
			if (rows.size() > 0){
				row.convertFromTable((TblRow)rows.get(0));
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
			
		}finally{
			session.close();
		}
		
		return row;
	}
	public Row getSingleRowWithHead(int idrow){
		Session session = HibernateUtils.getSessionFactory().openSession();
		Row row = new Row();
		try{
			Criteria cr = session.createCriteria(TblRow.class,"row");
			cr.add(Restrictions.eq("idRow", idrow));
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List<TblRow> rows = cr.list();
			if (rows.size() > 0){
				row.convertFromTable((TblRow)rows.get(0));
				Head head = new Head();
				head.convertFromTable(rows.get(0).getHead());
				row.setHead(head);
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
			
		}finally{
			session.close();
		}
		
		return row;
	}
	public TblRow getSingleTblRow(int idrow,Session session ){
		 
		Row row = new Row();
		//head = getMockHead();
		try{
			Criteria cr = session.createCriteria(TblRow.class,"row");
			cr.add(Restrictions.eq("idRow", idrow));
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List<TblRow> rows = cr.list();
			if (rows.size() > 0){
				return (TblRow)rows.get(0);
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
			
		}
		
		return null;
	}
	/********
	 * GET FILTERED DOCS FOR GENERATION
	 */
	public ArrayList<Head> getHeadRowsGenerateList(GenerateDocsFilter filter){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Head> list = new ArrayList<Head>();
		try{
			Criteria cr = session.createCriteria(TblHead.class,"head");
			if (filter.getCustomer() != null){
				cr.createAlias("head.customer", "customer");
				cr.add(Restrictions.eq("customer.idCustomer", filter.getCustomer().getIdCustomer()));
			}
			if (filter.getSourcedoc() != null){
				cr.createAlias("head.document", "document");
				cr.add(Restrictions.eq("document.idDocument", filter.getSourcedoc().getIdDocument()));
			}
			if (filter.getSupplier() != null){
				cr.createAlias("head.supplier", "supplier");
				cr.add(Restrictions.eq("supplier.idSupplier", filter.getSupplier().getIdSupplier()));
			}
			if (filter.getDestination() != null){
				cr.createAlias("head.destination", "destination");
				cr.add(Restrictions.eq("destination.idDestination", filter.getDestination().getIdDestination()));
			}
			if (filter.getTransporter() != null){
				cr.createAlias("head.transporter", "transporter");
				cr.add(Restrictions.eq("transporter.idTransporter", filter.getTransporter().getIdTransporter()));
			}
			if (filter.getFromDate() != null && filter.getFromDate().equals("") != true && filter.getToDate() != null &&   filter.getToDate().equals("") != true  ){
				cr.add(Restrictions.between("head.date", DataUtilConverter.convertDateFromString(filter.getFromDate()), DataUtilConverter.convertDateFromString(filter.getToDate())));
			}
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List<TblHead> heads = cr.list();
			if (heads.size() > 0){
				for (Iterator<TblHead> iterator = heads.iterator(); iterator.hasNext();){
					TblHead tblhead = iterator.next();
					if (tblhead.getGeneretedHead() == null || tblhead.getGeneretedHead().size() == 0){
						Head head = new Head();
						head.convertFromTableSingle(tblhead);
						Set<Row> rows = new HashSet<Row>();
						for (Iterator<Row> iterator2 = head.getRows().iterator();iterator2.hasNext();){
							Row rowToCheck = iterator2.next();
							if (checkGeneratedRow(rowToCheck) == true){
								rows.add(rowToCheck);
							}
						}
						head.setRows(rows);
						list.add(head);
					}
				}
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return list;
	}
	private void generateRows(Set<Row> rs,Head source,Head generate){
		for (Iterator<Row> iteratorRow = source.getRows().iterator(); iteratorRow.hasNext();){
			Row rowToAdd = iteratorRow.next();
			if (rowToAdd.isGenerate() == true){
				rowToAdd.setIdRow(0);
				rowToAdd.setHead(generate);
				//rowToAdd.setPrice(rowToAdd.getProduct().getPurchaseprice());
				
				boolean found = false;
				for (Iterator<Row> irs = rs.iterator();irs.hasNext();){
				   Row rexisting = irs.next();
				   if (rexisting.getProduct().getIdProduct() == rowToAdd.getProduct().getIdProduct() && rexisting.getPrice() == rowToAdd.getPrice() ){
					   rexisting.setQuantity(rexisting.getQuantity() + rowToAdd.getQuantity());
					   found = true;
					   break;
				   }
				}
				if (found == false){
					rs.add(rowToAdd);
				}
				
			}
		}
	}
	private  GECOObject generateHead(HashMap<Integer,Set<Head>> map,int number,GenerateDocsObject generateObj,Set<Row> rowsToGenerate,boolean supplier,boolean customer,boolean destination){
		ArrayList<Head> list = new ArrayList<Head>();
		Set<Integer> keys = map.keySet();
		//For each key
		try{
			for(Iterator<Integer> it = keys.iterator();it.hasNext();){
				//Prepare the header of the generated head
				Set<Row> rows = new HashSet<Row>();
				Head headGenerated = new Head();
				headGenerated.setDate(generateObj.getDate());
				headGenerated.setDocument(generateObj.getGenerateDoc());
				//Get the value
				Integer index = it.next();
				Set<Head> hd = map.get(index);
				
				for(Iterator<Head> headIt = hd.iterator();headIt.hasNext();){
					Head headGrouped = headIt.next();
					if (supplier == true){
						headGenerated.setSupplier(headGrouped.getSupplier());
						if (headGenerated.getDocument().isCredit() == true || headGenerated.getDocument().isDebit() == true){
							headGenerated.setPayment(headGrouped.getSupplier().getPayment());
						}
					}
					if (customer == true || destination == true){
						headGenerated.setCustomer(headGrouped.getCustomer());
						if (headGenerated.getDocument().isCredit() == true || headGenerated.getDocument().isDebit() == true){
							headGenerated.setPayment(headGrouped.getCustomer().getPayment());
						}
					}
					if (destination == true){
						headGenerated.setDestination(headGrouped.getDestination());
					}
					generateRows(rows,headGrouped, headGenerated);
					
				}
				headGenerated.setRows(rows);
				headGenerated.calculateNumber(index);
				GECOObject savedHead = saveUpdatesHead(headGenerated,number,true);
				if (savedHead.type .equals(GECOParameter.SUCCESS_TYPE)){
					headGenerated = (Head)((GECOSuccess)savedHead).success;
				}else{
					return savedHead;
				}
				saveGenerateDocument(hd,headGenerated,rowsToGenerate);
				number = number+1;
				list.add(headGenerated);
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			return new GECOError("HB", e.getMessage());
		}
		catch(GecoException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			return new GECOError("GECOEXC", e.getMessage());
		}
		return new GECOSuccess(list);
	}
	public GECOObject createHeadRowsGenerateList(GenerateDocsObject generateObj){
		ArrayList<Head> list = new ArrayList<Head>();
		//try{
			if (generateObj.getHeads().size() > 0){
				//headGenerated.calculateNumber();
				Set<Row> rows = new HashSet<Row>();
				Set<Row> rowsToGenerate = new HashSet<Row>();
				Set<Head> headToGenerate = new HashSet<Head>();
				for (Iterator<Head> iterator = generateObj.getHeads().iterator(); iterator.hasNext();){
					Head head = iterator.next();
					if (head.isGenerate() == true || checkRows(head,rowsToGenerate) == true){
						headToGenerate.add(head);
					}
				}
				if (generateObj.isGroupBySupplier() == true){
					HashMap<Integer,Set<Head>> map = DocumentHelper.groupBySupplier(headToGenerate);
					int number = 0;
					GECOObject gecoObj = new GECOObject();
					if (generateObj.isGroupByCustomer() == true || generateObj.isGroupByDestination() == true){
						Set<Integer> keys = map.keySet();
						for(Iterator<Integer> it = keys.iterator();it.hasNext();){
							Integer index = it.next();
							Set<Head> hd = map.get(index);
							HashMap<Integer,Set<Head>> submap = new HashMap<Integer, Set<Head>>(); 
							if (generateObj.isGroupByCustomer() == true){
								submap = DocumentHelper.groupByCustomer(hd);
							}else if (generateObj.isGroupByDestination() == true){
								submap = DocumentHelper.groupByDestination(hd);
							}
							gecoObj = generateHead(submap, number, generateObj, rowsToGenerate, true, generateObj.isGroupByCustomer(), generateObj.isGroupByDestination());
						}
					}else{
						gecoObj = generateHead(map, number, generateObj, rowsToGenerate, true, false, false);
					}
					return gecoObj;
				}else{
					HashMap<Integer,Set<Head>> map = null;
					if (generateObj.isGroupByCustomer() == true){
						map = DocumentHelper.groupByCustomer(headToGenerate);
					}else if (generateObj.isGroupByDestination() == true){
						map = DocumentHelper.groupByDestination(headToGenerate);
					}else{
						map = new HashMap<Integer, Set<Head>>();
						map.put(0,headToGenerate);
					}
					 
					int number = 0;
					return generateHead(map, number, generateObj, rowsToGenerate, false, generateObj.isGroupByCustomer(), generateObj.isGroupByDestination());
				}
				
			}
				/*
				if (generateObj.isGroupBySupplier() == true){
					HashMap<Integer,Set<Head>> map = DocumentHelper.groupBySupplier(headToGenerate);
					Set<Integer> keys = map.keySet();
					int number = 0;
					if (generateObj.isGroupByCustomer() == false && generateObj.isGroupByDestination() == false){
						for(Iterator<Integer> it = keys.iterator();it.hasNext();){
							Integer index = it.next();
							Set<Head> hd = map.get(index);
							rows = new HashSet<Row>();
							Head headGenerated = new Head();
							headGenerated.setDate(generateObj.getDate());
							headGenerated.setDocument(generateObj.getGenerateDoc());
							for(Iterator<Head> headIt = hd.iterator();headIt.hasNext();){
								Head headGrouped = headIt.next();
								headGenerated.setSupplier(headGrouped.getSupplier());
								if (headGenerated.getDocument().isCredit() == true || headGenerated.getDocument().isDebit() == true){
									headGenerated.setPayment(headGrouped.getSupplier().getPayment());
								}
								for (Iterator<Row> iteratorRow = headGrouped.getRows().iterator(); iteratorRow.hasNext();){
									Row rowToAdd = iteratorRow.next();
									if (rowToAdd.isGenerate() == true){
										rowToAdd.setIdRow(0);
										rowToAdd.setHead(headGenerated);
										rowToAdd.setPrice(rowToAdd.getProduct().getPurchaseprice());
										Set<Row> rs= rows;
										boolean found = false;
										for (Iterator<Row> irs = rs.iterator();irs.hasNext();){
										   Row rexisting = irs.next();
										   if (rexisting.getProduct().getIdProduct() == rowToAdd.getProduct().getIdProduct()){
											   rexisting.setQuantity(rexisting.getQuantity() + rowToAdd.getQuantity());
											   found = true;
											   break;
										   }
										}
										if (found == false){
											rs.add(rowToAdd);
										}
										//rows.add(rowToAdd);
									}
								}
							}
							headGenerated.setRows(rows);
							headGenerated.calculateNumber(index);
							headGenerated = (Head)((GECOSuccess)saveUpdatesHead(headGenerated,number,true)).success;
							saveGenerateDocument(hd,headGenerated,rowsToGenerate);
							number = number+1;
						}*/
					//}else {
						/*for(Iterator<Integer> it = keys.iterator();it.hasNext();){
							Integer index = it.next();
							Set<Head> hd = map.get(index);
							HashMap<Integer,Set<Head>> mapHead = null;
							
							if (generateObj.isGroupByCustomer() == true)
								mapHead = DocumentHelper.groupByCustomer(hd);
							else
								mapHead = DocumentHelper.groupByDestination(hd);
							
							Set<Integer> keysHead = mapHead.keySet();
							for(Iterator<Integer> ith = keysHead.iterator();ith.hasNext();){
								Integer indexHead = ith.next();
								Set<Head> hdCD = map.get(indexHead);
								rows = new HashSet<Row>();
								Head headGenerated = new Head();
								headGenerated.setDate(generateObj.getDate());
								headGenerated.setDocument(generateObj.getGenerateDoc());
								for(Iterator<Head> headIt = hdCD.iterator();headIt.hasNext();){
									Head headGrouped = headIt.next();
									headGenerated.setCustomer(headGrouped.getCustomer());
									headGenerated.setTransporter(headGrouped.getTransporter());
									if (generateObj.isGroupByDestination() == true)
										headGenerated.setDestination(headGrouped.getDestination());
									headGenerated.setSupplier(headGrouped.getSupplier());
									if (headGenerated.getDocument().isCredit() == true || headGenerated.getDocument().isDebit() == true){
										headGenerated.setPayment(headGrouped.getCustomer().getPayment());
									}
									for (Iterator<Row> iteratorRow = headGrouped.getRows().iterator(); iteratorRow.hasNext();){
										Row rowToAdd = iteratorRow.next();
										if (rowToAdd.isGenerate() == true){
											rowToAdd.setIdRow(0);
											rowToAdd.setHead(headGenerated);
											//rowToAdd.setPrice(price)
											Set<Row> rs= rows;
											boolean found = false;
											for (Iterator<Row> irs = rs.iterator();irs.hasNext();){
											   Row rexisting = irs.next();
											   if (rexisting.getProduct().getIdProduct() == rowToAdd.getProduct().getIdProduct()){
												   rexisting.setQuantity(rexisting.getQuantity() + rowToAdd.getQuantity());
												   found = true;
												   break;
											   }
											}
											if (found == false){
												rs.add(rowToAdd);
											}
											//rows.add(rowToAdd);
										}
									}
								}
								headGenerated.setRows(rows);
								headGenerated.calculateNumber(index);
								headGenerated = (Head)((GECOSuccess)saveUpdatesHead(headGenerated,number,true)).success;
								saveGenerateDocument(hd,headGenerated,rowsToGenerate);
								number = number+1;
							}
						}
					}
					
				}else{
					if (generateObj.isGroupByCustomer() == false  && generateObj.isGroupByDestination() == false   ){
						Head singleHead = new Head();
						singleHead.setDate(generateObj.getDate());
						singleHead.setDocument(generateObj.getGenerateDoc());
						singleHead.setRows(new HashSet<Row>());
						for (Iterator<Head> ith = headToGenerate.iterator();ith.hasNext();){
							Head h = ith.next();
							if (h.getRows() != null){
								for (Iterator<Row> ir = h.getRows().iterator();ir.hasNext();){
									Row r = ir.next();
									Row rowToCopy = new Row();
									rowToCopy.copy(r);
									rowToCopy.setIdRow(0);
									//CONTROL PRODUCTS ALREADY IN THE SYSTEM
									Set<Row> rs= singleHead.getRows();
									boolean found = false;
									for (Iterator<Row> irs = rs.iterator();irs.hasNext();){
									   Row rexisting = irs.next();
									   if (rexisting.getProduct().getIdProduct() == rowToCopy.getProduct().getIdProduct()){
										   rexisting.setQuantity(rexisting.getQuantity() + rowToCopy.getQuantity());
										   found = true;
										   break;
									   }
									}
									if (found == false){
										rs.add(rowToCopy);
									}
									//singleHead.getRows().add(rowToCopy);
								}
							}
						}
						singleHead.calculateNumber();
						saveUpdatesHead(singleHead,0,true);
					}else{
						HashMap<Integer,Set<Head>> map = null;
						if (generateObj.isGroupByCustomer() == true)
							map = DocumentHelper.groupByCustomer(headToGenerate);
						else
							map = DocumentHelper.groupByDestination(headToGenerate);
						Set<Integer> keys = map.keySet();
						int number = 0;
						for(Iterator<Integer> it = keys.iterator();it.hasNext();){
							Integer index = it.next();
							Set<Head> hd = map.get(index);
							rows = new HashSet<Row>();
							Head headGenerated = new Head();
							headGenerated.setDate(generateObj.getDate());
							headGenerated.setDocument(generateObj.getGenerateDoc());
							for(Iterator<Head> headIt = hd.iterator();headIt.hasNext();){
								Head headGrouped = headIt.next();
								headGenerated.setCustomer(headGrouped.getCustomer());
								if (generateObj.isGroupByDestination() == true)
									headGenerated.setDestination(headGrouped.getDestination());
								headGenerated.setTransporter(headGrouped.getTransporter());
								if (headGenerated.getDocument().isCredit() == true || headGenerated.getDocument().isDebit() == true){
									headGenerated.setPayment(headGrouped.getCustomer().getPayment());
								}
								for (Iterator<Row> iteratorRow = headGrouped.getRows().iterator(); iteratorRow.hasNext();){
									Row rowToAdd = iteratorRow.next();
									if (rowToAdd.isGenerate() == true){
										rowToAdd.setIdRow(0);
										rowToAdd.setHead(headGenerated);
										//rowToAdd.setPrice(price)
										Set<Row> rs= rows;
										boolean found = false;
										for (Iterator<Row> irs = rs.iterator();irs.hasNext();){
										   Row rexisting = irs.next();
										   if (rexisting.getProduct().getIdProduct() == rowToAdd.getProduct().getIdProduct()){
											   rexisting.setQuantity(rexisting.getQuantity() + rowToAdd.getQuantity());
											   found = true;
											   break;
										   }
										}
										if (found == false){
											rs.add(rowToAdd);
										}
										//rows.add(rowToAdd);
									}
								}
							}
							headGenerated.setRows(rows);
							headGenerated.calculateNumber(index);
							headGenerated = (Head)((GECOSuccess)saveUpdatesHead(headGenerated,number,true)).success;
							
							saveGenerateDocument(hd,headGenerated,rowsToGenerate);
							number = number+1;
						}
					}
				}
			}*/
				
				
				/*
				if (generateObj.isGroupByCustomer() == false  && generateObj.isGroupBySupplier() == false && generateObj.isGroupByDestination() == false   ){
					Head singleHead = new Head();
					singleHead.setDate(generateObj.getDate());
					singleHead.setDocument(generateObj.getGenerateDoc());
					singleHead.setRows(new HashSet<Row>());
					for (Iterator<Head> ith = headToGenerate.iterator();ith.hasNext();){
						Head h = ith.next();
						if (h.getRows() != null){
							for (Iterator<Row> ir = h.getRows().iterator();ir.hasNext();){
								Row r = ir.next();
								Row rowToCopy = new Row();
								rowToCopy.copy(r);
								rowToCopy.setIdRow(0);
								//CONTROL PRODUCTS ALREADY IN THE SYSTEM
								Set<Row> rs= singleHead.getRows();
								boolean found = false;
								for (Iterator<Row> irs = rs.iterator();irs.hasNext();){
								   Row rexisting = irs.next();
								   if (rexisting.getProduct().getIdProduct() == rowToCopy.getProduct().getIdProduct()){
									   rexisting.setQuantity(rexisting.getQuantity() + rowToCopy.getQuantity());
									   found = true;
									   break;
								   }
								}
								if (found == false){
									rs.add(rowToCopy);
								}
								//singleHead.getRows().add(rowToCopy);
							}
						}
					}
					singleHead.calculateNumber();
					saveUpdatesHead(singleHead,0,true);
				}else if (generateObj.isGroupByCustomer() == true ){
					HashMap<Integer,Set<Head>> map = DocumentHelper.groupByCustomer(headToGenerate);
					Set<Integer> keys = map.keySet();
					int number = 0;
					for(Iterator<Integer> it = keys.iterator();it.hasNext();){
						Integer index = it.next();
						Set<Head> hd = map.get(index);
						rows = new HashSet<Row>();
						Head headGenerated = new Head();
						headGenerated.setDate(generateObj.getDate());
						headGenerated.setDocument(generateObj.getGenerateDoc());
						for(Iterator<Head> headIt = hd.iterator();headIt.hasNext();){
							Head headGrouped = headIt.next();
							headGenerated.setCustomer(headGrouped.getCustomer());
							headGenerated.setTransporter(headGrouped.getTransporter());
							if (headGenerated.getDocument().isCredit() == true || headGenerated.getDocument().isDebit() == true){
								headGenerated.setPayment(headGrouped.getCustomer().getPayment());
							}
							for (Iterator<Row> iteratorRow = headGrouped.getRows().iterator(); iteratorRow.hasNext();){
								Row rowToAdd = iteratorRow.next();
								if (rowToAdd.isGenerate() == true){
									rowToAdd.setIdRow(0);
									rowToAdd.setHead(headGenerated);
									//rowToAdd.setPrice(price)
									Set<Row> rs= rows;
									boolean found = false;
									for (Iterator<Row> irs = rs.iterator();irs.hasNext();){
									   Row rexisting = irs.next();
									   if (rexisting.getProduct().getIdProduct() == rowToAdd.getProduct().getIdProduct()){
										   rexisting.setQuantity(rexisting.getQuantity() + rowToAdd.getQuantity());
										   found = true;
										   break;
									   }
									}
									if (found == false){
										rs.add(rowToAdd);
									}
									//rows.add(rowToAdd);
								}
							}
						}
						headGenerated.setRows(rows);
						headGenerated.calculateNumber(index);
						headGenerated = (Head)((GECOSuccess)saveUpdatesHead(headGenerated,number,true)).success;
						
						saveGenerateDocument(hd,headGenerated,rowsToGenerate);
						number = number+1;
					}
				}else if (generateObj.isGroupBySupplier() == true && generateObj.isGroupByDestination() == false ){
					
					}
				}else if (generateObj.isGroupByDestination() == true  ){
						HashMap<Integer,Set<Head>> map = DocumentHelper.groupByDestination(headToGenerate);
						Set<Integer> keys = map.keySet();
						int number = 0;
						for(Iterator<Integer> it = keys.iterator();it.hasNext();){
							Integer index = it.next();
							Set<Head> hd = map.get(index);
							rows = new HashSet<Row>();
							Head headGenerated = new Head();
							headGenerated.setDate(generateObj.getDate());
							headGenerated.setDocument(generateObj.getGenerateDoc());
							for(Iterator<Head> headIt = hd.iterator();headIt.hasNext();){
								Head headGrouped = headIt.next();
								headGenerated.setCustomer(headGrouped.getCustomer());
								headGenerated.setDestination(headGrouped.getDestination());
								headGenerated.setTransporter(headGrouped.getTransporter());
								if (headGenerated.getDocument().isCredit() == true || headGenerated.getDocument().isDebit() == true){
									headGenerated.setPayment(headGrouped.getCustomer().getPayment());
								}
								for (Iterator<Row> iteratorRow = headGrouped.getRows().iterator(); iteratorRow.hasNext();){
									Row rowToAdd = iteratorRow.next();
									if (rowToAdd.isGenerate() == true){
										rowToAdd.setIdRow(0);
										rowToAdd.setHead(headGenerated);
										//rowToAdd.setPrice(price)
										Set<Row> rs= rows;
										boolean found = false;
										for (Iterator<Row> irs = rs.iterator();irs.hasNext();){
										   Row rexisting = irs.next();
										   if (rexisting.getProduct().getIdProduct() == rowToAdd.getProduct().getIdProduct()){
											   rexisting.setQuantity(rexisting.getQuantity() + rowToAdd.getQuantity());
											   found = true;
											   break;
										   }
										}
										if (found == false){
											rs.add(rowToAdd);
										}
										//rows.add(rowToAdd);
									}
								}
							}
							headGenerated.setRows(rows);
							headGenerated.calculateNumber(index);
							headGenerated = (Head)((GECOSuccess)saveUpdatesHead(headGenerated,number,true)).success;
							
							saveGenerateDocument(hd,headGenerated,rowsToGenerate);
							number = number+1;
						}
					
				}
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}
		catch(GecoException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
		}*/
		return new GECOSuccess(list);
	}
	public GECOObject copyHeadRows(GenerateDocsObject generateObj){
		ArrayList<Head> list = new ArrayList<Head>();
		try{
			if (generateObj.getHeads().size() > 0){
				//headGenerated.calculateNumber();
				Head headGenerated = new Head();
				headGenerated.setDate(generateObj.getDate());
				headGenerated.setDocument(generateObj.getGenerateDoc());
				headGenerated.setRows(new HashSet<Row>());
				if (generateObj.getCustomer() != null){
					headGenerated.setCustomer(generateObj.getCustomer());
				}else if (generateObj.getSupplier() != null){
					headGenerated.setSupplier(generateObj.getSupplier());
				}
				for (Iterator<Head> iterator = generateObj.getHeads().iterator(); iterator.hasNext();){
					Head head = iterator.next();
					if (head.isGenerate() == true ){
						for (Iterator<Row> it = head.getRows().iterator();it.hasNext();){
							Row row = it.next();
							Row rowCopied  = new Row();
							rowCopied.copy(row);
							rowCopied.setProduct(new RegistryDao().getSingleCodeProduct(rowCopied.getProductcode(), generateObj.getList().getIdList(),head));
							rowCopied.setPrice(rowCopied.getProduct().getListprice());
							new RowTotalCalculator().rowCalculation(rowCopied);
							headGenerated.getRows().add(rowCopied);
						}
					}
				}
				int number = 0;
				headGenerated.calculateNumber();
				GECOObject obj = saveUpdatesHead(headGenerated);
				
				return obj;
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}
		catch(GecoException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
		}
		return new GECOError(GECOParameter.ERROR_HIBERNATE,"Errore nella copia dei dati");
	}
	private boolean checkHeadNumber(Head head) {
		Session session = HibernateUtils.getSessionFactory().openSession();
		try{
			Calendar cal = Calendar.getInstance();
		    cal.setTime(DataUtilConverter.convertDateFromString(head.getDate()));
		    int year = cal.get(Calendar.YEAR);
			Criteria cr = session.createCriteria(TblHead.class,"head");
			cr.add(Restrictions.eq("number", head.getNumber()));
			cr.add(Restrictions.eq("head.document.idDocument", head.getDocument().getIdDocument()));
			cr.add(Restrictions.between("head.date", toStartOfYear(year), toEndOfYear(year)));
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List heads = cr.list();
			if (heads.size() == 0){
				return true;
				
			}else if (heads.size() == 1){
				if ( ((TblHead)heads.get(0)).getDate().getYear() == DataUtilConverter.convertDateFromString(head.getDate()).getYear() ){
					if (((TblHead)heads.get(0)).getIdHead() == head.getIdHead()){
						return true;
					}else{
						return false;
					}
				}else{
					return true;
				}
			}else{
				for (int i = 0;i < heads.size();i++ ){
					if ( ((TblHead)heads.get(i)).getDate().getYear() == DataUtilConverter.convertDateFromString(head.getDate()).getYear() ){
						if (((TblHead)heads.get(0)).getIdHead() != head.getIdHead()){
							return false;
						}
					}
				}
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
			
		}finally{
			session.close();
		}
		return true;
	}
	private Date toStartOfYear(int year) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.YEAR, year);
	    calendar.set(Calendar.DAY_OF_YEAR, 1);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    return calendar.getTime();
	}

	private Date toEndOfYear(int year) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.YEAR, year);
	    calendar.set(Calendar.MONTH, 11);
	    calendar.set(Calendar.DAY_OF_MONTH, 31);
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE,59);
	    calendar.set(Calendar.SECOND,59);
	    return calendar.getTime();
	}
	private boolean checkGeneratedRow(Row row) {
		boolean nogenerated = false;
		Session session = HibernateUtils.getSessionFactory().openSession();
		try{
			Criteria cr = session.createCriteria(TblGenerateHeadRow.class,"generatedheadrow");
			cr.add(Restrictions.eq("generatedheadrow.rowSource.idRow", row.getIdRow()));
			
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List rows = cr.list();
			if (rows.size() == 0){
				nogenerated= true;
				
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
			
		}finally{
			session.close();
		}
		
		return nogenerated;
	}
	private boolean checkRows(Head head,Set<Row> rows){
		for (Iterator<Row> i = head.getRows().iterator();i.hasNext(); ){
			Row row = i.next();
			if (row.isGenerate() == true){
				Row rToGen = new Row();
				rToGen.copy(row);
				rToGen.setIdRow(row.getIdRow());
				rToGen.setGenerate(true);
				rows.add(rToGen);
				return true;
			}
		}
		return false;
	}
	/*******************************
	 * Generate DOCS
	 * @param heads
	 * @param head
	 */
	private void saveGenerateDocument(Set<Head> heads,Head head,Set<Row> rows){
		for (Iterator<Head> i = heads.iterator();i.hasNext();){
			Head headSource = i.next();
			if (headSource.isGenerate() == true){
				saveGenerateDocumentObject(head,headSource);
			}
				for(Iterator<Row> ir = rows.iterator(); ir.hasNext();){
					Row r = ir.next();
					if (r.isGenerate() == true && r.getIdRow() != 0){
						saveGenerateDocumentObject(head,r);
					}
				}
			
		}
	}
	private void saveGenerateDocumentObject(Head headGenerate,Head headSource){
		TblHead tblHeadSource = new TblHead();
		TblHead tblHeadGenerate = new TblHead();
		tblHeadGenerate.convertToTable(headGenerate);
		tblHeadSource.convertToTable(headSource);
		TblGenerateHeadRow tblGenerateRow = new TblGenerateHeadRow();
		tblGenerateRow.setHeadGenerate(tblHeadGenerate);
		tblGenerateRow.setHeadSource(tblHeadSource);
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.saveOrUpdate(tblGenerateRow);
			tx.commit();
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			if (tx!= null) tx.rollback();
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
	}
	private void saveGenerateDocumentObject(Head headGenerate,Row rowSource){
		TblRow tblRowSource = new TblRow();
		TblHead tblHeadGenerate = new TblHead();
		tblHeadGenerate.convertToTable(headGenerate);
		tblRowSource.convertToTable(rowSource);
		TblGenerateHeadRow tblGenerateRow = new TblGenerateHeadRow();
		tblGenerateRow.setHeadGenerate(tblHeadGenerate);
		tblGenerateRow.setRowSource(tblRowSource);
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.saveOrUpdate(tblGenerateRow);
			tx.commit();
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			if (tx!= null) tx.rollback();
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
	}
	private void deleteGeneratedRow(Head head){
		
	}
	
	/********
	 * GET FILTERED DOCS FOR GENERATION
	 */
	public ArrayList<Head> getHeadRowsNeededList(NeededFilter filter){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Head> list = new ArrayList<Head>();
		try{
			Criteria cr = session.createCriteria(TblHead.class,"head");
			cr.createAlias("head.document", "document");
			cr.createAlias("head.document.storemovement", "storemovement");
			cr.add(Restrictions.eq("storemovement.genericreserved",true));
			if (filter.dateReserved != null && filter.dateReserved.equals("") != true ){
				cr.add(Restrictions.eq("head.date", DataUtilConverter.convertDateFromString(filter.dateReserved)));
			}
			cr.createAlias("head.rows", "rows");
			cr.add(Restrictions.isNotEmpty("rows"));
			if (filter.supplier != null){
				cr.createAlias("rows.product", "product");
				cr.createAlias("product.supplier", "supplier");
				cr.add(Restrictions.eq("supplier.idSupplier",filter.supplier.getIdSupplier()));
			}
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List<TblHead> heads = cr.list();
			if (heads.size() > 0){
				for (Iterator<TblHead> iterator = heads.iterator(); iterator.hasNext();){
					TblHead tblhead = iterator.next();
					if (tblhead.getGeneretedHead() == null || tblhead.getGeneretedHead().size() == 0){
						Head head = new Head();
						if (filter.supplier == null)
							head.convertFromTableSingle(tblhead);
						else
							head.convertFromTableSingle(tblhead,filter.supplier);
						list.add(head);
					}
				}
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return list;
	}
	public ArrayList<Head> createHeadRowsNeeded(NeededObject generateObj){
		ArrayList<Head> list = new ArrayList<Head>();
		try{
			if (generateObj.heads.size() > 0){
				//headGenerated.calculateNumber();
				Set<Head> headToGenerate = new HashSet<Head>();
				for (Iterator<Head> iterator = generateObj.heads.iterator(); iterator.hasNext();){
					Head head = iterator.next();
					if (head.isGenerate() == true || checkRows(head,new HashSet<Row>()) == true){
						headToGenerate.add(head);
						
					}
				}
				Map<Integer,Set<Row>> rows =  DocumentHelper.groupRowBySupplierNeededCalculation(headToGenerate);
				ArrayList<Supplier> suppliers = new RegistryDao().getSupplierList();
				int index = 0;
				for (int i=0;i< suppliers.size();i++){
					if (rows.containsKey(suppliers.get(i).getIdSupplier()) == true){
						Head headToSave = new Head();
						headToSave.setDocument(generateObj.sourceDocument);
						headToSave.setDate(generateObj.date);
						headToSave.calculateNumber(index);
						headToSave.setSupplier(suppliers.get(i));
						headToSave.setRows(rows.get(suppliers.get(i).getIdSupplier()));
						ArrayList<Row> deletedRow = new ArrayList<Row>();
						for (Iterator<Row> ir = headToSave.getRows().iterator();ir.hasNext();){
							Row row = ir.next();
							row = DocumentHelper.setQtaRowNeededCalculation(row);
							if (row.getQuantity() == 0){
								deletedRow.add(row);
							}
						}
						if (deletedRow.size() > 0)
							headToSave.getRows().removeAll(deletedRow);
						new DocumentDao().saveHead(headToSave);
						index++;
					}
				}
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}
		catch(GecoException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
		}
		return list;
	}
	public GECOObject createDailyOrder(GenerateObject filter){
		GECOObject go = null;
		if (checkOrders(filter) == null){
			int index = 0;
			ArrayList<Customer> customers = new RegistryDao().getCustomerListByGroup(filter.getGroup());
			for(Iterator<Customer> it = customers.iterator();it.hasNext();){
				Customer c = it.next();
				Head head = new Head();
				head.setCustomer(c);
				head.setDate(filter.getDate());
				head.setDocument(filter.getDocument());
				head.setRows(getDailyOrdersHistoryRows(filter,c));
				try{
					head.calculateNumber(index);
					index = index +1;
					go = saveHead(head);
				}catch(GecoException e){
					e.printStackTrace();
					go = new GECOError(GECOParameter.ERROR_HIBERNATE, "Errore nel salvataggio degli ordini");
				}
			}
		}else{
			go = checkOrders(filter);
		}
		return new GECOSuccess(getDailyOrderHeads(filter));
	}
	private HashSet<Row> getDailyOrdersHistoryRows(GenerateObject filter,Customer c){
		HashSet<Row> rows = new HashSet<Row>();
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Head> list = new ArrayList<Head>();
		try{
			Criteria cr = session.createCriteria(TblRow.class,"row");
			cr.createAlias("row.head", "head");
			cr.createAlias("head.document", "document");
			cr.createAlias("head.customer", "customer");
			cr.add(Restrictions.eq("customer.idCustomer",c.getIdCustomer()));
			cr.add(Restrictions.eq("document.idDocument",filter.getDocument().getIdDocument()));
			if (filter.getHistoryDate() != null && filter.getHistoryDate().equals("") != true && filter.getDate() != null &&   filter.getDate().equals("") != true  ){
				cr.add(Restrictions.between("head.date", DataUtilConverter.convertDateFromString(filter.getHistoryDate()), DataUtilConverter.convertDateFromString(filter.getDate())));
			}
			cr.setProjection(Projections.distinct(Projections.property("row.productcode")));
			List<String> codes = cr.list();
			if (codes.size() > 0){
				for (Iterator<String> iterator = codes.iterator(); iterator.hasNext();){
					String code = iterator.next();
					Row row = new Row();
					row.setProductcode(code);
					Product prod = new RegistryDao().getSingleCodeProduct(code, 0,null);
					row.setProductdescription(prod.getDescription());
					row.setProduct(prod);
					row.setTaxrate(prod.getTaxrate());
					row.setUm(prod.getUmselected());
					row.setProductum(prod.getUmselected().getCode());
					float conv = (float)row.getProduct().getConversion(row.getUm());
					row.setPrice(row.getProduct().getSellprice() *  conv);
					row.setType("V");
					rows.add(row);
				}
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return rows;
	}
	public ArrayList<Head> getDailyOrderHeads(GenerateObject filter){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Head> list = new ArrayList<Head>();
		try{
			Criteria cr = session.createCriteria(TblHead.class,"head");
			cr.createAlias("head.document", "document");
			cr.add(Restrictions.eq("document.idDocument",filter.getDocument().getIdDocument()));
			cr.createAlias("head.customer", "customer");
			cr.add(Restrictions.eq("customer.group.idGroupCustomer",filter.getGroup().getIdGroupCustomer()));
			cr.add(Restrictions.eq("head.date", DataUtilConverter.convertDateFromString(filter.getDate())));
			
			List<TblHead> heads = cr.list();
			if (heads.size() > 0){
				for (Iterator<TblHead> iterator = heads.iterator(); iterator.hasNext();){
					TblHead tblhead = iterator.next();
					if (tblhead.getGeneretedHead() == null || tblhead.getGeneretedHead().size() == 0){
						Head head = new Head();
						head.convertFromTableSingle(tblhead);
						list.add(head);
					}
				}
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return list;
	}
	private GECOObject checkOrders(GenerateObject filter){
		Session session = HibernateUtils.getSessionFactory().openSession();
		GECOSuccess go = null;
		ArrayList<Head> list = new ArrayList<Head>();
		try{
			Criteria cr = session.createCriteria(TblHead.class,"head");
			cr.add(Restrictions.eq("head.date",DataUtilConverter.convertDateFromString(filter.getDate())));
			cr.add(Restrictions.eq("head.document.idDocument",filter.getDocument().getIdDocument()));
			cr.createAlias("head.customer", "customer");
			cr.add(Restrictions.eq("customer.group.idGroupCustomer",filter.getGroup().getIdGroupCustomer()));
			cr.add(Restrictions.eq("head.disable",false));
			List<TblHead> heads = cr.list();
			if (heads.size() > 0){
				return new GECOError(GECOParameter.ERROR_ALREDY_DONE,"Ordini già aperti");
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return go;
	}
	public GECOObject fillSerialNumbers(int idhead){
		Head h = getSingleHead(idhead);
		GECOObject g = StoreManager.getProductListWithSerialNumbers((HashSet)h.getRows());
		if (g.type == GECOParameter.SUCCESS_TYPE){
			try{
				h.setIncomplete(false);
				saveUpdatesHead(h);
			}catch(GecoException e){
				return new GECOError(GECOParameter.ERROR_HIBERNATE,"Errore nel salvataggio dei dati");
			}
			
		}
		return g;
	}
	public GECOObject getListOfSerialNumbers(int idhead){
		Head h = getSingleHead(idhead);
		GECOObject g = null;
		if (h.getDocument().getStoremovement().isLoad() == true){
			g = StoreManager.getProductListWithoutSerialNumbers((HashSet)h.getRows());
		}else{
			g = StoreManager.getProductListWithSerialNumbers((HashSet)h.getRows());
		}
		return g;
	}
	public GECOObject copyRowSerialNumber(Row r){
		Row  newRow = new Row();
		newRow.setProduct(r.getProduct());
		newRow.setPrice(r.getPrice());
		newRow.setProductcode(r.getProductcode());
		newRow.setProductdescription(r.getProductdescription());
		newRow.setProductum(r.getProductum());
		newRow.setUm(r.getUm());
		newRow.setQuantity(0);
		return new GECOSuccess(newRow);
	}
	public GECOObject saveSerialNumbersRows(int idhead,Set<Row> rows){
		Head h = getSingleHead(idhead);
		try{
			for(Iterator<Row> i = rows.iterator(); i.hasNext();){
				Row r = i.next();
				if (r.getSerialnumber() != "" && r.getSerialnumber() != null && r.getExpiredate() != "" && r.getExpiredate() != null){
					updateInsertRowSerialNumber(r, h.getRows());
				}else{
					new GECOError(GECOParameter.ERROR_VALUE_MISSING, "Attenzione Lotto e/o scadenza mancanti per l'articolo "+r.getProductcode());
				}
			}
			h.setIncomplete(false);
			saveUpdatesHead(h);
		}catch(Exception e){
			new GECOError(GECOParameter.ERROR_HIBERNATE, "Errore nel salvataggio dei dati");
		}
		return new GECOSuccess();
	}
	private void updateInsertRowSerialNumber(Row r,Set<Row> rows){
		if (r.getIdRow() == 0){
			rows.add(r);
		}else{
			for(Iterator<Row> i = rows.iterator(); i.hasNext();){
				Row rold = i.next();
				if (rold.getIdRow() == r.getIdRow()){
					rold.setQuantity(r.getQuantity());
					rold.setSerialnumber(r.getSerialnumber());
					rold.setExpiredate(r.getExpiredate());
					break;
				}
			}
		}
	}
	public GECOObject getOrderReport(GenerateObject filter){
		ArrayList<Supplier> suppliers = getListOfSupplierInDocument(filter);
		ArrayList<Customer> customers = getListOfCustomerInDocument(filter);
		ArrayList<GECOReportOrder> gros = new ArrayList<GECOReportOrder>();
		for (Iterator<Supplier> isup = suppliers.iterator();isup.hasNext();){
			Supplier supplier = isup.next();
			GECOReportOrder gro = new GECOReportOrder();
			gro.setSuppliername(supplier.getSuppliername());
			ArrayList<String> productcodes = getListOfProductInDocument(filter,supplier );
			Set<GECOReportOrderProduct> setgecoproduct =new HashSet<GECOReportOrderProduct>(); 
			gro.setProducts(setgecoproduct);
			for (Iterator<String> ip = productcodes.iterator();ip.hasNext();){
				String prodcode = ip.next();
				GECOReportOrderProduct grop = new GECOReportOrderProduct();
				grop.setProductcode(prodcode);
				gro.getProducts().add(grop);
				double total = 0.0;
				for (Iterator<Customer> c = customers.iterator();c.hasNext();){
					total += getListOfProductInDocument(filter,c.next(),prodcode,grop);
				}
				grop.setTotal(total);
			}
			gros.add(gro);
		}
		return new GECOSuccess(gros);
	}
	private ArrayList<Supplier> getListOfSupplierInDocument(GenerateObject filter){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Supplier> list = new ArrayList<Supplier>();
		try{
			Criteria cr = session.createCriteria(TblRow.class,"row");
			cr.createAlias("row.head", "head");
			cr.createAlias("head.document", "document");
			cr.createAlias("row.product", "product");
			cr.add(Restrictions.eq("document.idDocument",filter.getDocument().getIdDocument()));
			cr.add(Restrictions.eq("head.date", DataUtilConverter.convertDateFromString(filter.getDate())));
			cr.setProjection(Projections.distinct(Projections.property("product.supplier")));
			List<TblSupplier> lists = cr.list();
			for (Iterator<TblSupplier> is = lists.iterator(); is.hasNext();){
				TblSupplier sup = is.next();
				Supplier supobj = new Supplier();
				supobj.convertFromTable(sup);
				list.add(supobj);
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return list;
	}
	private ArrayList<Customer> getListOfCustomerInDocument(GenerateObject filter){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Customer> list = new ArrayList<Customer>();
		try{
			Criteria cr = session.createCriteria(TblHead.class,"head");
			cr.createAlias("head.document", "document");
			
			cr.add(Restrictions.eq("document.idDocument",filter.getDocument().getIdDocument()));
			cr.add(Restrictions.eq("head.date", DataUtilConverter.convertDateFromString(filter.getDate())));
			cr.setProjection(Projections.distinct(Projections.property("head.customer")));
			List<TblCustomer> lists = cr.list();
			for (Iterator<TblCustomer> is = lists.iterator(); is.hasNext();){
				TblCustomer sup = is.next();
				Customer supobj = new Customer();
				supobj.convertFromTable(sup);
				list.add(supobj);
			}
			//list = new ArrayList<Customer>(lists);
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return list;
	}
	private ArrayList<String> getListOfProductInDocument(GenerateObject filter,Supplier s){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<String> list = new ArrayList<String>();
		try{
			Criteria cr = session.createCriteria(TblRow.class,"row");
			cr.createAlias("row.head", "head");
			cr.createAlias("head.document", "document");
			cr.createAlias("row.product", "product");
			cr.createAlias("product.supplier", "supplier");
			cr.add(Restrictions.eq("supplier.idSupplier",s.getIdSupplier()));
			cr.add(Restrictions.eq("document.idDocument",filter.getDocument().getIdDocument()));
			cr.add(Restrictions.eq("head.date", DataUtilConverter.convertDateFromString(filter.getDate())));
			cr.setProjection(Projections.distinct(Projections.property("row.productcode")));
			List<String> lists = cr.list();
			list = new ArrayList<String>(lists);
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return list;
	}
	private double  getListOfProductInDocument(GenerateObject filter,Customer c,String code,GECOReportOrderProduct g){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<GECOReportOrderCustomerQuantity> list = new ArrayList<GECOReportOrderCustomerQuantity>();
		double qta = 0.0;
		try{
			Criteria cr = session.createCriteria(TblRow.class,"row");
			cr.createAlias("row.head", "head");
			cr.createAlias("head.document", "document");
			cr.createAlias("row.product", "product");
			cr.createAlias("product.supplier", "supplier");
			cr.add(Restrictions.eq("row.productcode",code));
			cr.add(Restrictions.eq("head.customer.idCustomer",c.getIdCustomer()));
			cr.add(Restrictions.eq("document.idDocument",filter.getDocument().getIdDocument()));
			cr.add(Restrictions.eq("head.date", DataUtilConverter.convertDateFromString(filter.getDate())));
			List<TblRow> lists = cr.list();
			
			for (Iterator<TblRow> i = lists.iterator(); i.hasNext();){
				TblRow r = i.next();
				g.setProductcode(code);
				g.setProductdescription(r.getProductdescription());
				g.setProductum(r.getProductum());
				if (g.getCustomers() == null)
					g.setCustomers(new HashSet<GECOReportOrderCustomerQuantity>());
				GECOReportOrderCustomerQuantity grocq = new GECOReportOrderCustomerQuantity();
				grocq.setCustomername(c.getCustomername());
				grocq.setQuantity(r.getQuantity());
				grocq.setIdRow(r.getIdRow());
				grocq.setUm(r.getProductum());
				qta += r.getQuantity();
				g.getCustomers().add(grocq);
			}
			
			
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return qta;
	}
	private ArrayList<Double> getListOfPrices(GenerateObject filter,Customer c){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Double> list = new ArrayList<Double>();
		try{
			Criteria cr = session.createCriteria(TblRow.class,"row");
			cr.createAlias("row.head", "head");
			cr.createAlias("head.document", "document");
			cr.createAlias("row.product", "product");
			cr.add(Restrictions.eq("document.idDocument",filter.getDocument().getIdDocument()));
			cr.setProjection(Projections.distinct(Projections.property("row.price")));
			List<Double> lists = cr.list();
			list = new ArrayList<Double>(lists);
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return list;
	}
	public GECOObject removeRow(int idRow){
		GECOSuccess suc = new GECOSuccess();
		Row r = getSingleRowWithHead(idRow);
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;
		if (r.getHead().isGenerateTo() == false){
			try{
				tx = session.beginTransaction();
				if (r.getHead().getDocument().getStoremovement().isStoreMovementActive() == true){
					StoreManager.removeSingleRow(r, session);
				}
				TblRow tr= getSingleTblRow(idRow, session);
				session.delete(tr);
				tx.commit();
			}catch(HibernateException e){
				System.err.println("ERROR IN LIST!!!!!!");
				if (tx!= null) tx.rollback();
				e.printStackTrace();
				session.close();
				throw new ExceptionInInitializerError(e);
			}finally{
				session.close();
			}
		}else{
			new GECOError(GECOParameter.ERROR_DELETE,"Impossibile eliminare la riga");
		}
		try{
		//	Session session = HibernateUtils.getSessionFactory().openSession();
			
		}catch(Exception e){
			new GECOError(GECOParameter.ERROR_HIBERNATE,"Errore nell'eliminazione dei dati");
		}
		
		return new GECOSuccess(true);
	}
	/****
	 * 
	 * @param head
	 * @return
	 * check serialnumber date
	 */
	public GECOObject headValidation(Head head){
		Date date = DataUtilConverter.convertDateFromString(head.getDate());
		if (head.getRows() != null){
			for (Iterator<Row> it = head.getRows().iterator();it.hasNext();){
				Row row = it.next();
				if(row.getExpiredate() != null && row.getExpiredate().equals("") == false){
					if (DataUtilConverter.convertDateFromString(row.getExpiredate()).after(date)  == false){
						return new GECOError(GECOParameter.ERROR_EXPIREDDATE,"L'articolo "+row.getProductcode()+" lotto:"+row.getSerialnumber()+" è scaduto");
					}
				}
				if(head.getDocument().getStoremovement().isUnload()){
					if (row.getIdRow() == 0){
						Storage store = new StoreDao().getStorageProduct(row.getProduct());
						if (row.getSerialnumber() != null && row.getSerialnumber().equals("") == false){
							for (Iterator<StorageSerialCode> its = store.getStoragesc().iterator(); its.hasNext();){
								StorageSerialCode ssc = its.next();
								if (ssc.getSerialcode().equals(row.getSerialnumber())){
									if (ssc.getStock() < (row.getQuantity() * row.getProduct().getConversion(row.getUm()))){
										return new GECOError(GECOParameter.ERROR_EXPIREDDATE,"L'articolo "+row.getProductcode()+" lotto:"+row.getSerialnumber()+" ha giacenza inseriore allo scarico");
									}
								}
							}
						}else{
							if (store.getStock() < (row.getQuantity() * row.getProduct().getConversion(row.getUm()))){
								return new GECOError(GECOParameter.ERROR_EXPIREDDATE,"L'articolo "+row.getProductcode()+" ha giacenza inseriore allo scarico");
							}
						}
					}else{
						Storage store = new StoreDao().getStorageProduct(row.getProduct());
						Row oldrow = new DocumentDao().getSingleRow(row.getIdRow());
						/**/
						if (row.getSerialnumber() != null && row.getSerialnumber().equals("") == false){
							for (Iterator<StorageSerialCode> its = store.getStoragesc().iterator(); its.hasNext();){
								StorageSerialCode ssc = its.next();
								if (ssc.getSerialcode().equals(row.getSerialnumber())){
									if (ssc.getStock() < (row.getQuantity() * row.getProduct().getConversion(row.getUm())) - (oldrow.getQuantity() * oldrow.getProduct().getConversion(oldrow.getUm()))){
										return new GECOError(GECOParameter.ERROR_EXPIREDDATE,"L'articolo "+row.getProductcode()+" lotto:"+row.getSerialnumber()+" ha giacenza inseriore allo scarico");
									}
								}
							}
						}else{
							if (store.getStock() < ((row.getQuantity() * row.getProduct().getConversion(row.getUm())) - (oldrow.getQuantity() * oldrow.getProduct().getConversion(oldrow.getUm())) )){
								return new GECOError(GECOParameter.ERROR_EXPIREDDATE,"L'articolo "+row.getProductcode()+" ha giacenza inseriore allo scarico");
							}
						}
					}
				}
				
			}
		}
		return new GECOSuccess(true);
	}
	public GECOObject createConadTrack(ServletContext context,HeadFilter filter){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Head> list = new ArrayList<Head>();
		try{
			Criteria cr = session.createCriteria(TblHead.class,"head");
			cr.setFirstResult(filter.startelement);
			cr.setMaxResults(filter.pageSize);
			setCriteriaHead(cr, filter);
			List<TblHead> heads = cr.list();
			if (heads.size() > 0){
				for (Iterator<TblHead> iterator = heads.iterator(); iterator.hasNext();){
					TblHead tblhead = iterator.next();
					Head head = new Head();
					head.convertFromTableSingle(tblhead);
					list.add(head);
				}
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		BufferedWriter writer = null;
        try {
            //create a temporary file
            File logFile = new File(context.getRealPath("report/CONAD.txt"));

            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            for (int h =0;h< list.size();h++){
            	Head headToTrack = list.get(h);
            	int index = h+1;
            	writer.write("01"+DataUtilConverter.addSpaceLeft(String.valueOf(index),5,"0")+DataUtilConverter.addSpaceLeft("",10)+DataUtilConverter.addSpaceLeft(String.valueOf(headToTrack.getNumber()),6,"0")+DataUtilConverter.dateToTrack(headToTrack.getDate())+DataUtilConverter.addSpaceLeft("",25)+DataUtilConverter.addSpaceLeft(new RegistryDao().getCompany().getInvoicecode(),6,"0")+DataUtilConverter.addSpaceLeft("",9)+DataUtilConverter.addSpaceLeft(headToTrack.getCustomer().getCustomercode().substring(1),6,"0")+DataUtilConverter.addSpaceLeft("",9)+DataUtilConverter.addSpaceLeft(headToTrack.getDestination().getDestinationcode(),6,"0")+DataUtilConverter.addSpaceLeft("",36)+"\r\n");
            	for (Iterator<Row> itrow = headToTrack.getRows().iterator();itrow.hasNext();){
            		Row row = itrow.next();
            		String type = "1";
            		if (row.getType().equals("V") == false)
            			type = "3";
            		writer.write("02"+DataUtilConverter.addSpaceLeft(String.valueOf(index),5,"0")+DataUtilConverter.addSpaceLeft("",10)+DataUtilConverter.addSpaceRight(row.getProductcode(),5)+DataUtilConverter.addSpaceRight(row.getProductdescription(),30)+DataUtilConverter.addSpaceRight(row.getProductum(),2)+" "+DataUtilConverter.numberToTrack(String.valueOf(row.getQuantity()))+DataUtilConverter.addSpaceRight("",26)+type+DataUtilConverter.addSpaceLeft("",38)+"\r\n");
            	}
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
		return new GECOSuccess ("/GeCoServices/report/CONAD.txt");
	
	}
}
