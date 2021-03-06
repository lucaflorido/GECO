var gecoProductRegistryControllers = angular.module("geco.registry.product",[]);



gecoProductRegistryControllers.controller('ProductListCtrl',["$scope","$rootScope","$http","ScopeFactory","$location",'LoginFactory',function($scope,$rootScope,$http,ScopeFactory,$location,LoginFactory){
    $("#maincontainer_productlist").focus();
	$scope.loginuser = LoginFactory.checklogin();
	
	$(document).unbind("keydown");
	$(document).keydown(function(e){
		if(e.altKey && e.keyCode == 78 ){
			$location.path("/product/0");
			$scope.$apply();
		}
		if(e.altKey && e.keyCode == 80 ){
		    $scope.printElements();
			$scope.$apply();
	   }
	});
	
	if ($scope.pagesize == null ){
		$scope.pagesize = 99;
		
	}else{
		//$scope.pagesize = ScopeFactory.factory.productList.pagesize
	}
	$scope.pageArray = [];
	if ($scope.showFilter == null){
		$scope.showFilter = false;
	}
	if ($scope.filter == null){
		$scope.filter = {"pagefilter":{}};
		$scope.currentGroup = {};
		$scope.currentBrand = {};
		$scope.currentCategory = {};
		$scope.currentSubCategory = {};
		$scope.currentSupplier = {};
	}else{
		$scope.currentGroup = $scope.filter.group;
		$scope.currentBrand = $scope.filter.brand;
		$scope.currentCategory = $scope.filter.category;
		$scope.currentSubCategory = $scope.filter.subcategory;
		$scope.currentSupplier = $scope.filter.supplier;
	}
	$scope.products = [];
	$scope.updateProd = {};
	$http.get('rest/basic/categoryproduct').success(function(data){
		$scope.categorys= data;
		for (var i=0;i<$scope.categorys.length;i++){
			if ($scope.currentCategory.idCategoryProduct == $scope.categorys[i].idCategoryProduct){
				$scope.currentCategory = $scope.categorys[i];
				$scope.subcategories = $scope.currentCategory.subcategories		
				if ($scope.filter.subcategory != null){
					for (var y=0;y<$scope.subcategories.length;y++){
						if ($scope.currentSubCategory.idSubCategoryProduct == $scope.subcategories[i].idSubCategoryProduct){
							$scope.currentSubCategory = $scope.subcategories[i];
						}
					}
				}else{
					$scope.currentSubCategory = null;
				}
			}
		}
	});
	
	$http.get('rest/basic/brand').success(function(data){
		$scope.brands= data;
		for (var i=0;i<$scope.brands.length;i++){
			if ($scope.currentBrand.idBrand == $scope.brands[i].idBrand){
				$scope.currentBrand = $scope.brands[i];
			}
		}
	});
	$http.get('rest/registry/supplier').success(function(data){
		$scope.suppliers= data;
		for (var i=0;i<$scope.suppliers.length;i++){
			if ($scope.currentSupplier.idSupplier == $scope.suppliers[i].idSupplier){
				$scope.currentSupplier = $scope.suppliers[i];
			}
		}
	});	
	$http.get('rest/basic/groupproduct').success(function(data){
		$scope.groups= data;
		for (var i=0;i<$scope.groups.length;i++){
			if ($scope.currentGroup.idGroupProduct == $scope.groups[i].idGroupProduct){
				$scope.currentGroup = $scope.groups[i];
			}
		}
	});
	
	$scope.getProducts = function(page){
			$(".pag").removeClass("selected");
			$("#pag"+page).addClass("selected");
			$scope.filter.pagefilter.startelement = (page - 1 ) * $scope.pagesize;
			$scope.filter.pagefilter.pageSize = $scope.pagesize;
			$rootScope.filter = $scope.filter
			$.ajax({
				url:"rest/registry/products",
				type:"POST",
				data:"filter="+JSON.stringify($scope.filter),
				success:function(data){
					result = JSON.parse(data);
					if (result.type == "success"){	
						$scope.products = result.success;
						$scope.$apply();
						$("#maincontainer_productlist").focus();
					}else{
						alert("Errore: "+result.errorName+" Messaggio:"+result.errorMessage);
					}	
					
				}
				});
	}
	
	$scope.getProductsNumber = function(){
		$rootScope.pagesize = $scope.pagesize;
		if ($scope.products.length != $scope.pagesize){
		$scope.pages = [];
		$scope.pageArray = [];
			$.ajax({
				url:"rest/registry/product/pages/"+$scope.pagesize,
				type:"GET",
				success:function(data){
						$scope.pages= JSON.parse(data);
						for (var i=0;i<$scope.pages;i++){
							$scope.pageArray.push(i+1);
						}
						$scope.$apply();
						$scope.getProducts(1);
						$("#maincontainer_productlist").focus();
					}
				
						
				})
		}
	}
	$scope.getProductsNumber();
	
	$scope.deleteElement = function(id){
		for(var i=0;i<$scope.products.length;i++){
			if (id == $scope.products[i].idProduct){
				$scope.deleteproduct = $scope.products[i];
				$.ajax({
						url:"rest/registry/product/",
						type:"DELETE",
						data:"productobj="+JSON.stringify($scope.deleteproduct),
						success:function(data){
							alert("Utente eliminato con successo");
							$http.get('rest/registry/product').success(function(data){
								$scope.products= data;
							});
						}	
					})
			}	
		}
	}
	$scope.printElements = function(){
		$.ajax({
						url:"rest/print/products/",
						type:"POST",
						data:"filter="+JSON.stringify($scope.filter),
						success:function(data){
							//alert("Utente eliminato con successo");
							window.open(JSON.parse(data), '_blank');
						}	
					})
	}
	$scope.openIncrement = function(){
		if ($scope.showIncrement == true){
			$scope.showIncrement = false;
			$scope.showFilter = false;
		}else{
			$scope.showIncrement = true;
			$scope.showFilter = true;
		}
		$rootScope.showFilter = $scope.showFilter;
		$rootScope.showIncrement = $scope.showIncrement;
	}
	$scope.incrementPrices = function(){
		$.ajax({
				url:"rest/registry/product/increment/",
				type:"POST",
				data:"filter="+JSON.stringify($scope.filter),
				success:function(data){
					result = JSON.parse(data);
					if (result.type == "success"){	
						
						$scope.getProductsNumber();
					}else{
						alert("Errore: "+result.errorName+" Messaggio:"+result.errorMessage);
					}	
				}	
			});
	}
	$scope.changeCategory = function(){
		$scope.filter.category = $scope.currentCategory;
		$scope.subcategories = $scope.currentCategory.subcategories		
		$scope.currentSubCategory = null;
	}
}]);
gecoProductRegistryControllers.controller('ProductDetailCtrl',["$scope","$http","$routeParams","$location","$rootScope",'LoginFactory',function($scope,$http,$routeParams,$location,$rootScope,LoginFactory){
    LoginFactory.checklogin();
	GECO_validator.startupvalidator();
	$("#maincontainer_productdetail").focus();
	$scope.currentBrand = {};
	$scope.currentSupplier = {};
	$scope.currentGroup = {};
	$scope.currentTaxRate = {};
	
	$(document).unbind("keydown");
	$(document).keydown(function(e){
		if(e.altKey && e.keyCode == 81 ){
		    $scope.saveProduct();
			$scope.$apply();
		}
		if(e.altKey && e.keyCode == 78 ){
			$location.path("/product/0");
			$scope.$apply();
		}
		if(e.altKey && e.keyCode == 69 ){
			$location.path("/product");
			$scope.$apply();
		}
		if(e.altKey && e.keyCode == 85 ){
			$scope.product.ums.push({idUnitMeasureProduct:0});
			$scope.umpid = $scope.product.ums.length-1;
			$scope.$apply();
		}
	});
	$scope.idproduct= $routeParams.idproduct;
	$scope.isNew = false
	if ($scope.idproduct == 0){
		$scope.isNew = true;
		$scope.umpid = 0;
	}else{
		$scope.isNew = false;
	}
	
	$http.get('rest/basic/unitmeasure').success(function(data){
		$scope.ums= data;
		
			
			
			
				
					$http.get('rest/registry/product/'+$scope.idproduct).success(function(data){
						$scope.product= data;
						if ($rootScope.newProductToAdd != null)
							$scope.product.code = $rootScope.newProductToAdd;
						if ($scope.product.idProduct == 0){
							$scope.product.ums = [];
							$scope.product.ums.push({idUnitMeasureProduct:0,preference:true,um:$scope.ums[0],conversion:1});
						}
						$http.get('rest/basic/groupproduct').success(function(data){
							$scope.groups= data;
								if($scope.product.group){
									for (var i=0;i<$scope.groups.length;i++){
										if ($scope.product.group.idGroupProduct == $scope.groups[i].idGroupProduct){
											$scope.currentGroup = $scope.groups[i]; 
										}
									}
								}
						});
						$http.get('rest/registry/supplier').success(function(data){
							$scope.suppliers= data;
							if($scope.product.supplier != null){
								for (var i=0;i<$scope.suppliers.length;i++){
									if ($scope.product.supplier.idSupplier == $scope.suppliers[i].idSupplier){
										$scope.currentSupplier = $scope.suppliers[i]; 
									}
									
								}
							}
						});
						$http.get('rest/basic/taxrate').success(function(data){
				$scope.taxrates= data;
						for (var itx=0;itx<$scope.taxrates.length;itx++){
							if($scope.product.taxrate){
								if ($scope.product.taxrate.idtaxrate == $scope.taxrates[itx].idtaxrate){
									$scope.currentTaxRate = $scope.taxrates[itx]; 
								}
							}
						}
						});
						$http.get('rest/basic/categoryproduct').success(function(data){
							$scope.categorys= data;
							if($scope.product.category){
								for (var ig=0;ig<$scope.categorys.length;ig++){
									if ($scope.product.category.idCategoryProduct == $scope.categorys[ig].idCategoryProduct){
										$scope.currentCategory = $scope.categorys[ig];
										$scope.subcategories = $scope.currentCategory.subcategories		
										$scope.currentSubCategory = null;
									}
								}
								for (var igs=0;igs<$scope.subcategories.length;igs++){
									if ($scope.product.subcategory == null){
										$scope.product.subcategory = {};
									}
									if ($scope.product.subcategory.idSubCategoryProduct == $scope.subcategories[igs].idSubCategoryProduct){
										$scope.currentSubCategory = $scope.subcategories[igs]; 
									}
								}
							}
						});
						$http.get('rest/basic/brand').success(function(data){
								$scope.brands= data;
								for (var itx=0;itx<$scope.brands.length;itx++){
									if ($scope.product != null && $scope.product.brand != null && $scope.brands != null){
										if ($scope.product.brand.idBrand == $scope.brands[itx].idBrand){
											$scope.currentBrand = $scope.brands[itx]; 
										}
									}
								}
								//$("#selbrand").trigger("chosen:updated");
						});
						//$(".combobox").trigger("chosen:updated");
					
					
				
				
			
		});
	});
	$scope.changeCategory = function(){
		$scope.subcategories = $scope.currentCategory.subcategories		
		$scope.currentSubCategory = null;
	}
	$scope.addUMElement = function(product){
		product.ums.push({idUnitMeasureProduct:0});
		$scope.umpid = product.ums.length-1 ;
	}
	$scope.changeUMElement = function(ump){
	    
		/*if (ump.um != null){
			for(var i=0;i<$scope.ums.length;i++){
				if (ump.um.idUnitMeasure == $scope.ums[i].idUnitMeasure){
					$scope.currentUMP = $scope.ums[i];
				}
			}
		}*/
		$scope.umpid = ump; //ump.idUnitMeasureProduct
	}
	
	$scope.saveProduct = function(){
		$scope.product.group = $scope.currentGroup;
		$scope.product.taxrate = $scope.currentTaxRate;
		$scope.product.category = $scope.currentCategory;
		$scope.product.brand = $scope.currentBrand;
		$scope.product.subcategory = $scope.currentSubCategory;
		$scope.product.supplier = $scope.currentSupplier;
	//if (GECO_validator.requiredFields()== true && GECO_validator.emailFields()==true){
			$.ajax({
				url:"rest/registry/product",
				type:"PUT",
				data:"products="+JSON.stringify($scope.product),
				success:function(data){
					
					result = JSON.parse(data);
					if (result.type == "success"){	
						$scope.product.idProduct = result.success;
						$scope.idproduct = result.success;
						
						$scope.$apply();
						if ($rootScope.headScope != null){
							$rootScope.newProductToAdd = $scope.product.code;
							$scope.isNew = false;
							$scope.confirmSaved();
							window.history.back();
						}else{
							$scope.isNew = false;
							$http.get('rest/registry/product/'+$scope.idproduct).success(function(data){
								$scope.product.idProduct= data.idProduct;
								$location.path("/product/"+$scope.product.idProduct);
							});
							$scope.confirmSaved();
						}
						
					}else{
						alert("Errore: "+result.errorName+" Messaggio:"+result.errorMessage);
					}	
				}	
			})
		//}
	} ;
	$rootScope.setSaveControl($scope.saveProduct);
	$scope.newProduct = function(){
		$location.path("/product/0");
	} 
	$scope.mainPage = function(){
		$location.path("/product");
	} 
	$scope.deleteProductUM = function(um){
		$.ajax({
						url:"rest/registry/productum/",
						type:"DELETE",
						data:"productobj="+JSON.stringify(um),
						success:function(data){
							$http.get('rest/registry/product/'+$scope.idproduct).success(function(data){
								$scope.product= data;
							});
						}	
					})
		
	}
	$scope.calculatePrices = function(type){
		$scope.basicPrices = {};
		$scope.basicPrices.sellprice = $scope.product.sellprice;
		$scope.basicPrices.percentage = $scope.product.percentage;
		$scope.basicPrices.purchaseprice = $scope.product.purchaseprice;
		$.ajax({
				url:"rest/util/prodbasicprice/"+type,
				type:"POST",
				data:"prices="+JSON.stringify($scope.basicPrices),
				success:function(data){
					
					result = JSON.parse(data);
					if (result.type == "success"){	
						$scope.product.sellprice = result.success.sellprice;
						$scope.product.percentage = result.success.percentage;
						$scope.product.purchaseprice = result.success.purchaseprice;
						$scope.$apply();
					}else{
						alert("Errore: "+result.errorName+" Messaggio:"+result.errorMessage);
					}	
				}	
			});
	}
}]);
