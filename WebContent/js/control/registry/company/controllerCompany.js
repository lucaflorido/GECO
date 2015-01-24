var gecoRegistryCompanyControllers = angular.module("geco.registry.company",[]);

/*****
REGISTRY
***/
gecoRegistryCompanyControllers.controller('CompanyCtrl',["$scope","$http",'LoginFactory',function($scope,$http,LoginFactory){
    $scope.loginuser = LoginFactory.checklogin();
	GECO_validator.startupvalidator();
	$scope.companysaved = true;
	$http.get('rest/registry/company').success(function(data){
		$scope.company = data;
		if ($scope.company == null  ){
			$scope.company = {idCompany:0};
		}
	});
	$scope.saveCompany = function(){
		$.ajax({
			url:"rest/registry/company",
			type:"PUT",
			data:"companys="+JSON.stringify($scope.company),
			success:function(data){
					/*$http.get('rest/basic/payment').success(function(data){
										$scope.payments= data;
										$scope.paymentsaved = true;
										$scope.modifyid = 0;
								});*/
					$scope.confirmSaved();
			}	
		})
		
	}
}]);
