window.getCandidateElements = function (tags) {
    var nodes = document.querySelectorAll(tags);
    var candidates = [];
    var hiddenElements = [];


    var documentHeight = realDocumentHeight();
    var documentWidth = realDocumentWidth();
    var scrollTopPosition = 0;
    var scrollLeftPosition = 0;
    console.log("Number of elements: " + nodes.length);

    do {
        do {
            window.scrollTo(scrollLeftPosition, scrollTopPosition);

            for (var i = 0; i < nodes.length; i++) {
                var node = nodes[i];
                var center = getElementCenter(node);

                if (center.x >= 0 &&
                    center.x < window.innerWidth &&
                    center.y >= 0 &&
                    center.y < window.innerHeight) {

                    /*
                    checking if element is already in one of lists is necessary for elements with fixed position
                     */
                    if (isElementDisplayed(node)) {
                        if (candidates.indexOf(node) < 0) {
                            candidates.push(node);
                            var indexOfNode = hiddenElements.indexOf(node);
                            if (indexOfNode > -1) {
                                hiddenElements.splice(indexOfNode, 1);
                            }
                        }
                    } else {
                        if (hiddenElements.indexOf(node) < 0) {
                            hiddenElements.push(node);
                        }
                    }
                }
            }

            scrollLeftPosition += window.innerWidth;
        } while (window.pageXOffset + window.innerWidth < documentWidth)

        scrollTopPosition += window.innerHeight;
    } while (window.pageYOffset + window.innerHeight < documentHeight);


    return JSON.stringify({
        elements: candidates.map(getNodeDescriptionEntity),
        hiddenElements: hiddenElements.map(getNodeDescriptionEntity)
    });
};

window.getNodeDescriptionEntity = function (node) {
    return {
        "cssPath": cssPath(node),
        "cssIdPath": cssIdPath(node),
        "position": node.getBoundingClientRect()
    };
};