function openMemberModal(id) {
	console.log("memberModal");
	$.get("/member/" + id).done(function(data) {
		$("#memberModalHolder").html(data);
		$("#memberModal").modal("show");
	})
}