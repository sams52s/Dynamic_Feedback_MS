document.getElementById("dashboard-menu").classList.add("selected");

document.getElementById("feedbackEditBtn").addEventListener("click", function () {
    document.getElementById("showFeedback").classList.add("d-none");
    document.getElementById("showEditFeedback").classList.remove("d-none");
});

document.getElementById("cancelEditBtn").addEventListener("click", function () {
    document.getElementById("showEditFeedback").classList.add("d-none");
    document.getElementById("showFeedback").classList.remove("d-none");
});


$(document).ready(function () {
    function updateStatusColor(status) {
        let color;
        switch (status) {
            case 'PENDING':
                color = 'orange';
                break;
            case 'APPROVED':
                color = 'green';
                break;
            case 'REJECTED':
                color = 'red';
                break;
            case 'COMPLETED':
                color = 'blue';
                break;
            case 'ARCHIVED':
                color = 'gray';
                break;
            case 'DELETED':
                color = 'black';
                break;
            default:
                color = 'black';
        }
        $("#statusText").css("color", color);
    }
    
    let currentStatus = $("#statusText").text().trim();
    updateStatusColor(currentStatus);

    $("#onGoStatus").change(function () {
        let newStatus = $(this).val();
        let feedbackId = "[[${feedbackDto.getId()}]]";

        $.ajax({
            type: "POST",
            url: "/web/feedbacks/update-status",
            data: JSON.stringify({id: feedbackId, status: newStatus}),
            contentType: "application/json",
            success: function (response) {
                $("#statusText").text(newStatus);
                updateStatusColor(newStatus);
                $("#notificationText").text("Status updated successfully!");
            },
            error: function () {
                alert("Failed to update status. Please try again.");
            }
        });
    });
});


$(document).ready(function () {
    $(document).on("click", ".edit-comment", function () {
        let commentDiv = $(this).closest(".comment-item");
        commentDiv.find(".comment-content").hide();
        commentDiv.find(".delete-comment").hide();
        commentDiv.find(".edit-comment-form").removeClass("d-none");
        $(this).hide();
    });
    $(document).on("click", ".cancel-edit", function () {
        let commentDiv = $(this).closest(".comment-item");
        commentDiv.find(".comment-content").show();
        commentDiv.find(".delete-comment").show();
        commentDiv.find(".edit-comment-form").addClass("d-none");
        commentDiv.find(".edit-comment").show();
    });
    $(document).on("click", ".delete-comment", function () {
        let commentId = $(this).data("comment-id");
        let feedbackId = $(this).data("feedback-id");
        $("#deleteMessage").text(`Are you sure you want to delete this comment?`);
        $("#confirmDeleteBtn").attr("href", "/web/comments/" + feedbackId + "/" + commentId + "/delete");
        $("#deleteModal").modal("show");
    });
    $(document).on("submit", ".edit-comment-form", function (e) {
        e.preventDefault();
        let form = $(this);
        let commentDiv = form.closest(".comment-item");

        $.post(form.attr("action"), form.serialize(), function (response) {
            commentDiv.find(".comment-content span:last").text(form.find("textarea").val());
            form.addClass("d-none");
            commentDiv.find(".comment-content").show();
            commentDiv.find(".delete-comment").show();
            commentDiv.find(".edit-comment").show();
        }).fail(function () {
            alert("Error updating comment. Please try again.");
        });
    });
});

