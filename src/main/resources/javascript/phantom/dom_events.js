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

page.ajaxRequests = [];

page.onResourceRequested = function (requestData, networkRequest) {
    var xRequestedWith = undefined;
    for (var i = 0; i < requestData.headers.length; i++) {
        if (requestData.headers[i].name == 'X-Requested-With') {
            xRequestedWith = requestData.headers[i];
            break;
        }
    }

    if (xRequestedWith && xRequestedWith.value == 'XMLHttpRequest') {
        page.ajaxRequests.push(requestData.id);
    }
};

page.onResourceReceived = function (response) {
    var indexOfId = page.ajaxRequests.indexOf(response.id);
    if (indexOfId >= 0) {
        page.ajaxRequests.splice(indexOfId, 1);
    }
};