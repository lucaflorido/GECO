var gecoRegistryTransporterControllers = angular.module("geco.registry.transporter",[]);

/****
	TRANSPORTER
**/
gecoRegistryTransporterControllers.controller('TransporterListCtrl',["$scope","$http",'LoginFactory','KeyDown',function($scope,$http,LoginFactory,KeyDown){
    KeyDown.setupList("/transporter/0",$scope.printElements);
	$http.get('rest/registry/transporter').success(function(data){
		$scope.transporters= data;
	});
	$scope.deleteElement = function(id){
		for(var i=0;i<$scope.transporters.length;i++){
			if (id == $scope.transporters[i].idTransporter){
				$scope.deletetransporter = $scope.transporters[i];
				$.ajax({
						url:"rest/registry/transporter/",
						type:"DELETE",
						data:"transporterobj="+JSON.stringify($scope.deletetransporter),
						success:function(data){
							alert("Utente eliminato con successo");
							$http.get('rest/registry/transporter').success(function(data){
								$scope.transporters= data;
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
gecoRegistryTransporterControllers.controller('TransporterDetailCtrl',["$scope","$http","$routeParams",'LoginFactory','KeyDown',function($scope,$http,$routeParams,LoginFactory,KeyDown){
    //LoginFactory.checklogin();
	$scope.listsections = ["Dettagli","Listini","Destinazioni"];
	$scope.selectedSection = $scope.listsections[0];
	GECO_validator.startupvalidator();
	$scope.idtransporter= $routeParams.idtransporter;
	$http.get('rest/registry/list').success(function(data){
		$scope.lists= data;
	});
	$http.get('rest/registry/transporter/'+$scope.idtransporter).success(function(data){
		$scope.transporter= data;
	});
	$scope.getListName = function(list){
		return list.code + ' '+ list.description + ' '+ list.startdate; 
	}
	$scope.addListElement = function(transporter){
		transporter.lists.push({idListTransporter:0});
	}
	$scope.changeListElement = function(list){
	    
		if (list.list != null){
			for(var i=0;i<$scope.lists.length;i++){
				if (list.list.idListMeasure == $scope.lists[i].idListMeasure){
					$scope.currentList = $scope.lists[i];
				}
			}
		}
		$scope.listid = list.idListTransporter
	}
	$scope.saveTransporter = function(){
		
	//if (GECO_validator.requiredFields()== true && GECO_validator.emailFields()==true){
			$.ajax({
				url:"rest/registry/transporter",
				type:"PUT",
				data:"transporters="+JSON.stringify($scope.transporter),
				success:function(data){
					result = JSON.parse(data);
					if (result.type == "success"){	
						$scope.transporter.idTransporter = result.success;
						$scope.idtransporter = result.success;
						alert("success");
						$scope.$apply();
					}else{
						alert("Errore: "+result.errorName+" Messaggio:"+result.errorMessage);
					}
				}	
			})
		//}
	} ;
	KeyDown.setupDetail("/transporter/0",$scope.saveTransporter,"/transporter");
}]);