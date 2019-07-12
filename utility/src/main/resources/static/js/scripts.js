function openMemberModal(id) {
	console.log("Öffne Member " + id);
	$.get("/member/" + id).done(function(data) {
		$("#memberModalHolder").html(data);
		$("#memberModal").modal("show");
	})
}
function openSearchModal() {
	console.log("Öffne Search Modal");
	$.get("/search").done(function(data) {
		$("#searchModalHolder").html(data);
		$("#searchModal").modal("show");
	})
}

function openEventModal() {
	console.log("Öffne Event Modal");
	$.get("/event").done(function(data) {
		$("#searchModalHolder").html(data);
		$("#eventModal").modal("show");

	})
}

function openAlertModal(content, type) {

	var sentences = content.split('.');

	sentences.forEach(function(each) {
		if (each.length > 0) {

			$.notify({
				message : each,
			}, {
				type : type,
				placement : {
					from : "bottom",
					align : "right"
				}
			})

		}

	});

	/*
	 * console.log("Öffne Alert Modal"); $.get("/alert?message=" +
	 * message).done(function(data){ $("#searchModalHolder").html(data);
	 * $("#alertModal").modal("show"); });
	 */

}

function removeEvent(id) {
	console.log("remove: " + id);
	$.get("/event/remove/" + id).done(function(data) {
		$(".event").remove();
		$(".main").html(data);
		openAlertModal("Event was successfully deleted", "success");
	});
}

function loadEvents(name, page) {
	var v = "/events?";
	if (name != undefined) {
		v += "name=" + name + "&";
	}
	if (page != undefined) {
		v += "page=" + page;
	}

	$.get(v).done(function(data) {
		$(".main").html(data);
	});

}

$(document).ready(function() {
	$('<div id="searchModalHolder"></div>').appendTo(document.body);
})

$(document).ajaxStop(function() {
	$(".card").each(function(i) {
		$(this).delay(225 * i).show(500);
	});
});