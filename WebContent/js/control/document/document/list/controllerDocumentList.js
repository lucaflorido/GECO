var gecoDocumentListControllers = angular.module("geco.document.list",[]);
gecoDocumentListControllers.controller('HeadListCtrl',["$scope","$http","$routeParams","$rootScope",'LoginFactory',function($scope,$http,$routeParams,$rootScope,LoginFactory){
    $scope.loginuser = LoginFactory.checklogin();
	$scope.pagesize = 10;
	$scope.pageArray = [];
	$scope.headamount = 0;
	$scope.headtaxamount = 0;
	$scope.headtotal = 0;
	//$scope.headfilter={};
	$scope.heads = [];
	if ($scope.headfilter == null){
		$scope.headfilter = {};
		$scope.currentCustomer = {};
		$scope.currentDocument = {};
		$scope.currentSuppllier = {};
	}else{
		$scope.currentCustomer = $scope.headfilter.customer;
		$scope.currentDocument = $scope.headfilter.doc;
		$scope.currentSuppllier = $scope.headfilter.supplier;
		if($scope.headfilter.pageSize)
			$scope.pagesize = $scope.headfilter.pageSize;
	}
	if ($routeParams.section == 1){
		$scope.headfilter.isCustomer = true;
		$scope.headfilter.isSupplier = false;
	}else{
		$scope.headfilter.isCustomer = false;
		$scope.headfilter.isSupplier = true;
	}
	if ($routeParams.type == 1){
		$scope.headfilter.isOrder = true;
		$scope.headfilter.isInvoice = false;
		$scope.headfilter.isTransport = false;
	}else{
		if ($routeParams.type == 2){
			$scope.headfilter.isInvoice = true;
			$scope.headfilter.isOrder = false;
			$scope.headfilter.isTransport = false;
		}else if ($routeParams.type == 3){
			$scope.headfilter.isOrder = false;
			$scope.headfilter.isTransport = true;
			$scope.headfilter.isInvoice = false;
		}
		
	}
	if ($scope.headfilter.active == null || $scope.headfilter.active == undefined){
		$scope.headfilter.active = true;
	}
	if ($scope.headfilter.inactive == null || $scope.headfilter.inactive == undefined){
		$scope.headfilter.inactive = false;
	}
	
	$http.get('rest/registry/supplier').success(function(data){
				$scope.suppliers= data;
		if ($scope.headfilter.supplier !== null && $scope.headfilter.supplier !== undefined){
			for (var i=0;$scope.suppliers.length;i++){
				if ($scope.suppliers[i].idSupplier == $scope.headfilter.supplier.idSupplier){
					$scope.headfilter.supplier = $scope.suppliers[i];
					$scope.currentSupplier = $scope.suppliers[i];					
				}

			}
		}
	});
	$http.get('rest/registry/customer').success(function(data){
		$scope.customers= data;
		if ($scope.headfilter.customer !== null && $scope.headfilter.customer !== undefined){
			for (var i=0;$scope.customers.length;i++){
				if ($scope.customers[i].idCustomer == $scope.headfilter.customer.idCustomer){
					$scope.headfilter.customer = $scope.customers[i];
					$scope.currentCustomer = $scope.customers[i];					
				}

			}
		}
	});
	$http.get('rest/basic/document').success(function(data){
		$scope.documents= data;
		if ($scope.headfilter.doc !== null && $scope.headfilter.doc !== undefined){
			for (var i=0;i<$scope.documents.length;i++){
				if ($scope.documents[i].idDocument == $scope.headfilter.doc.idDocument){
					$scope.headfilter.doc = $scope.documents[i];
					$scope.currentDocument = $scope.documents[i];					
				}
			}
		}
	});/*
	$scope.getHeads = function(page){
			$(".pag").removeClass("selected");
			$("#pag"+page).addClass("selected");
			$(".checkbox_all").prop('checked',false);
			$scope.headsToPrint	= [];
			$scope.headfilter.startelement = (page - 1 ) * $scope.pagesize;
			$scope.headfilter.pageSize = $scope.pagesize;
			$rootScope.headfilter = $scope.headfilter;
			$.ajax({
				url:"rest/head/head/",
				type:"POST",
				data:"filter="+JSON.stringify($scope.headfilter),
				success:function(data){
					$scope.heads= JSON.parse(data);
					for(var i=0;i<$scope.heads.length;i++){
						$scope.headamount = $scope.headamount + $scope.heads[i].amount;
						$scope.headtaxamount = $scope.headtaxamount + $scope.heads[i].taxamount;
						$scope.headtotal = $scope.headtotal + $scope.heads[i].total;
						$scope.headamount = Math.round($scope.headamount * 100)/100;
						$scope.headtaxamount = Math.round($scope.headtaxamount * 100)/100;
						$scope.headtotal = Math.round($scope.headtotal * 100)/100;
					}
					$scope.$apply();
					}	
				})
	}*/
	$scope.createConad = function(){
		$.ajax({
					url:"rest/head/head/conad",
					type:"POST",
					data:"filter="+JSON.stringify($scope.headfilter),
					success:function(data){
							window.open(JSON.parse(data).success, '_blank');
					}
			
		});
	}
	$scope.getHeadsNumber = function(){
		
			$scope.pages = [];
			$scope.pageArray = [];
			$scope.headfilter.pageSize = $scope.pagesize;
				$.ajax({
					url:"rest/head/pages/"+$scope.pagesize,
					type:"POST",
					data:"filter="+JSON.stringify($scope.headfilter),
					success:function(data){
							$scope.di = JSON.parse(data);
							$scope.pages= $scope.di.pages;
							$scope.maxSize = 5;
							$scope.bigTotalItems = $scope.di.count;
							
							
							$scope.$apply();
							$scope.getHeadsPag();
						}
					
							
					});
		
	}
	$scope.currentPage = 1;
	
	$scope.getHeadsPag = function(){
			
			$(".checkbox_all").prop('checked',false);
			$scope.headsToPrint	= [];
			$scope.headfilter.startelement = ($scope.currentPage - 1 ) * $scope.pagesize;
			$scope.headfilter.pageSize = $scope.pagesize;
			$rootScope.headfilter = $scope.headfilter;
			$.ajax({
				url:"rest/head/head/",
				type:"POST",
				data:"filter="+JSON.stringify($scope.headfilter),
				success:function(data){
					$scope.heads= JSON.parse(data);
					for(var i=0;i<$scope.heads.length;i++){
						$scope.headamount = $scope.headamount + $scope.heads[i].amount;
						$scope.headtaxamount = $scope.headtaxamount + $scope.heads[i].taxamount;
						$scope.headtotal = $scope.headtotal + $scope.heads[i].total;
						$scope.headamount = Math.round($scope.headamount * 100)/100;
						$scope.headtaxamount = Math.round($scope.headtaxamount * 100)/100;
						$scope.headtotal = Math.round($scope.headtotal * 100)/100;
					}
					$scope.$apply();
					}	
				})
	}
	$scope.$watch( 'currentPage', $scope.getHeadsNumber );
	//$scope.getHeadsNumber();
	$scope.deleteHeadElement = function(){
		$.ajax({
			url:"rest/head/head/",
			type:"DELETE",
			data:"headobj="+JSON.stringify($scope.deleteObj),
			success:function(data){
				//alert("Utente eliminato con successo");
				result = JSON.parse(data);
				if (result.type == "success"){	
					$rootScope.confirmDeleted();
					$scope.getHeads(1);
				}else{
					$rootScope.errorMessage(result.errorMessage);
				}
				
			}	
		})
			
	}
	$scope.confirmDeleteHead = function(obj){
		$scope.deleteObj = obj;
		$.confirm({
			text: "Confermare l'eliminazione dell'elemento selezionato?",
			confirm: function(){
				$scope.deleteHeadElement();
				}
			,
			cancel: function(button) {
				// do something
			},
			confirmButton: "Si",
			cancelButton: "No"
		});
	}
	$scope.printElements = function(){
		$http.get('rest/print').success(function(data){
					window.open(data, '_blank');
		});
	}
	
	$scope.printMassElements = function(){
		$.ajax({
						url:"rest/print/heads/",
						type:"POST",
						data:"ids="+JSON.stringify($scope.headsToPrint),
						success:function(data){
							window.open(JSON.parse(data), '_blank');
						}	
					})
	}
	$scope.headsToPrint = [];
	$scope.addRemoveHeadId = function(id){
		var found = false;
		var indexToRemove = 0;
		for (var i=0;i<$scope.headsToPrint.length;i++ ){
			if ($scope.headsToPrint[i] == id ){
				found = true;
			}
		}
		if (found == false){
			$scope.headsToPrint.push(id);
		}else{
			$scope.headsToPrint = jQuery.grep($scope.headsToPrint, function(value) {
			  return value != id;
			});
		}
	}
	$scope.getRowsWithSerialNumbers = function(id){
		$scope.idHeadSN = id;
		$.ajax({
				url:"rest/head/serialnumberList/"+id,
				type:"POST",
				success:function(data){
					result = JSON.parse(data);
					if (result.type == "success"){	
						$( "#dialog" ).dialog( "option", "minWidth", 550 );
						$( "#dialog" ).dialog( "option", "minHeight", 350 );
						$("#dialog").dialog("open")
						$scope.prods = result.success;
						$scope.$apply();
					}else{
						alert("Errore: "+result.errorName+" Messaggio:"+result.errorMessage);
					}	
				}	
			})
	}
	$scope.getRowSerialNumber = function(row){
		$.ajax({
				url:"rest/head/copyrowserialnumber/",
				type:"POST",
				data:"row="+JSON.stringify(row),
				success:function(data){
					result = JSON.parse(data);
					if (result.type == "success"){	
						$scope.prods.push( result.success);
						$scope.$apply();
					}else{
						alert("Errore: "+result.errorName+" Messaggio:"+result.errorMessage);
					}	
				}	
			})
	}
	$scope.saveRowSerialNumber = function(){
		$.ajax({
				url:"rest/head/saverowsserialnumber/"+$scope.idHeadSN,
				type:"PUT",
				data:"row="+JSON.stringify($scope.prods),
				success:function(data){
					result = JSON.parse(data);
					if (result.type == "success"){	
						$scope.$apply();
						alert("success")
					}else{
						alert("Errore: "+result.errorName+" Messaggio:"+result.errorMessage);
					}	
				}	
			})
	}
	$scope.dateSortFunction = function(head) {
		var date = head.date.split("/");
		return date[2]+date[1]+date[0];
	};
	$scope.dateSortReverseFunction = function(head) {
		var date = head.date.split("/");
		return date[2]+date[0]+date[1];
	};
	$scope.addRemoveHeads = function(){
		$scope.headsToPrint	= [];
		if ($(".checkbox_all").prop('checked') == true){
			for (var i=0;i<$scope.heads.length;i++ ){
				$scope.headsToPrint.push($scope.heads[i].idHead);
			}
			$(".checkbox_single").prop("checked",true);
		}else{
			$(".checkbox_single").prop("checked",false);
		}
	}
}]);
