$(document).ready(function(){
	$('#search-btn').click(function(){
		if($.trim($('#search-term').val()).length==0) return false;
		self.location=basePath+"/search/feed?term="+$.trim($('#search-term').val());
	});


});