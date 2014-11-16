/*
 this is current page object of PhantomJS session
 */
var page = this;

page.onConfirm = function (msg) {
    if (msg == "agh.crawler.events.stop_reload") {
        return false;
    }

    return false;
};

page.evaluate(function () {
    window.ajaxRequests = [];
});

page.onResourceRequested = function (requestData, networkRequest) {
    var xRequestedWith = undefined;
    for (var i = 0; i < requestData.headers.length; i++) {
        if (requestData.headers[i].name == 'X-Requested-With') {
            xRequestedWith = requestData.headers[i];
            break;
        }
    }

    console.log("AJAX request: " + requestData.id);
    if (xRequestedWith && xRequestedWith.value == 'XMLHttpRequest') {
        page.evaluate(function (id) {
            window.ajaxRequests.push(id);
        }, requestData.id);
    }
};

page.onResourceReceived = function (response) {
    page.evaluate(function (id) {
        var indexOfId = window.ajaxRequests.indexOf(id);
        if (indexOfId >= 0) {
            page.ajaxRequests.splice(indexOfId, 1);
        }
    }, response.id);
};