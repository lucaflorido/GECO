var gecoControllers = angular.module("gecoControllers",[]);
/**
	LOGIN CONTROLLER
*/
gecoControllers.controller('LoginCtrl',["$scope","$http",'LoginFactory',function($scope,$http,LoginFactory){
	$(".header").css("display","none");
	//LoginFactory.checklogin();
	$http.get('rest/user/startup').success(function(data){
	});
	$scope.login = {username:"",password:""};
	$scope.loginfunction = function(){
		$.ajax({
			url:"rest/user/",
			type:"POST",
			data:"loginobj="+JSON.stringify($scope.login),
			success:function(data){
				var result = $.parseJSON(data);
				if (result == true || result == "true"){
					$(".myprofilelabel").html($scope.login.username);
					
					$(".header").css("display","");
					$(window.location).attr('href', '#/welcome');
				}else{
					alert("Username o Password errati")
				}
			}	
		})
	};
	$(".loginbutton").click(function(e){
		$scope.loginfunction();
	});
	
	$scope.loginout = function(){
		$.ajax({
			url:"rest/user/logout/",
			type:"GET",
			success:function(data){
					$(".header").css("display","none");
					$(window.location).attr('href', '#/login');
			}	
		})
	}
	$("#logoutbutton").click(function(e){
		$scope.loginout();
	})
	$("#enterpassword").bind('keypress', function(e) {
		var code = e.keyCode || e.which;
		 if(code == 13) { //Enter keycode
		   //Do something
		   $scope.loginfunction();
		 }
	});
	
	$(".redobutton").click(function(e){
		alert($("ifname").attr("required"));
	})
	/*$http.get('rest/user').success(function(data){
		$scope.users= data;
	});*/
	
	
	
}]);
gecoControllers.controller('WelcomeCtrl',['$scope','LoginFactory',function($scope,LoginFactory){
	LoginFactory.checklogin();
	
}]);
gecoControllers.controller('UserListCtrl',["$scope","$http",'LoginFactory',function($scope,$http,LoginFactory){
    LoginFactory.checklogin();
	$scope.loginuser = LoginFactory.checklogin();
	$http.get('rest/user').success(function(data){
		$scope.users= data;
	});
	$scope.deleteElement = function(id){
		for(var i=0;i<$scope.users.length;i++){
			if (id == $scope.users[i].iduser){
				$scope.deleteUser = $scope.users[i];
				$.ajax({
						url:"rest/user/",
						type:"DELETE",
						data:"userobj="+JSON.stringify($scope.deleteUser),
						success:function(data){
							alert("Utente eliminato con successo");
							$http.get('rest/user').success(function(data){
								$scope.users= data;
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

gecoControllers.controller('UserDetailCtrl',['$scope', '$routeParams','$http','LoginFactory',function($scope,$routeParams,$http,LoginFactory){
	LoginFactory.checklogin();
	GECO_validator.startupvalidator();
	$scope.userId= $routeParams.userId ;
	
	$http.get('rest/role/').success(function(data){
		$scope.roles= data;
		$http.get('rest/user/'+$scope.userId).success(function(data){
			$scope.user= data;
			for (var i=0;i<$scope.roles.length;i++){
				if($scope.roles[i].idrole == $scope.user.role.idrole){
					$scope.currentRole = $scope.roles[i];
				}
			}
		});
	});
	
	$(".savebutton").click(function(e){
	    if (GECO_validator.requiredFields()== true && GECO_validator.emailFields()==true){
			$scope.user.role = $scope.currentRole;
			$.ajax({
				url:"rest/user/",
				type:"PUT",
				data:"loginobj="+JSON.stringify($scope.user),
				success:function(data){
					alert("success");
				}	
			})
		}
	} );
}]);

gecoControllers.controller('MyProfileCtrl',['$scope', '$routeParams','$http','LoginFactory',function($scope,$routeParams,$http,LoginFactory){
	LoginFactory.checklogin();
	$scope.myuserId= $routeParams.myuserId ;
	
	$http.get('rest/role/').success(function(data){
		$scope.roles= data;
		$http.get('rest/user/'+$scope.myuserId).success(function(data){
			$scope.user= data;
			for (var i=0;i<$scope.roles.length;i++){
				if($scope.roles[i].idrole == $scope.user.role.idrole){
					$scope.currentRole = $scope.roles[i];
				}
			}
		});
	});
	
	$(".savebutton").click(function(e){
		$scope.user.role = $scope.currentRole;
		$.ajax({
			url:"rest/user/",
			type:"PUT",
			data:"loginobj="+JSON.stringify($scope.user),
			success:function(data){
				alert("success");
			}	
		})
	} );
	$(".changepasswordbutton").click(function(e){
		$scope.user.password = $("#oldpassword").val();
		$scope.user.newpassword = $("#newpassword").val();
		$.ajax({
			url:"rest/user/changepassword/",
			type:"PUT",
			data:"userobj="+JSON.stringify($scope.user),
			success:function(data){
				alert("success");
			}	
		})
		
	} );
}]);
/*****
ROLE
***/
gecoControllers.controller('RoleCtrl',["$scope","$http",'LoginFactory',function($scope,$http,LoginFactory){
    $scope.loginuser = LoginFactory.checklogin();
	$scope.rolesaved = true;
	$http.get('rest/role').success(function(data){
		$scope.roles= data;
	});
	$scope.modifyid = 0;
	$scope.modifyRoleElement = function(id){
		$scope.modifyid = id;
	}
	$scope.addRoleElement = function(id){
		$scope.rolesaved = false;
		$scope.roles.push({idrole:0});
	}
	$scope.deleteRoleElement = function(id){
		for(var i=0;i<$scope.roles.length;i++){
			if (id == $scope.roles[i].idrole){
				$scope.deleteRole = $scope.roles[i];
				$.ajax({
						url:"rest/role/",
						type:"DELETE",
						data:"roleobj="+JSON.stringify($scope.deleteRole),
						success:function(data){
								$http.get('rest/role').success(function(data){
										$scope.roles= data;
								});
								
						}	
					})
			}	
		}
	}
	$scope.saveRoles = function(){
		$.ajax({
			url:"rest/role/",
			type:"PUT",
			data:"roles="+JSON.stringify($scope.roles),
			success:function(data){
					$http.get('rest/role').success(function(data){
										$scope.roles= data;
										$scope.rolesaved = true;
										$scope.modifyid = 0;
								});
					
			}	
		})
		
	}
	
	
}]);



