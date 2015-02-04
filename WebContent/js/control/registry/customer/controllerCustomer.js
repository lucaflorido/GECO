var gecoRegistryCustomerControllers = angular.module("geco.registry.customer",[]);



/****
	CUSTOMER
**/
gecoRegistryCustomerControllers.controller('CustomerListCtrl',["$scope","$http","$rootScope","KeyDown",'LoginFactory',function($scope,$http,$rootScope,KeyDown,LoginFactory){
     LoginFactory.checklogin();
	KeyDown.setupList("/customer/0",$scope.printElements);
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
	if ($scope.filterCustomer == null){
		$scope.filterCustomer = {"pagefilter":{}};
		$scope.currentCustomerGroup = {};
		$scope.currentBrand = {};
		$scope.currentCategory = {};
		$scope.currentSubCategory = {};
		$scope.currentSupplier = {};
	}else{
		$scope.currentCustomerGroup = $scope.filterCustomer.group;
		$scope.currentBrand = $scope.filterCustomer.brand;
		$scope.currentCategory = $scope.filterCustomer.category;
		$scope.currentSubCategory = $scope.filterCustomer.subcategory;
		$scope.currentSupplier = $scope.filterCustomer.supplier;
	}
	$http.get('rest/basic/groupcustomer').success(function(data){
		$scope.groups= data;
		if ($scope.currentCustomerGroup != null && $scope.currentCustomerGroup != undefined){
			for (var i=0;i<$scope.groups.length;i++){
				if ($scope.currentCustomerGroup.idGroupCustomer == $scope.groups[i].idGroupCustomer){
					$scope.currentCustomerGroup = $scope.groups[i];
				}
			}
		}
	});
	$http.get('rest/basic/categorycustomer').success(function(data){
		$scope.categorys= data;
		if ($scope.currentCategory != null && $scope.currentCategory != undefined){
			for (var i=0;i<$scope.categorys.length;i++){
				if ($scope.currentCategory.idCategoryCustomer == $scope.categorys[i].idCategoryCustomer){
					$scope.currentCategory = $scope.categorys[i];
				}
			}
		}
	});
	$scope.getCustomers = function(){
		$scope.filterCustomer.pagefilter.startelement = (1 - 1 ) * $scope.pagesize;
			$scope.filterCustomer.pagefilter.pageSize = $scope.pagesize;
			$rootScope.filterCustomer = $scope.filterCustomer
		$.ajax({
			url:"rest/registry/customer/",
			type:"POST",
			data:"filter="+JSON.stringify($scope.filterCustomer),
			success:function(data){
				
				$scope.customers= JSON.parse(data);
				
				$scope.$apply();
			}	
		})
	}
	$scope.getCustomers();
	/*$http.get('rest/registry/customer').success(function(data){
				$scope.customers= data;
			});
		*/
	$scope.deleteElement = function(id){
		for(var i=0;i<$scope.customers.length;i++){
			if (id == $scope.customers[i].idCustomer){
				$scope.deletecustomer = $scope.customers[i];
				$.ajax({
						url:"rest/registry/customer/",
						type:"DELETE",
						data:"customerobj="+JSON.stringify($scope.deletecustomer),
						success:function(data){
							alert("Utente eliminato con successo");
							$http.get('rest/registry/customer').success(function(data){
								$scope.customers= data;
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
gecoRegistryCustomerControllers.controller('CustomerDetailCtrl',["$scope","$http","$routeParams","KeyDown","$rootScope","$location",'LoginFactory',function($scope,$http,$routeParams,KeyDown,$rootScope,$location,LoginFactory){
    LoginFactory.checklogin();
	GECO_validator.startupvalidator();
	
	
	$scope.listsections = ["Dettagli [ALT +1]","Listini [ALT +2]","Destinazioni [ALT +3]"];
	$scope.selectedSection = $scope.listsections[0];
	
	$scope.idcustomer= $routeParams.idcustomer;
	$http.get('rest/registry/list').success(function(data){
		$scope.lists= data;
	});
	$http.get('rest/basic/payment').success(function(data){
		$scope.payments= data;
		$scope.fillPayment();
	});
	$http.get('rest/basic/categorycustomer').success(function(data){
		$scope.categorys= data;
		$scope.fillCategory();
	});
	$http.get('rest/basic/groupcustomer').success(function(data){
			$scope.groups= data;
			$scope.fillGroups();
	});
	$http.get('rest/registry/transporter').success(function(data){
			$scope.transporters= data;
			$scope.fillTransporter();
	});
	$scope.fillTransporter = function(){
		if ($scope.transporters !== null && $scope.transporters != undefined && $scope.customer !== null && $scope.customer !== undefined && $scope.customer.transporter !== null && $scope.customer.transporter !== undefined   ){
			for (var ig=0;ig<$scope.transporters.length;ig++){
				if ($scope.customer.transporter != null){
					if ($scope.customer.transporter.idTransporter == $scope.transporters[ig].idTransporter){
						$scope.currentTransporter = $scope.transporters[ig];
					}
				}else{
					$scope.customer.transporter = {};
				}
			}
		}
	}
	$scope.getTransporterLabel = function(transporter){
		return transporter.transportername + " " +transporter.transportersurname;
	}
	$scope.fillCategory = function(){
		if ($scope.categorys !== null && $scope.categorys != undefined && $scope.customer !== null && $scope.customer !== undefined   ){
			for (var ig=0;ig<$scope.categorys.length;ig++){
				if ($scope.customer.category != null){
					if ($scope.customer.category.idCategoryCustomer == $scope.categorys[ig].idCategoryCustomer){
						$scope.currentCategory = $scope.categorys[ig];
					}
				}else{
					$scope.customer.category = {};
				}
			}
		}
	}
	$scope.fillGroups = function(){
		if ($scope.groups !== null && $scope.groups != undefined && $scope.customer !== null && $scope.customer !== undefined   ){
			for (var i=0;i<$scope.groups.length;i++){
				if ($scope.customer.group != null){
					if ($scope.customer.group.idGroupCustomer == $scope.groups[i].idGroupCustomer){
						$scope.currentGroup = $scope.groups[i]; 
					}
				}else{
					$scope.customer.group = {}
				}
			}
		}
	}
	$scope.fillPayment = function(){
		if ($scope.payments !== null && $scope.payments != undefined && $scope.customer !== null && $scope.customer !== undefined   ){
			if ($scope.payments != null && $scope.customer.payment != null){
				for(var i=0;i<$scope.payments.length;i++ ){
					if ($scope.customer.payment.idPayment == $scope.payments[i].idPayment){
						$scope.currentPayment = $scope.payments[i];
					}
				}
			}
		}
	}
	$http.get('rest/registry/customer/'+$scope.idcustomer).success(function(data){
		$scope.customer= data;
		$scope.fillGroups();
		$scope.fillCategory();
		$scope.fillPayment(); 
		$scope.fillTransporter();
	});
	$scope.getListName = function(list){
		return list.code + ' '+ list.description + ' '+ list.startdate; 
	}
	$scope.addListElement = function(customer){
		customer.lists.push({idListCustomer:0});
	}
	$scope.changeListElement = function(list){
	    
		if (list.list != null){
			for(var i=0;i<$scope.lists.length;i++){
				if (list.list.idList == $scope.lists[i].idList){
					$scope.currentList = $scope.lists[i];
				}
			}
		}
		$scope.listid = list.idListCustomer
	}
	$scope.saveCustomer = function(){
		$scope.customer.group = $scope.currentGroup;
		$scope.customer.category = $scope.currentCategory;
	//if (GECO_validator.requiredFields()== true && GECO_validator.emailFields()==true){
			$.ajax({
				url:"rest/registry/customer",
				type:"PUT",
				data:"customers="+JSON.stringify($scope.customer),
				success:function(data){
					result = JSON.parse(data);
					if (result.type == "success"){	
						$scope.customer.idCustomer = result.success;
						$scope.idcustomer = result.success;
						$scope.confirmSaved();
						$location.path("/customer/"+$scope.idcustomer);
						$scope.$apply();
						
					}else{
						$rootScope.errorMessage("Errore: "+result.errorName+" Messaggio:"+result.errorMessage);
					}	
					
				}	
			})
		//}
	} ;
	KeyDown.setupDetail("/customer/0",$scope.saveCustomer,"/customer");
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
	
	$scope.deleteListElement = function(deleteobj){
				$.ajax({
						url:"rest/registry/customer/list",
						type:"DELETE",
						data:"customerlist="+JSON.stringify(deleteobj),
						success:function(data){
							alert("Elemento eliminato con successo");
							$http.get('rest/registry/customer/'+$scope.idcustomer).success(function(data){
								$scope.customer= data;
								$scope.fillGroups();
								$scope.fillCategory();
								$scope.fillPayment(); 
								$scope.fillTransporter();
							});
						}	
					})
			
		
	}
}]);


