var gecoCreateOrdersControllers = angular.module("geco.create.orders",[]);


gecoCreateOrdersControllers.controller('CreateOrdersCtrl',["$scope","$http","$routeParams","$location","$rootScope",'LoginFactory',function($scope,$http,$routeParams,$location,$rootScope,LoginFactory){
	$scope.filter = {};
	$scope.generateobj = {};
	LoginFactory.checklogin();
	$http.get('rest/basic/document').success(function(data){
		$scope.documentsTo= data;
	});
	$http.get('rest/basic/groupcustomer').success(function(data){
				$scope.groups= data;
	});
	$scope.generateDocs = function(){
		$scope.generateobj.heads = $scope.heads;
		$.ajax({
				url:"rest/head/createorders",
				type:"POST",
				data:"orders="+JSON.stringify($scope.generateobj),
				success:function(data){
					result = JSON.parse(data);
					if (result.type == "success"){	
						$scope.heads = result.success;
						$rootScope.headfilter = {};
						$rootScope.showFilter = true;
						$rootScope.headfilter.fromDate = $scope.generateobj.date;
						$rootScope.headfilter.toDate = $scope.generateobj.date;
						$location.path("headlist/1/1");
						$scope.$apply();
						
					}else{
						alert("Errore: "+result.errorName+" Messaggio:"+result.errorMessage);
					}	
				}	
		})
	}
}]);
