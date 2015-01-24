var gecoCopyRowsControllers = angular.module("geco.copy.rows",[]);




gecoCopyRowsControllers.controller('CopyRowsCtrl',["$scope","$http","$routeParams","$rootScope","$location",'LoginFactory',function($scope,$http,$routeParams,$rootScope,$location,LoginFactory){
	LoginFactory.checklogin();
	$scope.filter = {};
	$scope.generateobj = {};
	$(".datepicker").datepicker({ dateFormat: "dd/mm/yy" });
	$http.get('rest/registry/customer').success(function(data){
				$scope.customers= data;
	});
	$http.get('rest/registry/list').success(function(data){
				$scope.lists= data;
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
					if ($scope.heads.length > 0)
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
				url:"rest/head/copyrows/objectdocs",
				type:"POST",
				data:"generateobj="+JSON.stringify($scope.generateobj),
				success:function(data){
					result = JSON.parse(data);
					if (result.type == "success"){	
						$rootScope.headfilter = {};
					$rootScope.showFilter = true;
					$rootScope.headfilter.fromDate = $scope.generateobj.date;
					$rootScope.headfilter.toDate = $scope.generateobj.date;
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
						alert("Errore: "+result.errorName+" Messaggio:"+result.errorMessage);
					}	
				}	
		})
	}
	$scope.$on('ngRepeatFinished', function(ngRepeatFinishedEvent) {
    	$(".detailview").css("display","none")
	});
}]);
