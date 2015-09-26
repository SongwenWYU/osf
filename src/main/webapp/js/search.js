$(document).ready(function(){
	$('#search-btn').click(function(){
		var search_term = $('#search-term').val();


		$.ajax({
			url: basePath+'/search/event',
			type: 'POST',
			dataType: 'json',
			data:{
				term:search_term
			}
		})
		.success(function(data){
			
		})			


	});


});