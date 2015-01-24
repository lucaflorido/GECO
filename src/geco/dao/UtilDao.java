package geco.dao;

import geco.properties.GECOParameter;
import geco.vo.GECOError;
import geco.vo.GECOObject;
import geco.vo.GECOSuccess;
import geco.vo.helpobject.ProductBasicPricesCalculation;

public class UtilDao {
	public GECOObject calculateBasicPricesProduct(ProductBasicPricesCalculation basic,boolean isSellPrice){
		try{
			if (isSellPrice == false){
				basic.calculatePercentage();
			}else{
				basic.calculateSellPrice();
			}
		}catch(Exception e){
			new GECOError(GECOParameter.ERROR_CALCULATION, e.getMessage());
		}
		return new GECOSuccess(basic);
	}
}
