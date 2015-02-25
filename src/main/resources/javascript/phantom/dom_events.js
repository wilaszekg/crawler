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
page.excludeResources = [];
page.onlyResourceToRequest = null;

page.shouldSkipRequestedResource = function (requestUrl) {
    var endsWith = function (source, substring) {
        return source.indexOf(substring, source.length - substring.length) !== -1;
    };

    if (page.onlyResourceToRequest) {
        return !(page.onlyResourceToRequest === requestUrl ||
        page.onlyResourceToRequest + "/" === requestUrl);
    }

    var startOfQuery = requestUrl.indexOf("?");
    var requestUrlWithoutParams = startOfQuery >= 0 ?
        requestUrl.substring(0, startOfQuery) : requestUrl;

    for (var i = 0; i < page.excludeResources.length; i++) {
        if (endsWith(requestUrlWithoutParams, page.excludeResources[i])) {
            return true;
        }
    }

    return false;
};

page.onResourceRequested = function (requestData, networkRequest) {
    if (page.shouldSkipRequestedResource(requestData.url)) {
        networkRequest.abort();
    }

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
    if (page.onlyResourceToRequest && response.redirectURL) {
        page.onlyResourceToRequest = response.redirectURL;
    }
    var indexOfId = page.ajaxRequests.indexOf(response.id);
    if (indexOfId >= 0) {
        page.ajaxRequests.splice(indexOfId, 1);
    }
};