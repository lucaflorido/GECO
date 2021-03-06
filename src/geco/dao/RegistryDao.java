package geco.dao;

import geco.hibernate.DataUtilConverter;
import geco.hibernate.HibernateUtils;
import geco.pojo.TblCompany;
import geco.pojo.TblBank;
import geco.pojo.TblCustomer;
import geco.pojo.TblDestination;
import geco.pojo.TblHead;
import geco.pojo.TblList;
import geco.pojo.TblListCustomer;
import geco.pojo.TblListProduct;
import geco.pojo.TblProduct;
import geco.pojo.TblRow;
import geco.pojo.TblSupplier;
import geco.pojo.TblTransporter;
import geco.pojo.TblUnitMeasureProduct;
import geco.properties.GECOParameter;
import geco.vo.Address;
import geco.vo.BankContact;
import geco.vo.CategoryCustomer;
import geco.vo.Company;
import geco.vo.Bank;
import geco.vo.Contact;
import geco.vo.Customer;
import geco.vo.Destination;
import geco.vo.GECOError;
import geco.vo.GECOObject;
import geco.vo.GECOSuccess;
import geco.vo.GroupCustomer;
import geco.vo.Head;
import geco.vo.ListCustomer;
import geco.vo.NewList;
import geco.vo.Product;
import geco.vo.ProductDatePrice;
import geco.vo.Supplier;
import geco.vo.Transporter;
import geco.vo.UnitMeasure;
import geco.vo.UnitMeasureProduct;
import geco.vo.filter.HeadFilter;
import geco.vo.filter.PagesFilter;
import geco.vo.filter.customer.SelectCustomerList;
import geco.vo.filter.product.SelectProductsFilter;
import geco.vo.filter.supplier.SelectSupplierList;
import geco.vo.helpobject.ProductBarCode;
import geco.vo.helpobject.ProductBasicPricesCalculation;

import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;

