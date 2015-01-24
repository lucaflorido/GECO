var gecoRegistryListControllers = angular.module("geco.registry.list",[]);



/***
	LIST
*/

gecoRegistryListControllers.controller('ListListCtrl',["$scope","$http",'LoginFactory','KeyDown',function($scope,$http,LoginFactory,KeyDown){
    $scope.loginuser = LoginFactory.checklogin();
	
	KeyDown.setupList("/list/0",$scope.printElements);
	$http.get('rest/registry/list').success(function(data){
		$scope.lists= data;
	});
	$scope.deleteElement = function(id){
		for(var i=0;i<$scope.lists.length;i++){
			if (id == $scope.lists[i].idList){
				$scope.deletelist = $scope.lists[i];
				$.ajax({
						url:"rest/registry/list/",
						type:"DELETE",
						data:"listobj="+JSON.stringify($scope.deletelist),
						success:function(data){
							alert("Utente eliminato con successo");
							$http.get('rest/registry/list').success(function(data){
								$scope.lists= data;
							});
						}	
					})
			}	
		}
	}
	$scope.printElement = function(id){
		$.ajax({
						url:"rest/print/list/"+id,
						type:"POST",
						success:function(data){
							//alert("Utente eliminato con successo");
							window.open(JSON.parse(data), '_blank');
						}	
					})
	}
	$scope.dateSortFunction = function(list) {
		var date = list.startdate.split("/");
		return date[2]+date[1]+date[0];
	};
}]);
gecoRegistryListControllers.controller('ListDetailCtrl',["$scope","$http","$routeParams","ScopeFactory",'LoginFactory','KeyDown',function($scope,$http,$routeParams,ScopeFactory,LoginFactory,KeyDown){
    LoginFactory.checklogin();
	GECO_validator.startupvalidator();
	$scope.newList = {isPercentage:false};
	$(".datepicker").datepicker({ dateFormat: "dd/mm/yy" });
	$scope.idlist= $routeParams.idlist;
	$http.get('rest/registry/product').success(function(data){
		$scope.products= data;
		$http.get('rest/registry/list/'+$scope.idlist).success(function(data){
			$scope.list= data;
		});
	});
	$scope.addProdElement = function(list){
		$scope.prodid = 0
		list.listproduct.push({idListProduct:0});
	}
	$scope.changeProdElement = function(ump){
	    
		if (ump.product != null){
			for(var i=0;i<$scope.products.length;i++){
				if (ump.product.idProduct == $scope.products[i].idProduct){
					$scope.currentProd = $scope.products[i];
				}
			}
		}
		$scope.prodid = ump.idListProduct
	}
	$scope.saveProduct = function(){
	//if (GECO_validator.requiredFields()== true && GECO_validator.emailFields()==true){
			
			
			$scope.newList.list = $scope.list;
			$scope.value = 0;
			$.ajax({
				url:"rest/registry/list",
				type:"PUT",
				data:"lists="+JSON.stringify($scope.newList),
				success:function(data){
					result = JSON.parse(data);
					if (result.type == "success"){	
						$scope.list.idList = result.success;
						$scope.idlist = result.success;
						alert("success");
						$http.get('rest/registry/list/'+$scope.idlist).success(function(data){
							$scope.list= data;
						});
					}else{
						alert("Errore: "+result.errorName+" Messaggio:"+result.errorMessage);
					}	
				}	
			})
		//}
	} ;
	KeyDown.setupDetail("/list/0",$scope.saveProduct,"/list");
}]);




