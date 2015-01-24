var gecoRegistrySupplierControllers = angular.module("geco.registry.supplier",[]);


/****
	SUPPLIER
**/
gecoProductRegistryControllers.controller('SupplierListCtrl',["$scope","$http","$rootScope",'LoginFactory','KeyDown',function($scope,$http,$rootScope,LoginFactory,KeyDown){
    LoginFactory.checklogin();
	KeyDown.setupList("/supplier/0",$scope.printElements);
	if ($scope.pagesize == null ){
		$scope.pagesize = 99;
		//ScopeFactory.factory.productList.pagesize = 100;
	}else{
		//$scope.pagesize = ScopeFactory.factory.productList.pagesize
	}
	$scope.pageArray = [];
	if ($scope.showFilter == null){
		$scope.showFilter = false;
	}
	if ($scope.filterSupplier == null){
		$scope.filterSupplier = {"pagefilter":{}};
		$scope.currentGroup = {};
		$scope.currentBrand = {};
		$scope.currentCategory = {};
		$scope.currentSubCategory = {};
		$scope.currentSupplier = {};
	}else{
		$scope.currentGroup = $scope.filterSupplier.group;
		$scope.currentBrand = $scope.filterSupplier.brand;
		$scope.currentCategory = $scope.filterSupplier.category;
		$scope.currentSubCategory = $scope.filterSupplier.subcategory;
		$scope.currentSupplier = $scope.filterSupplier.supplier;
	}
	$http.get('rest/basic/groupsupplier').success(function(data){
		$scope.groups= data;
		for (var i=0;i<$scope.groups.length;i++){
			if ($scope.currentGroup.idGroupSupplier == $scope.groups[i].idGroupSupplier){
				$scope.currentGroup = $scope.groups[i];
			}
		}
	});
	$http.get('rest/basic/categorysupplier').success(function(data){
		$scope.categorys= data;
		for (var i=0;i<$scope.categorys.length;i++){
			if ($scope.currentCategory.idCategorySupplier == $scope.categorys[i].idCategorySupplier){
				$scope.currentCategory = $scope.categorys[i];
			}
		}
	});
	$scope.getSuppliers = function(){
		$scope.filterSupplier.pagefilter.startelement = (1 - 1 ) * $scope.pagesize;
			$scope.filterSupplier.pagefilter.pageSize = $scope.pagesize;
			$rootScope.filterSupplier = $scope.filterSupplier
		$.ajax({
			url:"rest/registry/supplier/",
			type:"POST",
			data:"filter="+JSON.stringify($scope.filterSupplier),
			success:function(data){
				
				$scope.suppliers= JSON.parse(data);
				
				$scope.$apply();
			}	
		})
	}
	$scope.getSuppliers();
	
	
	/*$http.get('rest/registry/supplier').success(function(data){
		$scope.suppliers= data;
		
	});*/
	
	$scope.deleteElement = function(id){
		for(var i=0;i<$scope.suppliers.length;i++){
			if (id == $scope.suppliers[i].idSupplier){
				$scope.deletesupplier = $scope.suppliers[i];
				$.ajax({
						url:"rest/registry/supplier/",
						type:"DELETE",
						data:"supplierobj="+JSON.stringify($scope.deletesupplier),
						success:function(data){
							alert("Utente eliminato con successo");
							$http.get('rest/registry/supplier').success(function(data){
								$scope.suppliers= data;
							});
						}	
					})
			}	
		}
	}
	$scope.printElements = function(){
		$http.get('rest/print').success(function(data){
					window.open(data, '_blank');
		});
	}
	
}]);
gecoProductRegistryControllers.controller('SupplierDetailCtrl',["$scope","$http","$routeParams",'LoginFactory','KeyDown',function($scope,$http,$routeParams,LoginFactory,KeyDown){
    LoginFactory.checklogin();
	$scope.listsections = ["Dettagli","Listini","Destinazioni"];
	$scope.selectedSection = $scope.listsections[0];
	GECO_validator.startupvalidator();
	$scope.idsupplier= $routeParams.idsupplier;
	
	$http.get('rest/basic/payment').success(function(data){
		$scope.payments= data;
		if ($scope.supplier != null && $scope.supplier.payment != null){
			for(var i=0;i<$scope.payments.length;i++ ){
				if ($scope.supplier.payment.idPayment == $scope.payments[i].idPayment){
					$scope.currentPayment = $scope.payments[i];
				}
			}
		}
	});
	$http.get('rest/basic/groupsupplier').success(function(data){
			$scope.groups= data;
	});
	$http.get('rest/registry/list').success(function(data){
		$scope.lists= data;
	});
	$http.get('rest/registry/supplier/'+$scope.idsupplier).success(function(data){
		$scope.supplier= data;
		if ($scope.payments != null && $scope.supplier.payment != null){
			for(var i=0;i<$scope.payments.length;i++ ){
				if ($scope.supplier.payment.idPayment == $scope.payments[i].idPayment){
					$scope.currentPayment = $scope.payments[i];
				}
			}
		}
		for (var i=0;i<$scope.groups.length;i++){
					if ($scope.supplier.group != null){
						if ($scope.supplier.group.idGroupSupplier == $scope.groups[i].idGroupSupplier){
							$scope.currentGroup = $scope.groups[i]; 
						}
					}else{
						$scope.supplier.group = {}
					}
				}
				$http.get('rest/basic/categorysupplier').success(function(data){
					$scope.categorys= data;
					for (var ig=0;ig<$scope.categorys.length;ig++){
						if ($scope.supplier.category != null){
							if ($scope.supplier.category.idCategorySupplier == $scope.categorys[ig].idCategorySupplier){
								$scope.currentCategory = $scope.categorys[ig];
							}
						}else{
							$scope.supplier.category = {};
						}
					}
				});
				
	});
	$scope.getListName = function(list){
		return list.code + ' '+ list.description + ' '+ list.startdate; 
	}
	$scope.addListElement = function(supplier){
		supplier.lists.push({idListSupplier:0});
	}
	$scope.changeListElement = function(list){
	    
		if (list.list != null){
			for(var i=0;i<$scope.lists.length;i++){
				if (list.list.idListMeasure == $scope.lists[i].idListMeasure){
					$scope.currentList = $scope.lists[i];
				}
			}
		}
		$scope.listid = list.idListSupplier
	}
	$scope.saveSupplier = function(){
		$scope.supplier.group = $scope.currentGroup;
		$scope.supplier.category = $scope.currentCategory;
	//if (GECO_validator.requiredFields()== true && GECO_validator.emailFields()==true){
			$.ajax({
				url:"rest/registry/supplier",
				type:"PUT",
				data:"suppliers="+JSON.stringify($scope.supplier),
				success:function(data){
					result = JSON.parse(data);
					if (result.type == "success"){	
						$scope.supplier.idSupplier = result.success;
						$scope.idsupplier = result.success;
						alert("success");
						$scope.$apply();
					}else{
						alert("Errore: "+result.errorName+" Messaggio:"+result.errorMessage);
					}	
					
				}	
			})
		//}
	} ;
	KeyDown.setupDetail("/supplier/0",$scope.saveSupplier,"/supplier");
	$(document).keydown(function(e){
		//ALT +1
		if(e.altKey && e.keyCode == 49 ){
			$scope.selectedSection = $scope.listsections[0];
			$scope.$apply();
		}
		//ALT +2
		if(e.altKey && e.keyCode == 50 ){
			$scope.selectedSection = $scope.listsections[1];
			$scope.$apply();
		}
		//ALT +3
		if(e.altKey && e.keyCode == 51 ){
			$scope.selectedSection = $scope.listsections[2];
			$scope.$apply();
		}
	});
}]);
