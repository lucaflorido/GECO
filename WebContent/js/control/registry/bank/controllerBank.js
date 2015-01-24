var gecoRegistryBankControllers = angular.module("geco.registry.bank",[]);

gecoRegistryBankControllers.controller('BankListCtrl',["$scope","$http",'LoginFactory',function($scope,$http,LoginFactory){
    $scope.loginuser = LoginFactory.checklogin();
	$http.get('rest/registry/bank').success(function(data){
		$scope.banks= data;
	});
	$scope.deleteElement = function(id){
		for(var i=0;i<$scope.banks.length;i++){
			if (id == $scope.banks[i].idBank){
				$scope.deletebank = $scope.banks[i];
				$.ajax({
						url:"rest/registry/bank/",
						type:"DELETE",
						data:"bankobj="+JSON.stringify($scope.deletebank),
						success:function(data){
							alert("Utente eliminato con successo");
							$http.get('rest/registry/bank').success(function(data){
								$scope.banks= data;
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
gecoRegistryBankControllers.controller('BankDetailCtrl',["$scope","$http","$routeParams",'LoginFactory',function($scope,$http,$routeParams,LoginFactory){
    LoginFactory.checklogin();
	GECO_validator.startupvalidator();
	$scope.idbank= $routeParams.idbank ;
	
	$http.get('rest/registry/bank/'+$scope.idbank).success(function(data){
		$scope.bank= data;
	});
	
	$scope.saveBank = function(){
	//if (GECO_validator.requiredFields()== true && GECO_validator.emailFields()==true){
			$.ajax({
				url:"rest/registry/bank",
				type:"PUT",
				data:"banks="+JSON.stringify($scope.bank),
				success:function(data){
					$scope.confirmSaved();
				}	
			})
		//}
	} ;
	
}]);
