function openMemberModal(id) {
	$.get("/member/" + id).done(function(data) {
		$("#memberModalHolder").html(data);
		$("#memberModal").modal("show");
	})
}
function openSearchModal() {
	$.get("/search").done(function(data) {
		$("#searchModalHolder").html(data);
		$("#searchModal").modal("show");
	})
}

function openEventModal(){
	$.get("/event").done(function(data){
		$("#searchModalHolder").html(data);
		$("#eventModal").modal("show");
	
	})
}

$(document).ready(function() {
	$('<div id="searchModalHolder"></div>').appendTo(document.body);

})