
var counter = 1;
$(document).ready(function () {

    $("#addField").click(function () {
        let name = prompt("Please enter the name of the new field:")

        if (name === null) return false;

        let element = '<div class="col-md-6 col-sm-6 col-12 ">'
        element += '<div class="form-group">'
        element += '<div class="form-group">'
        element += `<label for="${name}"><h6 class="text-primary">${name}</h6></label>`
        element += '<button type="button" class="btn btn-close float-right remove">X</button>'
        element += `<input type="text" class="form-control" id="${name}">`
        element += '</div>'
        element += '</div>'

        if (counter > 4) {
            alert("Only 4 additional fields allowed");
            return false;
        }
        $('#fields').append(element);
        counter++;
    });
});

$(document).on('click', ".remove", function (e) {
    $(this).parent().parent().parent().remove();
    counter--;
});

