window.removedNodes = [];
window.addedNodesCount = 0;

window.onbeforeunload = function (event) {
    event.preventDefault();
    event.stopPropagation();
    return "agh.crawler.events.stop_reload";
};

document.addEventListener("DOMNodeRemoved", function (event) {
    if (event.target.innerText) {
        window.removedNodes.push([event.target.innerText, window.getXPath(event.target)]);
    }
});

document.addEventListener("DOMNodeInserted", function (event) {
    window.addedNodesCount++;
});
