function checkPassword(form) {
   var $errorMsg = $("#errorMsg");
   var userName = form[form.id + ":userName"].value;	
   var password = form[form.id + ":password"].value;
   var passwordConfirm = form[form.id + ":passwordConfirm"].value;
   var $form = $("form");
   if($errorMsg.length > 0) {
	   $errorMsg.remove();
   }
   if(userName === '' || password === '') {
	   $form.after($("<span></span>").text("user name and password cannot be empty").attr({id: "errorMsg", class: "error"}));
	   return false;
   } 
   else if(password !== passwordConfirm) {
	   $form.after($("<span></span>").text("Password and password confirm fields don't match").attr({id: "errorMsg", class: "error"}));
	   return false;
   }
   return true;
      
}
