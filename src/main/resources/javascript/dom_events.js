window.removedNodes = [];
window.addedNodesCount = 0;

window.onbeforeunload = function (event) {
    event.preventDefault();
    event.stopPropagation();
    return "agh.crawler.events.stop_reload";
};

document.addEventListener("DOMNodeRemoved", function (event) {
    if (event.target.innerText) {
        window.removedNodes.push(event.target.innerText);
    }
});

document.addEventListener("DOMNodeInserted", function (event) {
    window.addedNodesCount++;
});

window.restoreRemovedNodes = function () {
    for (var i = 0; i < window.removedNodes.length; i++) {
        window.document.body.appendChild(window.removedNodes[i]);
    }
};