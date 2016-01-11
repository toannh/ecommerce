testjs = {};
testjs.init = function(){
	alert('init oy nha');
	var request = new Object();
	request.username = "store247";
	request.password = "123456";
	testjs.callAPI(request);
};
testjs.callAPI = function(data_request){
	alert('callAPI oy nha');
	$.ajax({
		  dataType:"json",
		  url: "http://beta.chodientu.vn/app/user/signin.api",
		  contentType: 'application/json',
		  data :  data_request,
		  method: 'POST',
		  success: function(result){
		  	alert('success');
		  }
	});	
};