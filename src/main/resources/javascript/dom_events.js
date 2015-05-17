window.removedNodes = [];
window.removedLinks = [];
window.addedNodesCount = 0;

window.onbeforeunload = function (event) {
    event.preventDefault();
    event.stopPropagation();
    return "agh.crawler.events.stop_reload";
};

document.findChildLinks = function (element) {
    var links = [];
    if (element.href) {
        links.push(element.href);
    }
    var children = element.childNodes;
    for (var i = 0; i < children.length; i++) {
        var childLinks = document.findChildLinks(children[i]);
        for (var j = 0; j < childLinks.length; j++) {
            links.push(childLinks[j]);
        }
    }
    return links;
};

document.addEventListener("DOMNodeRemoved", function (event) {
    if (event.target.innerText) {
        window.removedNodes.push([event.target.innerText, window.getXPath(event.target)]);
    }
    var links = document.findChildLinks(event.target);
    for (var i = 0; i < links.length; i++) {
        window.removedLinks.push(links[i]);
    }
});

document.addEventListener("DOMNodeInserted", function (event) {
    window.addedNodesCount++;
});
