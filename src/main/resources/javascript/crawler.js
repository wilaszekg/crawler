var i;
var bodyChildren = document.body.children;

for (i = 0; i < bodyChildren.length; i++) {
    bodyChildren[i].style.position = 'relative';
}

var elementsToCrawl = window.getAutonomousElements(document.body);

for (i = 0; i < elementsToCrawl.length; i++) {
    var element = elementsToCrawl[i];
    if (element.dataset.crawlerVisited != "true") {
        element.dataset.crawlerVisited = "true";
        click(element);
    }
}