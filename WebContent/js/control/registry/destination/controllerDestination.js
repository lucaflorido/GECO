var gecoRegistryDestinationControllers = angular.module("geco.registry.destination",[]);






/****
	DESTINATION
**/
gecoProductRegistryControllers.controller('DestinationListCtrl',["$scope","$http",'LoginFactory','KeyDown',function($scope,$http,LoginFactory,KeyDown){
    $scope.loginuser = LoginFactory.checklogin();
	KeyDown.setupList("/destination/0",$scope.printElements);
	$http.get('rest/registry/destination').success(function(data){
		$scope.destinations= data;
	});
	$scope.deleteElement = function(id){
		for(var i=0;i<$scope.destinations.length;i++){
			if (id == $scope.destinations[i].idDestination){
				$scope.deletedestination = $scope.destinations[i];
				$.ajax({
						url:"rest/registry/destination/",
						type:"DELETE",
						data:"destinationobj="+JSON.stringify($scope.deletedestination),
						success:function(data){
							alert("Utente eliminato con successo");
							$http.get('rest/registry/destination').success(function(data){
								$scope.destinations= data;
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
gecoProductRegistryControllers.controller('DestinationDetailCtrl',["$scope","$http","$routeParams",'LoginFactory','KeyDown',function($scope,$http,$routeParams,LoginFactory,KeyDown){
    //LoginFactory.checklogin();
	GECO_validator.startupvalidator();
	$scope.iddestination= $routeParams.iddestination;
	$http.get('rest/registry/customer').success(function(data){
				$scope.customers= data;
				if ($scope.destination != null  ){
				for (var i=0;i< $scope.customers.length;i++){
					if ($scope.customers[i].idCustomer == $scope.destination.customer.idCustomer){
						$scope.currentCustomer = $scope.customers[i];
						break;
					}
				}
		}
	});
	$http.get('rest/registry/destination/'+$scope.iddestination).success(function(data){
		$scope.destination= data;
		if ($scope.destination.customer != null && $scope.customers != null ){
			for (var i=0;i< $scope.customers.length;i++){
				if ($scope.customers[i].idCustomer == $scope.destination.customer.idCustomer){
					$scope.currentCustomer = $scope.customers[i];
					break;
				}
			}
		}
		$scope.fillTransporter();
	});
	
	$http.get('rest/registry/transporter').success(function(data){
			$scope.transporters= data;
			$scope.fillTransporter();
	});
	$scope.fillTransporter = function(){
		if ($scope.transporters !== null && $scope.transporters != undefined && $scope.destination.transporter !== null && $scope.destination.transporter !== undefined   ){
			for (var ig=0;ig<$scope.transporters.length;ig++){
				if ($scope.destination.transporter != null){
					if ($scope.destination.transporter.idTransporter == $scope.transporters[ig].idTransporter){
						$scope.currentTransporter = $scope.transporters[ig];
					}
				}else{
					$scope.destination.transporter = {};
				}
			}
		}
	}
	$scope.getTransporterLabel = function(transporter){
		return transporter.transportername + " " +transporter.transportersurname;
	}
	$scope.saveDestination = function(){
		$scope.destination.customer = $scope.currentCustomer;
	//if (GECO_validator.requiredFields()== true && GECO_validator.emailFields()==true){
			$.ajax({
				url:"rest/registry/destination",
				type:"PUT",
				data:"destinations="+JSON.stringify($scope.destination),
				success:function(data){
					result = JSON.parse(data);
					if (result.type == "success"){	
						$scope.destination.idDestination  = result.success;
						$scope.iddestination = result.success;
						$scope.confirmSaved();
						$scope.$apply();
					}else{
						alert("Errore: "+result.errorName+" Messaggio:"+result.errorMessage);
					}	
				}	
			})
		//}
	} ;
	KeyDown.setupDetail("/destination/0",$scope.saveDestination,"/destination");
}]);



