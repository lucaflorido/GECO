var gecoGenerateDocumentControllers = angular.module("geco.generate.document",[]);



gecoGenerateDocumentControllers.controller('GenerateDocsCtrl',["$scope","$http","$routeParams","$rootScope","$location",'LoginFactory',function($scope,$http,$routeParams,$rootScope,$location,LoginFactory){
	LoginFactory.checklogin();
	$scope.filter = {};
	$scope.generateobj = {};
	$http.get('rest/registry/customer').success(function(data){
				$scope.customers= data;
	});
	$http.get('rest/registry/supplier').success(function(data){
				$scope.suppliers= data;
	});
	$http.get('rest/registry/transporter').success(function(data){
				$scope.transporters= data;
	});
	
	$http.get('rest/basic/document').success(function(data){
		$scope.documents= data;
	});
	$scope.selectDocs = function(){
		$scope.heads = [];
		$.ajax({
				url:"rest/head/generationdocs/filterdocs",
				type:"POST",
				data:"filter="+JSON.stringify($scope.filter),
				success:function(data){
					$scope.heads= JSON.parse(data);
					$scope.selectedSection = $scope.listsections[1];
					$scope.$apply();
				}	
			})
			
		
	}
	$scope.setGenerate = function(head){
		if (head.generate == null || head.generate == false){
			head.generate = true;
			for (var i=0;i< head.rows.length;i++){
				head.rows[i].generate = true;
			}
		}else{
			head.generate = false;
			for (var i=0;i< head.rows.length;i++){
				head.rows[i].generate = false;
			}
		}
	}
	$scope.setUnSetGenerate = function(){
		var isChecked = $('#chkSelect').prop('checked');
		if (isChecked == false){
			$scope.setAllHeads(false);	
		}else{
			$scope.setAllHeads(true);
		}
	}
	$scope.setAllHeads = function(value){
		for (var i=0;i< $scope.heads.length;i++){
			$scope.heads[i].generate = value;
			for (var ir=0;ir< $scope.heads[i].rows.length;ir++){
				$scope.heads[i].rows[ir].generate = value;
			}
		}
	}
	$scope.setGenerateRow = function(row,head){
		if (row.generate == null || row.generate == false){
			row.generate = true;
			var generate = true;
			for (var i=0;i< head.rows.length;i++){
				if (head.rows[i].generate == false){
					generate = false
				}
			}
			head.generate = generate;
		}else{
			row.generate = false;
			head.generate = false;
		}
	}
	$scope.detailView = function(idHead){
		if ($("#det"+idHead).css("display") == "none"){
			$("#det"+idHead).css("display","");
		}else{
			$("#det"+idHead).css("display","none");
		}
	}
	$scope.listsections = ["Seleziona","Genera"];
	$scope.selectedSection = $scope.listsections[0];
	$scope.generateDocs = function(){
		$scope.generateobj.heads = $scope.heads;
		$.ajax({
				url:"rest/head/generationdocs/objectdocs",
				type:"POST",
				data:"generateobj="+JSON.stringify($scope.generateobj),
				success:function(data){
					var result = JSON.parse(data);
					if (result.type == "success"){
						$rootScope.headfilter = {};
						$rootScope.showFilter = true;
						$rootScope.headfilter.fromDate = $scope.generateobj.date;
						$rootScope.headfilter.toDate = $scope.generateobj.date;
						$rootScope.headfilter.doc = $scope.currentDocumentGenerate;
 						$rootScope.confirmGenerated();
						if ($scope.generateobj.generateDoc.customer == true){
							if ($scope.generateobj.generateDoc.order == true){
								$location.path("/headlist/1/1");
							}else if ($scope.generateobj.generateDoc.invoice == true){
								$location.path("/headlist/1/2");
							}else if ($scope.generateobj.generateDoc.transport == true){
								$location.path("/headlist/1/3");
							}
						}else if ($scope.generateobj.generateDoc.supplier == true){
							if ($scope.generateobj.generateDoc.order == true){
								$location.path("/headlist/2/1");
							}else if ($scope.generateobj.generateDoc.invoice == true){
								$location.path("/headlist/2/2");
							}else if ($scope.generateobj.generateDoc.transport == true){
								$location.path("/headlist/2/3");
							}
						}
						$scope.$apply();
					}else{
						$rootScope.errorMessage(result.errorMessage);
					}
				}	
		})
	}
	$scope.changeCustomer = function(idCustomer){
		$rootScope.setModified();
		$http.get('rest/registry/destination/customer/'+idCustomer).success(function(data){
				$scope.destinations= data;
				if ($scope.currentDestination != null){
					for (var d=0;d<$scope.destinations.length;d++){
						if ($scope.currentDestination != null && $scope.currentDestination.idDestination == $scope.destinations[d].idDestination){
							$scope.currentDestination = $scope.destinations[d];
						}
					}
				}
				//TRANSPORTER
				
		});
		
		
		
	}
}]);


