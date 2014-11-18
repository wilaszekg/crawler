var webPage = require('webpage');
var fs = require('fs');

var page = webPage.create();

/*var resourceToBlock = function (requestData) {
 };

 function sleepFor(sleepDuration) {
 var now = new Date().getTime();
 while (new Date().getTime() < now + sleepDuration) { *//* do nothing *//*
 }
 }

 page.viewportSize = { width: 1920, height: 1080 };*/
/*
 page.settings.userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36";
 page.customHeaders = {
 'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36'
 };
 */

/*page.onResourceRequested = function (requestData, networkRequest) {
    console.log('Request (#' + requestData.id + '): ' + JSON.stringify(requestData));
    console.log(JSON.stringify(networkRequest));
};

page.onResourceReceived = function (response) {
    console.log('Response (#' + response.id + ', stage "' + response.stage + '"): ' + JSON.stringify(response));
};*/

function click(el) {
    var ev = document.createEvent("MouseEvent");
    ev.initMouseEvent(
        "click",
        true /* bubble */, true /* cancelable */,
        window, null,
        0, 0, 0, 0, /* coordinates */
        false, false, false, false, /* modifier keys */
        0 /*left*/, null
    );
    el.dispatchEvent(ev);
}

page.onConsoleMessage = function (msg) {
    console.log(msg);
};

page.onConfirm = function (msg) {
    console.log("confirm: " + msg);
    return false;
};

page.open("http://localhost:8000", function (status) {
    console.log("loaded");
    //console.log(page.settings.javascriptEnabled);
    /*page.render('example.png');
     fs.write('page.html', page.evaluate(function () {
     return document.getElementsByTagName('body')[0].innerHTML;
     }), 'w')*/

    console.log("this: " + this);

    page.evaluate(function () {
        document.addEventListener("DOMNodeInserted", function () {
            console.log("NODE INSERTED");
        });
    });

    page.evaluate(function () {
        function click(el) {
            var ev = document.createEvent("MouseEvent");
            ev.initMouseEvent(
                "click",
                true /* bubble */, true /* cancelable */,
                window, null,
                0, 0, 0, 0, /* coordinates */
                false, false, false, false, /* modifier keys */
                0 /*left*/, null
            );
            el.dispatchEvent(ev);
        }

        var link = document.querySelector("a");
        click(link);
        var outsider = document.querySelector("a.dssdfsdfsd");
        click(outsider);
    });
    phantom.exit();
});

/*
 page.onResourceRequested = function (requestData, networkRequest) {
 //if (requestData.url.endsWith(".png"))
 //console.log('Request (#' + requestData.id + '): ' + JSON.stringify(requestData));
 };

 page.onLoadFinished = function (status) {
 console.log('Status: ' + status);
 console.log('readyState: ' + document.readyState);
 while (page.evaluate(function () {return document.readyState;}) != "complete") {
 sleepFor(1000);
 }

 fs.write('content.txt', page.plainText, 'w');
 page.render('example.png');
 phantom.exit();
 };*/
