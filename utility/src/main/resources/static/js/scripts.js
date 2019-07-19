

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

function showEventSettings(id) {
	$.get("/event/settings/" + id).done(function(data) {

		$("#searchModalHolder").html(data);
		$("#eventSettingsModal").modal("show");
		setTimeout(
				function() {
					console.log("Date");
					$("#datetimepicker")
							.datetimepicker(
									'format',
									"DD.MM.YYYY HH:mm");
				}, 1000);
	});
}

function openEventModal() {
	$.get("/event").done(function(data) {
		$("#searchModalHolder").html(data);
		$("#eventModal").modal("show");
		setTimeout(
				function() {
					console.log("Date");
					$("#datetimepicker")
							.datetimepicker(
									'format',
									"DD.MM.YYYY HH:mm");
				}, 1000);
	});
}

function removeMember(id) {
	$.get("/member/remove/" + id).done(function(data) {
		$("#memberModal").modal("hide");
		$("body").html(data);
		openAlertModal("Successfull deleted", "success");

	});
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

function* counter(index){
	while(true){
		yield index++;
	}
}

var iterator = counter(0);

function eventSettingsAddMember() {
	var value = $('.username').val();
	var i = iterator.next().value;

	var x = $("<div></div>")
			.addClass("row")
			.addClass("name-" + i)
			.addClass("pb-2")
			.append(
					($("<div></div>").addClass("col-9").append(value))
							.append($("<input>").attr("name","names[]").attr("type", "hidden").attr(
									"value", value)))
			.append(
					($("<div></div>").addClass("col-3")
							.append($(
									'<span class="float-right" aria-hidden="true">&times;</span>')
									.attr("onclick", "eventSettingsRemoveMember('" + i + "')"))));

	$(".m-b").append(x);

}

function eventSettingsRemoveMember(id){
	$(".name-" + id).remove();
}

$(document).ready(function() {
	$('<div id="searchModalHolder"></div>').appendTo(document.body);

})

$(document).ajaxStop(function() {
	$(".card").each(function(i) {
		$(this).delay(225 * i).show(500);
	});
});