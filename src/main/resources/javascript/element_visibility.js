/**
 *
 * @param {HTMLElement} element
 */
window.isElementDisplayed = function (element) {
    var elementCenter = getElementCenter(element);
    var visibleElement = document.elementFromPoint(elementCenter.x, elementCenter.y);

    while (visibleElement) {
        if (visibleElement === element) {
            return true;
        }
        visibleElement = visibleElement.parentElement;
    }
    return false;
};

/**
 *
 * @param {HTMLElement} element
 */
window.getElementCenter = function (element) {
    var position = element.getBoundingClientRect();
    return  {
        x: (position.left + position.right) / 2,
        y: (position.bottom + position.top) / 2
    }
};

window.elementSpace = function (element) {
    var position = element.getBoundingClientRect();
    return (position.right - position.left) * (position.bottom - position.top);
};

window.realDocumentHeight = function () {
    var body = document.body;
    return Math.max(body.scrollHeight, body.offsetHeight);
};

window.realDocumentWidth = function () {
    var body = document.body;
    return Math.max(body.scrollWidth, body.offsetWidth);
};