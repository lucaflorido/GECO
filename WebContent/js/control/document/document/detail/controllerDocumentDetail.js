var gecoDocumentDetailControllers = angular.module("geco.document.detail",[]);

gecoDocumentDetailControllers.controller('HeadDetailCtrl',["$scope","$http","$routeParams","$location","$rootScope","$route",'LoginFactory',function($scope,$http,$routeParams,$location,$rootScope,$route,LoginFactory){
	LoginFactory.checklogin();
	$rootScope.issaved = true;
	$rootScope.orderArray = [];
	$(document).unbind("keydown");
	$(document).keydown(function(e){
      //ALT + N
      if(e.altKey && e.keyCode == 78 ){
			if($scope.selectedSection == $scope.listsections[1]){
				$scope.addRowElement();
				$scope.$apply();
				$("#prod_code").focus();
			}
      }
	  //ALT + R
	   if(e.altKey && e.keyCode == 82 ){
			$route.reload(); 
		}
	  //ALT + J
	   if(e.altKey && e.keyCode == 74 ){
			$scope.openNewDocument(); 
		}
		//ALT+Q
	   if(e.altKey && e.keyCode == 81 ){
			$scope.saveHead();
		}
		//ALT+V
		if(e.altKey && e.keyCode == 87 ){
			if($scope.selectedSection == $scope.listsections[1] && $scope.currentRow != null){
				$scope.search="";
				$scope.prodFound = [];
				$scope.$apply();
				$( "#dialog" ).dialog( "option", "minWidth", 550 );
				$("#dialog").dialog("open");
			}	
		}
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
	$scope.show_qta = false;
	$scope.listsections = ["Testa [Alt + 1]","Righe [Alt + 2]","Totali [Alt + 3]"];
		$scope.isOrder = false;
		$scope.isLoadSerialN = false;
		$scope.isUnloadSerialN = false;
		$scope.idhead= $routeParams.idhead;
    GECO_validator.startupvalidator();
	
	if ($routeParams.rows == null){
		if ($scope.selectedSection == null)
			$scope.selectedSection = $scope.listsections[0];
	}else{
	$("#maincontainer_productdetail").focus();
		switch ($routeParams.rows){
			case "1":
				$scope.isOrder = true;
				$scope.selectedSection = $scope.listsections[1];
				break;
			case "2":
				$scope.isLoadSerialN = true;
				$scope.selectedSection = $scope.listsections[1];
				break;	
			case "3":
				$scope.isUnloadSerialN = true;
				$scope.selectedSection = $scope.listsections[1];
				break;	
		}
	}
	
		$http.get('rest/head/head/'+$scope.idhead).success(function(data){
		$scope.head= data;
		$http.get('rest/basic/payment').success(function(data){
			$scope.payments= data;
			for (var i=0;i<$scope.payments.length;i++){
				if ($scope.head.payment != null && $scope.head.payment.idPayment == $scope.payments[i].idPayment){
					$scope.currentPayment = $scope.payments[i]; 
				}
			}
		});
		$http.get('rest/registry/supplier').success(function(data){
			$scope.suppliers= data;
			for (var i=0;i<$scope.suppliers.length;i++){
				if ($scope.head.supplier != null && $scope.head.supplier.idSupplier == $scope.suppliers[i].idSupplier){
					$scope.currentSupplier = $scope.suppliers[i]; 
				}
			}
		});
		$http.get('rest/basic/document').success(function(data){
			$scope.documents= data;
			for (var i=0;i<$scope.documents.length;i++){
				if ($scope.head.document != null && $scope.head.document.idDocument == $scope.documents[i].idDocument){
					$scope.currentDocument = $scope.documents[i]; 
				}
			}
		});		
		$http.get('rest/registry/customer').success(function(data){
			$scope.customers= data; 
			$scope.fillCustomer();
		});
		$http.get('rest/registry/transporter').success(function(data){
					$scope.transporters= data;
					$scope.setTransporter();
		});
		$scope.setTransporter();
		if ($rootScope.headScope == null){
			$scope.isSaving = false;
			$scope.addRow=true;
		}else{
			$scope.selectedSection = $rootScope.headScope.selectedSection;
			$scope.head = $rootScope.headScope.head;
			$scope.head.rows = $rootScope.headScope.head.rows;
			$scope.currentRow = $rootScope.headScope.currentRow;
			$scope.currentRow.productcode = $rootScope.newProductToAdd;
			$scope.getProduct($scope.currentRow.productcode); 
			$scope.addRow=true;
			$scope.isSaving = false;	
			$rootScope.headScope = null;
			$rootScope.newProductToAdd = null;
			$http.get('rest/registry/customer/'+$scope.head.customer.idCustomer).success(function(data){
				$scope.customer= data;
				$scope.destinations = $scope.customer.destinations;
				if ($scope.destinations != undefined && $scope.destinations.length >0 && $scope.head.destination != null && $scope.currentDestination == null){
				for (var d=0;d<$scope.destinations.length;d++){
					if ($scope.head.destination != null && $scope.head.destination.idDestination == $scope.destinations[d].idDestination){
						$scope.currentDestination = $scope.destinations[d];
					}
				}
				$scope.lists = $scope.customer.lists;
					if ($scope.head.customer.lists != null  && $scope.currentList == null){
						for (var d=0;d<$scope.head.customer.lists.length;d++){
							//$scope.lists.push($scope.head.customer.lists[d].list);
							if ($scope.head.customer.lists[d].list != null && $scope.head.list != null && $scope.head.list.idList == $scope.head.customer.lists[d].list.idList){
								$scope.currentList = $scope.head.customer.lists[d].list;
							}
							
						}
					}
					

			}
			});
			
		}
	});
	$scope.fillCustomer = function(){
		if ($scope.customers != undefined && $scope.customers.length >0 && $scope.head != null && $scope.head.customer != null && $scope.currentCustomer == null){
			for (var itx=0;itx<$scope.customers.length;itx++){
				if ($scope.head.customer.idCustomer == $scope.customers[itx].idCustomer){
					$scope.currentCustomer = $scope.customers[itx]; 
					$scope.destinations = $scope.head.customer.destinations;
					if ($scope.destinations != undefined && $scope.destinations.length >0 && $scope.head.destination != null && $scope.currentDestination == null){
						for (var d=0;d<$scope.destinations.length;d++){
							if ($scope.head.destination != null && $scope.head.destination.idDestination == $scope.destinations[d].idDestination){
								$scope.currentDestination = $scope.destinations[d];
							}
						}
					}
					$scope.lists = [];
					if ($scope.head.customer.lists != null ){
						for (var d=0;d<$scope.head.customer.lists.length;d++){
							$scope.lists.push($scope.head.customer.lists[d].list);
							if ($scope.head.customer.lists[d].list != null && $scope.head.list != null && $scope.head.list.idList == $scope.head.customer.lists[d].list.idList){
								$scope.currentList = $scope.head.customer.lists[d].list;
								
							}
							
						}
					}
					break;
					
				}
			}
		}
	}
	$scope.setTransporter = function(){
		if ($scope.head != null && $scope.transporters != null){
			for (var i=0;i<$scope.transporters.length;i++){
							if ($scope.head.transporter != null && $scope.head.transporter.idTransporter == $scope.transporters[i].idTransporter){
								$scope.currentTransporter = $scope.transporters[i]; 
							}
						}
		}
	}
	
	$scope.changeDestination = function(){
		if ($scope.currentDestination.transporter !== null && $scope.currentDestination.transporter !== undefined ){
					for(var i=0;i<$scope.transporters.length;i++){
						if ($scope.transporters[i].idTransporter == $scope.currentDestination.transporter.idTransporter){
							$scope.currentTransporter = $scope.transporters[i] 
							//$(".selpayment").trigger("chosen:update");
						}
					}
		}else{
			if ($scope.currentCustomer.transporter === null || $scope.currentCustomer.transporter === undefined ){
				$scope.currentTransporter = null;
			}
			
		}
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
				if ($scope.currentCustomer.transporter !== null && $scope.currentCustomer.transporter !== undefined ){
					for(var i=0;i<$scope.transporters.length;i++){
						if ($scope.transporters[i].idTransporter == $scope.currentCustomer.transporter.idTransporter){
							$scope.currentTransporter = $scope.transporters[i] 
							//$(".selpayment").trigger("chosen:update");
						}
					}
				}else{
					$scope.currentTransporter = null;
				}
		});
		$http.get('rest/registry/list/customer/'+idCustomer).success(function(data){
				$scope.lists = [];
				if (data != null){
					for (var d=0;d<data.length;d++){
					$scope.lists.push(data[d].list)
						if (data[d].list != null && $scope.currentList != null && $scope.currentList.idList == data[d].list.idList){
							$scope.currentList = data[d].list;
						}
						
					}
				}
		});
		if ($scope.currentDocument != null){
			if ($scope.currentDocument.credit == true || $scope.currentDocument.debit == true){
				for(var i=0;i<$scope.payments.length;i++){
					if ($scope.payments[i].idPayment == $scope.currentCustomer.payment.idPayment){
						$scope.currentPayment = $scope.payments[i] 
						//$(".selpayment").trigger("chosen:update");
					}
				}
			}
		}
		
		
	}
	$scope.changeSupplier = function(){
		if ($scope.currentDocument != null){
			if ($scope.currentDocument.credit == true || $scope.currentDocument.debit == true){
				for(var i=0;i<$scope.payments.length;i++){
					if ($scope.payments[i].idPayment == $scope.currentSupplier.payment.idPayment){
						$scope.currentPayment = $scope.payments[i] 
						//$(".selpayment").trigger("chosen:update")
					}
				}
			}
		}
	}
	/******
        ADD NEW ROW	
	******/
	$scope.addRowElement = function(){
		//if ($scope.currentRow == null){
			if($scope.head.rows == null){
				$scope.head.rows = [];
			}
			var newobj = {idRow:0,type:"V"};
			$scope.currentRow = newobj;
			$scope.showNewRow = true;
			$scope.head.rows.push(newobj);
			$rootScope.issaved = false;
			$("#prod_code").focus();
			$scope.showprice = false;
		//}
	}
	$scope.confirmRowElement = function(){
		if ($scope.currentRow != null){
			if($scope.head.rows == null){
				$scope.head.rows = [];
			}
			
			$scope.$apply();
		}
	}
	$scope.checkKeyCode = function(){
		alert($event.keyCode);
	}
	$scope.getProductCode = function(produm,um,code){
		for(var i=0;i<produm.length;i++){
			if (produm[i].um.idUnitMeasure == um.idUnitMeasure){
				return produm[i].code;
			}
		}
		return code;
	}
	$scope.getProduct = function(code){
		if (code != "" && code != undefined){
			$rootScope.setModified();
			var idList = 0;
			if ($scope.head.list!= null){
				idList = $scope.head.list.idList
			}
			$.ajax({
					url:'rest/registry/product/code/'+code+"/"+idList,
					type:"POST",
					data:"head="+JSON.stringify($scope.head),
					success:function(data){
					data = JSON.parse(data);
					if (data.idProduct != 0){
					$scope.currentRow.product = data;
					$scope.currentRow.productcode =$scope.getProductCode(data.ums,data.umselected, code);
					$scope.currentRow.productdescription = data.description;
					$scope.currentRow.price = data.listprice;
					$scope.currentRow.productum = data.umselected.code;
					$scope.currentRow.taxrate = data.taxrate;
					$scope.currentRow.um = data.umselected;
					if ($scope.currentRow.quantity == 0 || $scope.currentRow.quantity == "" ){
						$scope.currentRow.quantity  = 1;
					}
					if (data.weigth > 0){
						$scope.currentRow.quantity = data.weigth;
					}
					if($scope.currentRow.product.storage)
						$scope.currentRow.product.stockqta = Math.round($scope.currentRow.product.storage.stock/$scope.currentRow.product.conversionrate *100) /100;
						$scope.$apply();
						$("#qta").focus();
					}else{
						$scope.search=code;
						$scope.prodFound = [];
						$scope.searchProd(code);
						$scope.$apply();
						$scope.showSearch = true;
					}
				}
			});
			
		}
	}
	$scope.calculateRow = function(row ){
		
		
		if (row.quantity != "" && row.quantity != null  ){
			$rootScope.setModified();
			$scope.totrowobj = {};
			$scope.totrowobj.qta = row.quantity;
			$scope.totrowobj.taxrate = row.product.taxrate.value;
			$scope.totrowobj.price = row.price;
			$.ajax({
					url:"rest/documenthelp/rowtotal",
					type:"POST",
					data:"row="+JSON.stringify($scope.totrowobj),
					success:function(data){
						$scope.totrow = $.parseJSON(data);
						row.total = $scope.totrow.total;
						row.amount = $scope.totrow.amount;
						row.taxamount = $scope.totrow.taxamount;
						$scope.$apply();
						$scope.calculateTotal();
					}	
				});
		}
	}
	$scope.setCurrentRow = function(row){
		
		$scope.currentRow = row;
		$scope.showNewRow = true;
	} 
	$scope.calculateTotal = function(){
		$scope.totrowobj = {};
		$scope.totrowobj.rows = $scope.head.rows;
		$.ajax({
				url:"rest/documenthelp/headtotal",
				type:"POST",
				data:"head="+JSON.stringify($scope.totrowobj),
				success:function(data){
					$scope.totrow = $.parseJSON(data);
					$scope.head.total =$scope.totrow.total;
					$scope.head.total4 =$scope.totrow.total4;
					$scope.head.total10 =$scope.totrow.total10;
					$scope.head.total20 =$scope.totrow.total20;
					$scope.head.amount =$scope.totrow.amount;
					$scope.head.amount4 =$scope.totrow.amount4;
					$scope.head.amount10 =$scope.totrow.amount10;
					$scope.head.amount20 =$scope.totrow.amount20;
					$scope.head.taxamount =$scope.totrow.taxamount;
					$scope.head.taxamount4 =$scope.totrow.taxamount4;
					$scope.head.taxamount10 =$scope.totrow.taxamount10;
					$scope.head.taxamount20 =$scope.totrow.taxamount20;
					$scope.addRow=true;
					$scope.$apply();
				}	
			})
	}
	
	$scope.saveHead = function(){
		$scope.checkHead();
	};
	$scope.checkHead = function(){
	$scope.saveHeadChecked();
		
		$.ajax({
			url:"rest/head/checkhead",
			type:"POST",
			data:"head="+JSON.stringify($scope.head),
			success:function(data){
				result = JSON.parse(data);
				if (result.type == "success"){	
					$scope.saveHeadChecked();
				}else{
					
					$scope.saveHeadChecked();

				}
				$scope.isSaving = false;						
			},
			error:function(data){
				$scope.isSaving = false;
			}		
		});
		/************************** THIS CODE IS FOR CHECKING QUANTITY AND SERIAL NUMBERS
		*****************************/
	}
	
	$scope.saveHeadChecked = function(){
		$scope.head.payment = $scope.currentPayment;
	     if ($scope.isSaving == false){  
			$scope.isSaving = true;	
				$.ajax({
					url:"rest/head/head",
					type:"PUT",
					data:"heads="+JSON.stringify($scope.head),
					success:function(data){
						
						result = JSON.parse(data);
						if (result.type == "success"){	
							$scope.head = result.success;
							$scope.idhead = $scope.head.idHead;
							$scope.$apply();
							$rootScope.selectedSection = $scope.selectedSection
							$rootScope.issaved = true;
							$scope.confirmSaved();
						}else{
							$scope.errorMessage("Errore: "+result.errorName+" Messaggio:"+result.errorMessage);
						}
                        $scope.isSaving = false;						
					},
					error:function(data){
						$scope.isSaving = false;
					}		
				})
		}
	}
	$rootScope.saveFuntion = $scope.saveHeadChecked;
	/******
	CALCULATE NUMBER
	******/
	$scope.calculateNumber = function(){
		
		if ($scope.head.date != null && $scope.head.document != null && ($scope.head.idHead == null || $scope.head.idHead == 0 || $scope.head.idHead == undefined)){
			$rootScope.setModified();
			var year = $scope.head.date.split("/");
			
			year = year[2];
			if ($scope.head.document.counter != null && $scope.head.document.counter.yearsvalue != null ){
				var yearsvalue = $scope.head.document.counter.yearsvalue;
				for (var i=0; i< yearsvalue.length;i++){
					if (year == yearsvalue[i].year){
					    $scope.head.number = yearsvalue[i].value
					}
				}
				if ($scope.head.number == 0){
					$scope.head.number = 1;
				}
			}
			
			
		}
	}
	/******
	FORMAT YEAR
	******/
	$scope.formatYear = function(){
		if ($scope.head.date == null || $scope.head.date == undefined){
			return;
			}
		var year = $scope.head.date.split("/");
		if (year.length <2){
			return;
			}
		year = year[2];
		switch(year.length){
			case 0:
			year = "2000"
			$scope.head.date = $scope.head.date.substring(0,6);
			$scope.head.date = $scope.head.date+year;
			break;
			case 1:
			year = "200"+year ;
			$scope.head.date = $scope.head.date.substring(0,6);
			$scope.head.date = $scope.head.date+year;
			break;
			case 2:
			year = "20"+year ;
			$scope.head.date = $scope.head.date.substring(0,6);
			$scope.head.date = $scope.head.date+year;
			break;
			case 3:
			year = "2"+year ;
			$scope.head.date = $scope.head.date.substring(0,6);
			$scope.head.date = $scope.head.date+year;
			break;
		}
		
	}
	$scope.printElements = function(){
		if ($scope.head.idHead != 0 && $scope.head.idHead != null ){
		$.ajax({
				url:"rest/print/head/"+$scope.head.idHead,
				type:"GET",
				success:function(data){
					result = JSON.parse(data);
					window.open(result, '_blank');
				}	
			});
		}else{
			$scope.errorMessage("E' possibile stampare solo i documenti salvati");
		}
	}
	$scope.search = "";
	$scope.searchProd = function(code){
		if (code != null && code != undefined){
			$scope.search = code;
		}
		if ($scope.search != "" && $scope.search != undefined){
			$http.get("rest/registry/product/search/"+$scope.search).success(function(data){
				if (data.length >0){
					$scope.prodFound = data;
					
				}else{
					$scope.errorMessage("Nessun prodotto trovato");
				}
			})
		}
		var phase = this.$root.$$phase;
		if(phase != '$apply' && phase != '$digest') {
			$scope.$apply();
		}
	}
	$scope.insertProduct = function(code){
		$scope.getProduct(code);
		$scope.showSearch = false;
	}
	$scope.closeSearch = function(){
		$scope.showSearch = false;
	}
	$scope.forceSerialNumbers = function(){
		var serialnumber = $("#forceserial").val();
		var expireddate = $("#forceexpiredate").val();
		for (var i=0; i< $scope.head.rows.length;i++){
			if ($scope.head.rows[i].product.manageserialnumber == true && ($scope.head.rows[i].serialnumber == "" || $scope.head.rows[i].serialnumber == null) && ($scope.head.rows[i].expiredate == "" || $scope.head.rows[i].expiredate == null )){
				$scope.head.rows[i].serialnumber = serialnumber;
				$scope.head.rows[i].expiredate = expireddate;
			}
		}
		$scope.$apply();
	}
	$scope.forceLoadSerialNumbers = function(){
		$.ajax({
				url:"rest/head/serialnumberList/"+$scope.head.idHead,
				type:"POST",
				success:function(data){
					result = JSON.parse(data);
					var store = [];
					if  (result.success.length > 0){
						for(var i=0;i<result.success.length;i++){
							var found = false;
							for(var k=0;k<$scope.head.rows.length;k++){
								
								if (result.success[i].idRow == $scope.head.rows[k].idRow){
									$scope.head.rows[k].quantity = result.success[i].quantity;
									$scope.head.rows[k].serialnumber = result.success[i].serialnumber;
									$scope.head.rows[k].expiredate = result.success[i].expiredate;
									found = true;
									break;
								}
							}
							if (found == false){
								store.push(result.success[i]);
							}
						}
						if (store.length >0){
							for(var y=0;y<store.length;y++){
								$scope.head.rows.push(store[y]);
							}
						}
						$scope.$apply();
					}
					
				}	
			});
		}
	$rootScope.deleteElement = function(){
		if ($rootScope.deleteObj.idRow == 0){
			$scope.head.rows = $.grep($scope.head.rows,function(a){  return a != $rootScope.deleteObj;})
			$scope.currentRow = null;
			$scope.$apply();
		}else{
			$.ajax({
				url:"rest/head/removerow/"+$rootScope.deleteObj.idRow,
				type:"DELETE",
				success:function(data){
					result = JSON.parse(data);
					if (result.type == "success"){	
							$scope.head.rows = $.grep($scope.head.rows,function(a){  return a != $rootScope.deleteObj;})
							$scope.currentRow = null;
							$scope.$apply();
							
						}else{
							$scope.errorMessage("Errore: "+result.errorName+" Messaggio:"+result.errorMessage);
						}
				}	
			});
		}
	}
	$scope.openNewDocument = function(){
		$scope.selectedSection = $scope.listsections[0];
		var url = $location.url();
		if (url.indexOf("/head/0") < 0){
			$location.path("/head/0")
		}else{
			$route.reload();
		}
	}
	$scope.addNewRowFromQuantity = function(e){
		if (e.keyCode == 13 && $scope.currentRow.quantity != ""){
			$scope.addRowElement();
			$scope.$apply();
			$("#prod_code").focus();
		}
	}
	$scope.addNewProduct = function(){
		$rootScope.headScope = $scope;
		$scope.showSearch = false;
		$rootScope.newProductToAdd = $scope.currentRow.productcode;
		
		$location.path("/product/0")
	}
	/*****
	SHOW PRICE
	******/
	$scope.showprice = false;
	
}]);

