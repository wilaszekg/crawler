    window.removed_nodes = [];

window.onbeforeunload = function (event) {
    event.preventDefault();
    event.stopPropagation();
    return "agh.crawler.events.stop_reload";
};

document.addEventListener("DOMNodeRemoved", function (event) {
    if(event.target.innerText) {
        window.removed_nodes.push(event.target.innerText);
    }
});

window.restoreRemovedNodes = function () {
    for (var i = 0; i < window.removed_nodes.length; i++) {
        window.document.body.appendChild(window.removed_nodes[i]);
    }
};