public class RegistryDao {
	/*****
	 * Get List of Company 
	 */
	public Company getCompany(){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Company> list = new ArrayList<Company>();
		Company company = new Company();
		try{
			Criteria cr = session.createCriteria(TblCompany.class);
			List<TblCompany> companys = cr.list();
			if (companys.size() > 0){
				for (Iterator<TblCompany> iterator = companys.iterator(); iterator.hasNext();){
					TblCompany tblcompany = iterator.next();
					
					company.convertFromTable(tblcompany);
					list.add(company);
				}
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return company;
	}
	/***
	 * Save update Companys
	 * **/
	public GECOObject saveUpdatesCompany(Company sms){
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
				if (sms.control() == null ){
					TblCompany tblsm = new TblCompany();
					tblsm.convertToTable(sms);
					session.saveOrUpdate(tblsm);
				}else{
					if (tx!= null) tx.rollback();
					//session.close();
					return sms.control();
				}
			
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
		return new GECOSuccess();
	}
	/***
	 * DELETE A SINGLE Tblcompany
	 * **/
	public Boolean deleteCompany(Company sm){
		TblCompany tblsm = new TblCompany();
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
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return true;
		
	}
	/*****
	 * Get List of Bank 
	 */
	public ArrayList<Bank> getBankList(){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Bank> list = new ArrayList<Bank>();
		try{
			Criteria cr = session.createCriteria(TblBank.class);
			List<TblBank> banks = cr.list();
			if (banks.size() > 0){
				for (Iterator<TblBank> iterator = banks.iterator(); iterator.hasNext();){
					TblBank tblbank = iterator.next();
					Bank bank = new Bank();
					bank.convertFromTable(tblbank);
					list.add(bank);
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
	/***
	 * Save update Banks
	 * **/
	public GECOObject saveUpdatesBank(Bank sm){
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			if (sm.control() == null ){
					TblBank tblsm = new TblBank();
					tblsm.convertToTable(sm);
					session.saveOrUpdate(tblsm);
			}else{
				if (tx!= null) tx.rollback();
				//session.close();
				return sm.control();
			}
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
		return new GECOSuccess();
	}
	/***
	 * DELETE A SINGLE Tblbank
	 * **/
	public Boolean deleteBank(Bank sm){
		TblBank tblsm = new TblBank();
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
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return true;
		
	}
	/**
	 * GET A SINGLE BANK
	 * **/
	public Bank getSingleBank(int idbank){
		Session session = HibernateUtils.getSessionFactory().openSession();
		Bank bank = new Bank();
		try{
			
			Criteria cr = session.createCriteria(TblBank.class,"bank");
			cr.add(Restrictions.eq("idBank", idbank));
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List banks = cr.list();
			if (banks.size() > 0){
				
				bank.convertFromTable((TblBank)banks.get(0));
				
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
			
		}finally{
			session.close();
		}
		return bank;
	}
	

	public GECOObject getProductList(SelectProductsFilter filter){
		Session session = HibernateUtils.getSessionFactory().openSession();
		TreeSet<Product> list = new TreeSet<Product>();
		GECOObject obj = null;
		try{
			Criteria cr = setProductCriteria(filter, session);
			List<TblProduct> products = cr.list();
			if (products.size() > 0){
				for (Iterator<TblProduct> iterator = products.iterator(); iterator.hasNext();){
					TblProduct tblproduct = iterator.next();
					Product product = new Product();
					product.convertFromTable(tblproduct);
					list.add(product);
				}
			}
			obj = new GECOSuccess(list);
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			obj = new GECOError(GECOParameter.ERROR_HIBERNATE,"Errore nei dati ");
		}finally{
			session.close();
		}
		return obj;
	}
	private Criteria setProductCriteria(SelectProductsFilter filter,Session session){
		Criteria cr = session.createCriteria(TblProduct.class,"product");
		cr.setFirstResult(filter.getPagefilter().startelement);
		cr.setMaxResults(filter.getPagefilter().pageSize);
		if (filter.getBrand() != null){
			cr.add(Restrictions.eq("product.brand.idBrand", filter.getBrand().getIdBrand()));
		}
		if (filter.getCategory() != null){
			cr.add(Restrictions.eq("product.category.idCategoryProduct", filter.getCategory().getIdCategoryProduct()));
		}
		if (filter.getSubcategory() != null){
			cr.add(Restrictions.eq("product.subcategory.idSubCategoryProduct", filter.getSubcategory().getIdSubCategoryProduct()));
		}
		if (filter.getGroup() != null && filter.getGroup().getIdGroupProduct() != 0){
			cr.add(Restrictions.eq("product.group.idGroupProduct", filter.getGroup().getIdGroupProduct()));
		}
		if (filter.getSupplier() != null){
			cr.add(Restrictions.eq("product.supplier.idSupplier", filter.getSupplier().getIdSupplier()));
		}
		if (filter.getSearchstring() != null && filter.getSearchstring().equals("") == false){
			cr.add(Restrictions.disjunction(Restrictions.like("product.code","%"+  filter.getSearchstring()+"%"),Restrictions.like("product.description","%"+ filter.getSearchstring()+"%")));
		}
		cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return cr;
	}
	public ArrayList<Product> getProductList(){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Product> list = new ArrayList<Product>();
		try{
			Criteria cr = session.createCriteria(TblProduct.class);
			List<TblProduct> products = cr.list();
			if (products.size() > 0){
				for (Iterator<TblProduct> iterator = products.iterator(); iterator.hasNext();){
					TblProduct tblproduct = iterator.next();
					Product product = new Product();
					product.convertFromTable(tblproduct);
					list.add(product);
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
	public ArrayList<Product> getProductFilteredList(SelectProductsFilter filter){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Product> list = new ArrayList<Product>();
		try{
			Criteria cr = session.createCriteria(TblProduct.class,"product");
			
			
			
			
			if (filter.getGroup() != null){
				cr.createAlias("product.group", "group");
				cr.add(Restrictions.eq("group.idGroupProduct", filter.getGroup().getIdGroupProduct()));
			}
				
			if (filter.getCategory() != null){
				cr.createAlias("product.category", "category");
				cr.add(Restrictions.eq("category.idCategoryProduct", filter.getCategory().getIdCategoryProduct()));
			}
				
			if (filter.getBrand() != null){
				cr.createAlias("product.brand", "brand");
				cr.add(Restrictions.eq("brand.idBrand", filter.getBrand().getIdBrand()));
			}
				
			if (filter.getSupplier() != null){
				cr.createAlias("product.supplier", "supplier");
				cr.add(Restrictions.eq("supplier.idSupplier", filter.getSupplier().getIdSupplier()));
			}
				
			List<TblProduct> products = cr.list();
			if (products.size() > 0){
				for (Iterator<TblProduct> iterator = products.iterator(); iterator.hasNext();){
					TblProduct tblproduct = iterator.next();
					Product product = new Product();
					product.convertFromTable(tblproduct);
					list.add(product);
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
	public GECOObject calculateIncrementProducts(SelectProductsFilter filter){
		try{
			ArrayList<Product> prods = getProductFilteredList(filter);
			ProductBasicPricesCalculation c = new ProductBasicPricesCalculation();
			for (Iterator<Product> it = prods.iterator();it.hasNext();){
				Product prod = it.next();
				if (filter.isPercentage() == true)
					c.percentageIncrement(filter.getIncrement(), prod);
				else
					c.amountIncrement(filter.getIncrement(), prod);
				this.saveUpdatesProduct(prod);
			}
		}catch (Exception e){
			return new GECOError(GECOParameter.ERROR_CALCULATION, e.getMessage());
		}
		
		return new GECOSuccess(true);
	}
	public int getListPagesNumber(int size){
		int pages = 0;
		Session session = HibernateUtils.getSessionFactory().openSession();
		try{
			Criteria cr = session.createCriteria(TblProduct.class,"prod");
			
			pages = ((Long) cr.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		if (pages > size){
			pages = pages / size;
		}else{
			pages = 0;
		}
		return pages;
	}
	/*****
	 * Search Products 
	 */
	public ArrayList<UnitMeasureProduct> searchProducts(String value){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<UnitMeasureProduct> list = new ArrayList<UnitMeasureProduct>();
		try{
			Criteria cr = session.createCriteria(TblUnitMeasureProduct.class,"um");
			cr.createAlias("um.product", "prod");
			cr.add(Restrictions.disjunction().add(Restrictions.like("um.code", "%" + value + "%")).add(Restrictions.like("prod.description", "%" + value + "%")).add(Restrictions.like("um.realbarcode", "%" + value + "%")));
			List<TblUnitMeasureProduct> products = cr.list();
			if (products.size() > 0){
				for (Iterator<TblUnitMeasureProduct> iterator = products.iterator(); iterator.hasNext();){
					TblUnitMeasureProduct tblproduct = iterator.next();
					UnitMeasureProduct product = new UnitMeasureProduct();
					product.convertFromTableSearch(tblproduct);
					list.add(product);
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
	
	
	/***
	 * Save update Products
	 * **/
	public GECOObject saveUpdatesProduct(Product sm){
		int id=0;
		if (sm.control() == null){
			sm.updateCode();
			Session session = HibernateUtils.getSessionFactory().openSession();
			Transaction tx = null;
			try{
				tx = session.beginTransaction();
				if (sm.getCode() != "" && sm.getCode() != null ){
						TblProduct tblsm = new TblProduct();
						tblsm.convertToTable(sm);
						session.saveOrUpdate(tblsm);
						id=tblsm.getIdProduct();
				}
				tx.commit();
			}catch(HibernateException e){
				System.err.println("ERROR IN LIST!!!!!!");
				if (tx!= null) tx.rollback();
				e.printStackTrace();
				session.close();
				return new GECOError(GECOParameter.ERROR_HIBERNATE,"Errore nel salvataggio dei dati");
			}finally{
				if (session.isOpen() == true)
					session.close();
			}
		}else{
			return sm.control();
		}
		return new GECOSuccess(id);
	}
	
	/***
	 * DELETE A SINGLE Tblproduct
	 * **/
	public Boolean deleteProduct(Product sm){
		TblProduct tblsm = new TblProduct();
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
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return true;
		
	}
	public Boolean deleteUMProduct(UnitMeasureProduct sm){
		TblUnitMeasureProduct tblsm = new TblUnitMeasureProduct();
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
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return true;
		
	}
	/**
	 * GET A SINGLE PRODUCT
	 * **/
	public Product getSingleProduct(int idproduct){
		Session session = HibernateUtils.getSessionFactory().openSession();
		Product product = new Product();
		try{
			
			Criteria cr = session.createCriteria(TblProduct.class,"product");
			cr.add(Restrictions.eq("idProduct", idproduct));
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List products = cr.list();
			if (products.size() > 0){
				
				product.convertFromTable((TblProduct)products.get(0));
				
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
			
		}finally{
			session.close();
		}
		return product;
	}
	/**
	 * GET A SINGLE PRODUCT
	 * **/
	public Product getSingleCodeProduct(String code,int idList,Head head){
		ProductBarCode pbc = getBarCodeProduct(code);
		Session session = HibernateUtils.getSessionFactory().openSession();
		Product product = new Product();
		//SEARCH BY CODE
		try{			
			Criteria cr = session.createCriteria(TblProduct.class,"product");
			cr.createAlias("product.ums", "um");
			cr.add(Restrictions.eq("um.code", pbc.getCode()));
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List products = cr.list();
			if (products.size() == 1){
				product.convertFromTable((TblProduct)products.get(0));
				setPriceHistory(head, product, session);
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
			
		}finally{
			session.close();
		}
		if (product.getIdProduct() > 0){
			product.setListprice(getProductPriceList(product.getIdProduct(), idList));
			product = calculateUM(product, code);
		}
		try{
			product.setStorage(new StoreDao().getStorageProduct(product));
		}catch(Exception e){
			
		}
		product.setWeigth(pbc.getWeight());
		return product;
	}
	/**
	 * GET A SINGLE BARCODE
	 * **/
	private ProductBarCode getBarCodeProduct(String code){
		ProductBarCode  pbc = new ProductBarCode();
		pbc.setCode(code);
		pbc.setBarcode(code);
		pbc.setBarCodeWeight(0);
		Set<Integer> rbcn = getRealBarCodeNumber();
		for (Iterator<Integer> itbc = rbcn.iterator();itbc.hasNext();){
			Integer i = itbc.next();
			String codestr = "";
			if(code.length() > i){
				codestr = code.substring(0,i);
			}
			String rescode = getSingleBarCodeProduct(codestr);
			if (rescode.equals("") == false){
				pbc.setBarCodeWeight(getSingleWeightBarCodeProduct(codestr));
				pbc.setCode(rescode);
				return pbc;
			}
		}
		Set<Integer> wbcs = getBarCodeWeight();
		for (Iterator<Integer> it = wbcs.iterator();it.hasNext();){
			Integer i = it.next();
			String codestr = "";
			if(code.length() > i){
				int l = code.length();
				l = l-1-i;
				codestr = code.substring(0,l);
			}
			String rescode = getSingleBarCodeProduct(codestr);
			if (rescode.equals("") == false){
				pbc.setBarCodeWeight(i);
				pbc.setCode(rescode);
				return pbc;
			}
		}
		return pbc;
	}
	private String getSingleBarCodeProduct(String code){
		String result  = "";
		if (code.equals("") == true){
			return "";
		}
		Session session = HibernateUtils.getSessionFactory().openSession();
		UnitMeasureProduct umproduct = new UnitMeasureProduct();
		//SEARCH BY CODE
		try{			
			Criteria cr = session.createCriteria(TblUnitMeasureProduct.class,"unitmeasure_product");
			cr.add(Restrictions.like("unitmeasure_product.realbarcode", code));
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List umproducts = cr.list();
			if (umproducts.size() == 1){
				umproduct.convertFromTable((TblUnitMeasureProduct)umproducts.get(0));
				result = umproduct.getCode();
				return result;
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
			
		}finally{
			session.close();
		}
		
		return result;
	}
	private int getSingleWeightBarCodeProduct(String code){
		int result  = 0;
		Session session = HibernateUtils.getSessionFactory().openSession();
		UnitMeasureProduct umproduct = new UnitMeasureProduct();
		//SEARCH BY CODE
		try{			
			Criteria cr = session.createCriteria(TblUnitMeasureProduct.class,"unitmeasure_product");
			cr.add(Restrictions.like("unitmeasure_product.realbarcode", code));
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List umproducts = cr.list();
			if (umproducts.size() == 1){
				umproduct.convertFromTable((TblUnitMeasureProduct)umproducts.get(0));
				result = umproduct.getWeightbarcode();
				return result;
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
			
		}finally{
			session.close();
		}
		
		return result;
	}
	private Set<Integer> getBarCodeWeight(){
		Session session = HibernateUtils.getSessionFactory().openSession();
		UnitMeasureProduct umproduct = new UnitMeasureProduct();
		Set<Integer> wbcs = new HashSet<Integer>();
		//SEARCH BY CODE
		try{			
			Criteria cr = session.createCriteria(TblUnitMeasureProduct.class,"unitmeasure_product");
			ProjectionList projList = Projections.projectionList();
		    projList.add(Projections.property("weightbarcode"));
		    //projList.add(Projections.groupProperty("weightbarcode"));
	    	cr.setProjection(projList);
			//cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List umproducts = cr.list();
			for (Iterator<Integer> it = umproducts.iterator();it.hasNext();){
				Integer row = (Integer)it.next();
				wbcs.add(row);
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return wbcs;
	}
	private Set<Integer> getRealBarCodeNumber(){
		Session session = HibernateUtils.getSessionFactory().openSession();
		UnitMeasureProduct umproduct = new UnitMeasureProduct();
		Set<Integer> wbcs = new HashSet<Integer>();
		//SEARCH BY CODE
		try{			
			Criteria cr = session.createCriteria(TblUnitMeasureProduct.class,"unitmeasure_product");
			ProjectionList projList = Projections.projectionList();
		    projList.add(Projections.property("realbarcodenumber"));
		    //projList.add(Projections.groupProperty("weightbarcode"));
	    	cr.setProjection(projList);
			//cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List umproducts = cr.list();
			for (Iterator<Integer> it = umproducts.iterator();it.hasNext();){
				Integer row = (Integer)it.next();
				wbcs.add(row);
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return wbcs;
	}
	private void setPriceHistory(Head head,Product prod,Session session){
		if (head == null){
			return; 
		}
		if (head.getCustomer() == null){
			return;
		}
		if (head.getDate() == null || head.getDate().equals("")){
			return;
		}
		prod.setPricehistory(new HashSet<ProductDatePrice>());
		Date toDate = DataUtilConverter.convertDateFromString(head.getDate());
		Calendar c = Calendar.getInstance(); 
		c.setTime(toDate); 
		c.add(Calendar.MONTH, -3);
		Date fromDate = c.getTime();
		try{			
			Criteria cr = session.createCriteria(TblRow.class,"row");
			cr.createAlias("row.head", "head");
			cr.add(Restrictions.eq("head.customer.idCustomer", head.getCustomer().getIdCustomer()));
			cr.add(Restrictions.eq("head.document.idDocument", head.getDocument().getIdDocument()));
			cr.add(Restrictions.eq("row.product.idProduct", prod.getIdProduct()));
			cr.add(Restrictions.between("head.date", fromDate, toDate));
			//cr.setProjection(Projections.distinct(Projections.property("row.price")));
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List<TblRow> rows = cr.list();
			for (Iterator<TblRow> it = rows.iterator();it.hasNext();){
				TblRow row = it.next();
				ProductDatePrice pdp = new ProductDatePrice(DataUtilConverter.convertStringFromDate(row.getHead().getDate()),HibernateUtils.roundfloat(row.getPrice()));
				prod.getPricehistory().add(pdp);
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}
	}
	private float getProductPriceList(int idProduct,int idList){
		Session session = HibernateUtils.getSessionFactory().openSession();
		float price = 0;
		try{			
			Criteria cr = session.createCriteria(TblListProduct.class,"listproduct");
			cr.createAlias("listproduct.product", "prod");
			cr.createAlias("listproduct.list", "list");
			cr.add(Restrictions.eq("prod.idProduct", idProduct));
			cr.add(Restrictions.eq("list.idList", idList));
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List listproducts = cr.list();
			if (listproducts.size() > 0){
				price = ((TblListProduct)listproducts.get(0)).getPrice();
				//No Price in LIST take the purchaseprice
				
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
			
		}finally{
			session.close();
		}
		return price;
	}
	private Product calculateUM(Product prod,String code){
		for (Iterator<UnitMeasureProduct> iterator = prod.getUms().iterator();iterator.hasNext();){
			UnitMeasureProduct ump = iterator.next();
			if(ump.getCode().equals(code)){
				prod.setUmselected(ump.getUm());
				prod.setConversionrate(ump.getConversion());
				break;
			}
		}
		if (prod.getListprice() == 0){
			prod.setListprice(HibernateUtils.roundfloat(prod.getSellprice()));
		}
		if (prod.getConversionrate() > 0){
			prod.setListprice(HibernateUtils.roundfloat(prod.getListprice() * prod.getConversionrate()));
		}
		return prod;
	}
	/*****
	 * Get List of List 
	 */
	public ArrayList<geco.vo.List> getListList(){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<geco.vo.List> list = new ArrayList<geco.vo.List>();
		try{
			Criteria cr = session.createCriteria(TblList.class,"list");
			cr.add(Restrictions.eq("list.active",true));
			//cr.createAlias("list.listproduct", "listproduct");
			//cr.add(Restrictions.disjunction(Restrictions.isNull("listproduct"),Restrictions.eq("listproduct.active",true)));
			
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List<TblList> lists = cr.list();
			if (lists.size() > 0){
				for (Iterator<TblList> iterator = lists.iterator(); iterator.hasNext();){
					TblList tbllist = iterator.next();
					geco.vo.List listobj = new geco.vo.List();
					listobj.convertFromTable(tbllist);
					list.add(listobj);
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
	/***
	 * Save update Lists
	 * **/
	public GECOObject saveUpdatesList(NewList nlist){
		int id = 0;
		geco.vo.List sm = nlist.getList();
		if (sm.control() == null){
			Date lastDate = getDate(sm.getIdList());
			Session session = HibernateUtils.getSessionFactory().openSession();
			Transaction tx = null;
			tx = session.beginTransaction();
			try{
				if (lastDate != null){
					if (checkEqualDate(sm,lastDate) == true){
						sm.copyProducts(sm, false, nlist.isPercentage(), nlist.getValue());
					}else if (checkAfterDate(sm, lastDate) == true){
						sm.copyProducts(sm, true, nlist.isPercentage(), nlist.getValue());
					}
				}else{
					sm.setActive(true);
				}
				TblList tblsm = new TblList();
				tblsm.convertToTableForSaving(sm);
				session.saveOrUpdate(tblsm);
				id = tblsm.getIdList();
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
			
			return sm.control();
		}
		return new GECOSuccess(id);
	}
	public Date getDate(int idList){
		Session session = HibernateUtils.getSessionFactory().openSession();
		try{
			Criteria cr = session.createCriteria(TblList.class,"list");
			cr.add(Restrictions.eq("list.idList",idList));
			List<TblList> lists = cr.list();
			if (lists.size() > 0){
				return lists.get(0).getStartdate();
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			session.close();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		
		return null;
	}
	public void saveOldList(geco.vo.List list,Session session){
		try{
					TblList tblsm = new TblList();
					tblsm.convertToTableForSaving(list);
					tblsm.setActive(false);
					session.saveOrUpdate(tblsm);
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
		
			e.printStackTrace();
			session.close();
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
	
	}
	public boolean checkAfterDate(geco.vo.List list,Date date){
		return DataUtilConverter.convertDateFromString(list.getStartdate()).after(date);
	}
	public boolean checkEqualDate(geco.vo.List list,Date date){
		return DataUtilConverter.convertDateFromString(list.getStartdate()).equals(date);
	}
	/***
	 * DELETE A SINGLE Tbllist
	 * **/
	public Boolean deleteList(geco.vo.List sm){
		TblList tblsm = new TblList();
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
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return true;
		
	}
	/**
	 * GET A SINGLE BANK
	 * **/
	public geco.vo.List getSingleList(int idlist){
		Session session = HibernateUtils.getSessionFactory().openSession();
		geco.vo.List list = new geco.vo.List();
		try{
			
			Criteria cr = session.createCriteria(TblList.class,"list");
			cr.add(Restrictions.eq("idList", idlist));
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List lists = cr.list();
			if (lists.size() > 0){
				list.convertFromTableSingle((TblList)lists.get(0));
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
	
	/*****
	 * Get List of List for Customer 
	 */
	public ArrayList<ListCustomer> getCustomerPriceList(int idCustomer){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<ListCustomer> list = new ArrayList<ListCustomer>();
		try{
			Criteria cr = session.createCriteria(TblListCustomer.class,"listcustomer");
			cr.add(Restrictions.eq("listcustomer.customer.idCustomer", idCustomer));
			List<TblListCustomer> listcustomers = cr.list();
			if (listcustomers.size() > 0){
				for (Iterator<TblListCustomer> iterator = listcustomers.iterator(); iterator.hasNext();){
					TblListCustomer listc = iterator.next();
					ListCustomer lc = new ListCustomer();
					//destination = getMockDestination();
					lc.convertFromTable(listc);
					list.add(lc);
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
	
	/*****
	 * Get List of Customer 
	 */
	public ArrayList<Customer> getCustomerList(){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Customer> list = new ArrayList<Customer>();
		try{
			Criteria cr = session.createCriteria(TblCustomer.class);
			List<TblCustomer> customers = cr.list();
			if (customers.size() > 0){
				for (Iterator<TblCustomer> iterator = customers.iterator(); iterator.hasNext();){
					TblCustomer tblcustomer = iterator.next();
					Customer customer = new Customer();
					//customer = getMockCustomer();
					customer.convertFromTable(tblcustomer);
					list.add(customer);
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
	/*****
	 * Get List of Customer 
	 */
	public ArrayList<Customer> getCustomerList(SelectCustomerList filter){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Customer> list = new ArrayList<Customer>();
		try{
			Criteria cr = session.createCriteria(TblCustomer.class,"customer");
			if (filter.getGroup() != null)
				cr.add(Restrictions.eq("customer.group.idGroupCustomer", filter.getGroup().getIdGroupCustomer()));
			if (filter.getCategory() != null)
				cr.add(Restrictions.eq("customer.category.idCategoryCustomer", filter.getCategory().getIdCategoryCustomer()));
			cr.setFirstResult(filter.getPagefilter().startelement);
			cr.setMaxResults(filter.getPagefilter().pageSize);
			if (filter.getSearchstring() != null && filter.getSearchstring().equals("") == false){
				cr.add(Restrictions.disjunction(Restrictions.like("customer.customercode","%"+  filter.getSearchstring()+"%"),Restrictions.like("customer.customername","%"+ filter.getSearchstring()+"%")));
			}
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List<TblCustomer> customers = cr.list();
			if (customers.size() > 0){
				for (Iterator<TblCustomer> iterator = customers.iterator(); iterator.hasNext();){
					TblCustomer tblcustomer = iterator.next();
					Customer customer = new Customer();
					//customer = getMockCustomer();
					customer.convertFromTable(tblcustomer);
					list.add(customer);
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
	public ArrayList<Customer> getCustomerListByGroup(GroupCustomer gc){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Customer> list = new ArrayList<Customer>();
		try{
			Criteria cr = session.createCriteria(TblCustomer.class,"customer");
			cr.add(Restrictions.eq("customer.group.idGroupCustomer", gc.getIdGroupCustomer()));
			List<TblCustomer> customers = cr.list();
			if (customers.size() > 0){
				for (Iterator<TblCustomer> iterator = customers.iterator(); iterator.hasNext();){
					TblCustomer tblcustomer = iterator.next();
					Customer customer = new Customer();
					//customer = getMockCustomer();
					customer.convertFromTable(tblcustomer);
					list.add(customer);
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
	/***
	 * Save update Customers
	 * **/
	public GECOObject saveUpdatesCustomer(Customer sm){
		int id=0;
		if (sm.control() == null){
			Session session = HibernateUtils.getSessionFactory().openSession();
			Transaction tx = null;
			
			try{
				tx = session.beginTransaction();
				if (sm.getCustomername() != "" && sm.getCustomername() != null ){
						TblCustomer tblsm = new TblCustomer();
						tblsm.convertToTableSingle(sm, tblsm);
						session.saveOrUpdate(tblsm);
						id=tblsm.getIdCustomer();
				}
				tx.commit();
			}catch(HibernateException e){
				System.err.println("ERROR IN LIST!!!!!!");
				if (tx!= null) tx.rollback();
				e.printStackTrace();
				return new GECOError(GECOParameter.ERROR_HIBERNATE, "Errore nel salvataggio dei dati");
			}finally{
				session.close();
			}
		}else{
			return sm.control();
		}
		return new GECOSuccess(id);
	}
	public GECOObject saveUpdatesCustomer(Customer sm,Session session){
		int id=0;
		if (sm.control() == null){
			
			
			try{
				if (sm.getCustomername() != "" && sm.getCustomername() != null ){
						TblCustomer tblsm = new TblCustomer();
						tblsm.convertToTableSingle(sm, tblsm);
						session.saveOrUpdate(tblsm);
						id=tblsm.getIdCustomer();
				}
			}catch(HibernateException e){
				System.err.println("ERROR IN LIST!!!!!!");
				e.printStackTrace();
				return new GECOError(GECOParameter.ERROR_HIBERNATE, "Errore nel salvataggio dei dati");
			}
		}else{
			return sm.control();
		}
		return new GECOSuccess(id);
	}
	/***
	 * DELETE A SINGLE Tblcustomer
	 * **/
	public Boolean deleteCustomer(Customer sm){
		TblCustomer tblsm = new TblCustomer();
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
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return true;
		
	}
	/**
	 * GET A SINGLE CUSTOMER
	 * **/
	public Customer getSingleCustomer(int idcustomer){
		Session session = HibernateUtils.getSessionFactory().openSession();
		Customer customer = new Customer();
		//customer = getMockCustomer();
		try{
			
			Criteria cr = session.createCriteria(TblCustomer.class,"customer");
			cr.add(Restrictions.eq("idCustomer", idcustomer));
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List customers = cr.list();
			if (customers.size() > 0){
				
				customer.convertFromTableSingle((TblCustomer)customers.get(0));
				
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
			
		}finally{
			session.close();
		}
		
		return customer;
	}
	
	
	/*****
	 * Get List of Destination 
	 */
	public ArrayList<Destination> getDestinationList(){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Destination> list = new ArrayList<Destination>();
		try{
			Criteria cr = session.createCriteria(TblDestination.class);
			List<TblDestination> destinations = cr.list();
			if (destinations.size() > 0){
				for (Iterator<TblDestination> iterator = destinations.iterator(); iterator.hasNext();){
					TblDestination tbldestination = iterator.next();
					Destination destination = new Destination();
					//destination = getMockDestination();
					destination.convertFromTable(tbldestination);
					list.add(destination);
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
	/*****
	 * Get List of Destination for Customer 
	 */
	public ArrayList<Destination> getCustomerDestinationList(int idCustomer){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Destination> list = new ArrayList<Destination>();
		try{
			Criteria cr = session.createCriteria(TblDestination.class,"destination");
			cr.add(Restrictions.eq("destination.customer.idCustomer", idCustomer));
			List<TblDestination> destinations = cr.list();
			if (destinations.size() > 0){
				for (Iterator<TblDestination> iterator = destinations.iterator(); iterator.hasNext();){
					TblDestination tbldestination = iterator.next();
					Destination destination = new Destination();
					//destination = getMockDestination();
					destination.convertFromTable(tbldestination);
					list.add(destination);
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
	/***
	 * Save update Destinations
	 * **/
	public GECOObject saveUpdatesDestination(Destination sm){
		int id=0;
		if(sm.control()==null){
			Session session = HibernateUtils.getSessionFactory().openSession();
			Transaction tx = null;
			
			try{
				tx = session.beginTransaction();
				if (sm.getDestinationname() != "" && sm.getDestinationname() != null ){
						TblDestination tblsm = new TblDestination();
						tblsm.convertToTable(sm);
						session.saveOrUpdate(tblsm);
						id=tblsm.getIdDestination();
				}
				tx.commit();
			}catch(HibernateException e){
				System.err.println("ERROR IN LIST!!!!!!");
				if (tx!= null) tx.rollback();
				e.printStackTrace();
				throw new ExceptionInInitializerError(e);
			}finally{
				session.close();
			}
		}else{
			return sm.control();
		}
		return new GECOSuccess(id);
	}
	/***
	 * DELETE A SINGLE Tbldestination
	 * **/
	public Boolean deleteDestination(Destination sm){
		TblDestination tblsm = new TblDestination();
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
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return true;
		
	}
	/**
	 * GET A SINGLE CUSTOMER
	 * **/
	public Destination getSingleDestination(int iddestination){
		Session session = HibernateUtils.getSessionFactory().openSession();
		Destination destination = new Destination();
		//destination = getMockDestination();
		try{
			
			Criteria cr = session.createCriteria(TblDestination.class,"destination");
			cr.add(Restrictions.eq("idDestination", iddestination));
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List destinations = cr.list();
			if (destinations.size() > 0){
				
				destination.convertFromTable((TblDestination)destinations.get(0));
				
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
			
		}finally{
			session.close();
		}
		
		return destination;
	}
	
	
	/*****
	 * Get List of Supplier 
	 */
	public ArrayList<Supplier> getSupplierList(){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Supplier> list = new ArrayList<Supplier>();
		try{
			Criteria cr = session.createCriteria(TblSupplier.class);
			List<TblSupplier> suppliers = cr.list();
			if (suppliers.size() > 0){
				for (Iterator<TblSupplier> iterator = suppliers.iterator(); iterator.hasNext();){
					TblSupplier tblsupplier = iterator.next();
					Supplier supplier = new Supplier();
					//supplier = getMockSupplier();
					supplier.convertFromTable(tblsupplier);
					list.add(supplier);
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
	public ArrayList<Supplier> getSupplierList(SelectSupplierList filter){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Supplier> list = new ArrayList<Supplier>();
		try{
			Criteria cr = session.createCriteria(TblSupplier.class,"supplier");
			if (filter.getGroup() != null)
				cr.add(Restrictions.eq("supplier.group.idGroupSupplier", filter.getGroup().getIdGroupSupplier()));
			if (filter.getCategory() != null)
				cr.add(Restrictions.eq("supplier.category.idCategorySupplier", filter.getCategory().getIdCategorySupplier()));
			cr.setFirstResult(filter.getPagefilter().startelement);
			cr.setMaxResults(filter.getPagefilter().pageSize);
			if (filter.getSearch() != null && filter.getSearch().equals("") == false){
				cr.add(Restrictions.disjunction(Restrictions.like("supplier.suppliercode","%"+  filter.getSearch()+"%"),Restrictions.like("supplier.suppliername","%"+ filter.getSearch()+"%")));
			}
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List<TblSupplier> suppliers = cr.list();
			if (suppliers.size() > 0){
				for (Iterator<TblSupplier> iterator = suppliers.iterator(); iterator.hasNext();){
					TblSupplier tblsupplier = iterator.next();
					Supplier supplier = new Supplier();
					//supplier = getMockSupplier();
					supplier.convertFromTable(tblsupplier);
					list.add(supplier);
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
	/***
	 * Save update Suppliers
	 * **/
	public GECOObject saveUpdatesSupplier(Supplier sm){
		int id=0;
		if (sm.control() == null){
			Session session = HibernateUtils.getSessionFactory().openSession();
			Transaction tx = null;
			
			try{
				tx = session.beginTransaction();
				if (sm.getSuppliername() != "" && sm.getSuppliername() != null ){
						TblSupplier tblsm = new TblSupplier();
						tblsm.convertToTableSingle(sm, tblsm);
						session.saveOrUpdate(tblsm);
						id=tblsm.getIdSupplier();
				}
				tx.commit();
			}catch(HibernateException e){
				System.err.println("ERROR IN LIST!!!!!!");
				if (tx!= null) tx.rollback();
				e.printStackTrace();
				return new GECOError(GECOParameter.ERROR_HIBERNATE, "Errore nel salvataggio dei dati");
			}finally{
				session.close();
			}
		}else{
			return sm.control();
		}
		return new GECOSuccess(id);
	}
	/***
	 * DELETE A SINGLE Tblsupplier
	 * **/
	public Boolean deleteSupplier(Supplier sm){
		TblSupplier tblsm = new TblSupplier();
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
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return true;
		
	}
	/**
	 * GET A SINGLE CUSTOMER
	 * **/
	public Supplier getSingleSupplier(int idsupplier){
		Session session = HibernateUtils.getSessionFactory().openSession();
		Supplier supplier = new Supplier();
		//supplier = getMockSupplier();
		try{
			
			Criteria cr = session.createCriteria(TblSupplier.class,"supplier");
			cr.add(Restrictions.eq("idSupplier", idsupplier));
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List suppliers = cr.list();
			if (suppliers.size() > 0){
				
				supplier.convertFromTableSingle((TblSupplier)suppliers.get(0));
				
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
			
		}finally{
			session.close();
		}
		
		return supplier;
	}
	
	
	

	/*****
	 * Get List of Transporter 
	 */
	public ArrayList<Transporter> getTransporterList(){
		Session session = HibernateUtils.getSessionFactory().openSession();
		ArrayList<Transporter> list = new ArrayList<Transporter>();
		try{
			Criteria cr = session.createCriteria(TblTransporter.class);
			List<TblTransporter> transporters = cr.list();
			if (transporters.size() > 0){
				for (Iterator<TblTransporter> iterator = transporters.iterator(); iterator.hasNext();){
					TblTransporter tbltransporter = iterator.next();
					Transporter transporter = new Transporter();
					//transporter = getMockTransporter();
					transporter.convertFromTable(tbltransporter);
					list.add(transporter);
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
	/***
	 * Save update Transporters
	 * **/
	public GECOObject saveUpdatesTransporter(Transporter sm){
		int id=0;
		if (sm.control() == null){
			Session session = HibernateUtils.getSessionFactory().openSession();
			Transaction tx = null;
			
			try{
				tx = session.beginTransaction();
				if (sm.getTransportername() != "" && sm.getTransportername() != null ){
						TblTransporter tblsm = new TblTransporter();
						tblsm.convertToTable(sm);
						session.saveOrUpdate(tblsm);
						id=tblsm.getIdTransporter();
				}
				tx.commit();
			}catch(HibernateException e){
				System.err.println("ERROR IN LIST!!!!!!");
				if (tx!= null) tx.rollback();
				e.printStackTrace();
				throw new ExceptionInInitializerError(e);
			}finally{
				session.close();
			}
		}else{
			return sm.control();
		}
		return new GECOSuccess(id);
	}
	/***
	 * DELETE A SINGLE Tbltransporter
	 * **/
	public Boolean deleteTransporter(Transporter sm){
		TblTransporter tblsm = new TblTransporter();
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
			throw new ExceptionInInitializerError(e);
		}finally{
			session.close();
		}
		return true;
		
	}
	/**
	 * GET A SINGLE CUSTOMER
	 * **/
	public Transporter getSingleTransporter(int idtransporter){
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transporter transporter = new Transporter();
		//transporter = getMockTransporter();
		try{
			
			Criteria cr = session.createCriteria(TblTransporter.class,"transporter");
			cr.add(Restrictions.eq("idTransporter", idtransporter));
			cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List transporters = cr.list();
			if (transporters.size() > 0){
				transporter.convertFromTable((TblTransporter)transporters.get(0));
			}
		}catch(HibernateException e){
			System.err.println("ERROR IN LIST!!!!!!");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
			
		}finally{
			session.close();
		}
		
		return transporter;
	}
}
