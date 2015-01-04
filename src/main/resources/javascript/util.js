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
