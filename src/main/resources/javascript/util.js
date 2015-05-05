window.click = function (el) {
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
};

/**
 * Gets an XPath for an element which describes its hierarchical location.
 */
window.getXPath = function (element) {
    if (element && element.id)
        return '//*[@id="' + element.id + '"]';
    else
        return window.getElementTreeXPath(element);
};

window.getElementTreeXPath = function (element) {
    var paths = [];

    // Use nodeName (instead of localName) so namespace prefix is included (if any).
    for (; element && element.nodeType == 1; element = element.parentNode) {
        var index = 0;
        // EXTRA TEST FOR ELEMENT.ID
        if (element && element.id) {
            paths.splice(0, 0, '/*[@id="' + element.id + '"]');
            break;
        }

        for (var sibling = element.previousSibling; sibling; sibling = sibling.previousSibling) {
            // Ignore document type declaration.
            if (sibling.nodeType == Node.DOCUMENT_TYPE_NODE)
                continue;

            if (sibling.nodeName == element.nodeName)
                ++index;
        }

        var tagName = element.nodeName.toLowerCase();
        var pathIndex = (index ? "[" + (index + 1) + "]" : "");
        paths.splice(0, 0, tagName + pathIndex);
    }

    return paths.length ? "/" + paths.join("/") : null;
};

window.scrollToBottom = function () {
    window.scrollTo(0, document.body.scrollHeight);
};

window.elementSpace = function (element) {
    var position = element.getBoundingClientRect();
    return (position.right - position.left) * (position.bottom - position.top);
};

window.canBeAjaxLink = function (element) {
    var href = element.attributes['href'];
    return !element.hostname || (href && href.value.substring(0, 1) == '#');
};

/**
 *
 * @param {HTMLElement} element
 */
window.getAutonomousElements = function () {
    var findAutonomousElements = function (element) {
        var elementArea = window.elementSpace(element);
        // to small elements shouldn't trigger actions
        if (elementArea < 100) {
            return [];
        }

        if (element.target == '_blank') {
            return [null];
        }

        if (['a', 'button', 'input'].indexOf(element.tagName.toLowerCase()) >= 0) {
            if (window.canBeAjaxLink(element)) {
                return [element];
            } else {
                return [null];
            }
        }

        var children = element.children;
        var autonomousChildren = [];

        for (var i = 0; i < children.length; i++) {
            autonomousChildren = autonomousChildren.concat(findAutonomousElements(children[i]));
        }

        return autonomousChildren.length == 0 ? [element] : autonomousChildren;
    };

    return findAutonomousElements(document.body).filter(function (element) {
        return element != null;
    });
};
