angular.module('modules.common.shared', [])
.factory('LoginFactory', function () {
	var factory = {};
	factory.checklogin = function(){
		$.ajax({
			url:"rest/user/loggedinuser/",
			type:"GET",
			success:function(data){
				var result = $.parseJSON(data);
				if (result.username != "" && result.username != null){
					factory.checkrole(result);
					$(".myprofilelabel").html(result.username);
					$("#myprofilebutton").click(function(e){
						$(window.location).attr('href', '#/myprofile/'+result.iduser);
					})
					$(".header").css("display","");
				}else{
					$(".header").css("display","none");
					$(window.location).attr('href', '#/');
					
				}
			}	
		});
	}
	factory.checkrole = function(user){
		if (user.role.admin == 'true' || user.role.admin == true ){
			$(".admin_grant").css("display","");
		}else{
			$(".admin_grant").css("display","none");
		}
		if (user.role.create == 'true' || user.role.admin == 'true' ){
			$(".create_grant").css("display","");
		}else{
			$(".create_grant").css("display","none");
		}
		if (user.role.update == 'true' || user.role.admin == 'true' ){
			$(".update_grant").css("display","");
		}else{
			$(".update_grant").css("display","none");
		}
		if (user.role['delete'] == 'true' || user.role.admin == 'true' ){
			$(".delete_grant").css("display","");
		}else{
			$(".delete_grant").css("display","none");
		}
		
	}
	return factory;
	
});