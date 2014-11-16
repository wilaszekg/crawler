window.cssIdPath = function (el) {
    if (!(el instanceof Element)) return;
    var path = [];
    while (el.nodeType === Node.ELEMENT_NODE && el.nodeName.toLowerCase() != "body") {
        var selector = el.nodeName.toLowerCase();
        if (el.id) {
            selector += '#' + el.id;
        } else {
            var sib = el, nth = 1;
            while (sib.nodeType === Node.ELEMENT_NODE && (sib = sib.previousSibling) && nth++);
            selector += ":nth-child(" + nth + ")";
        }
        path.unshift(selector);
        el = el.parentNode;
    }
    return path.join(" > ");
};

window.cssPath = function (el) {
    if (!(el instanceof Element)) return;
    var path = [];
    while (el.nodeType === Node.ELEMENT_NODE && el.nodeName.toLowerCase() != "body") {
        var selector = el.nodeName.toLowerCase();
        if (el.id) {
            selector += '#' + el.id;
        }
        for (var i = 0; i < el.classList.length; i++) {
            selector += '.' + el.classList[i];
        }
        path.unshift(selector);
        el = el.parentNode;
    }
    return path.join(" > ");
};