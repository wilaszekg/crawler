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

page.nonAjaxSuffixes = [".js", ".css", ".png", ".jpg"];
page.ajaxRequests = [];
page.ajaxCounter = 0;
page.excludeResources = [];
page.onlyResourceToRequest = null;

page.urlEndsWithOneOf = function (url, suffixes) {
    var endsWith = function (source, substring) {
        return source.indexOf(substring, source.length - substring.length) !== -1;
    };

    var startOfQuery = url.indexOf("?");
    var requestUrlWithoutParams = startOfQuery >= 0 ?
        url.substring(0, startOfQuery) : url;

    for (var i = 0; i < suffixes.length; i++) {
        if (endsWith(requestUrlWithoutParams, suffixes[i])) {
            return true;
        }
    }

    return false;
};

page.shouldSkipRequestedResource = function (requestUrl) {
    if (page.onlyResourceToRequest) {
        return !(page.onlyResourceToRequest === requestUrl ||
        page.onlyResourceToRequest + "/" === requestUrl);
    }

    return page.urlEndsWithOneOf(requestUrl, page.excludeResources);
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

    var hasXhrHeader = xRequestedWith && xRequestedWith.value == 'XMLHttpRequest';
    if (hasXhrHeader || !page.urlEndsWithOneOf(requestData.url, page.nonAjaxSuffixes)) {
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
        page.ajaxCounter = page.ajaxCounter + 1;
    }
};