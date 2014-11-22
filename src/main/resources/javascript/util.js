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
 * @return height of page before scroll
 */
window.getCurrentHeightAndScrollToBottom = function () {
    var height = document.body.scrollHeight;
    window.scrollTo(0, height);
    return height;
};