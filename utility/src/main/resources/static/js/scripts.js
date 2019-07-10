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

function openAlertModal(param) {

	var sentences = param.split('.');
	var type = "info";

	console.log(sentences.length);
	if (sentences.length > 1) {
		type = "danger";
	}

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
		$(".container.main").append(data);
		openAlertModal("Event was successfully deleted");
	});
}

function loadEvents() {
	$.get("/events").done(function(data) {
		$(".container.main").append(data);
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