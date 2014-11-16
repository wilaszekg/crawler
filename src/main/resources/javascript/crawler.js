var elementsToCrawl = document.querySelectorAll("a");


var shouldBeTriggered = function (element) {
    if (element.target == '_blank') {
        return false;
    }
    var href = element.attributes['href'];
    if (href && href.value != '#') {
        return false;
    }

    return true;
};

var i = 0;
var clickedElements = 0;
while (i < elementsToCrawl.length) {
    var element = elementsToCrawl[i];
    if (shouldBeTriggered(element)) {
        click(element);
        clickedElements++;
    }
    i++;
}