
function getHTMLElementsAtPoint(x,y) {
    alert("getHTMLElementsAtPoint");
    var tags = "";
    var e;
    var offset = 0;
    while ((tags.search(",(A|IMG),") < 0) && (offset < 20)) {
        tags = ",";
        e = document.elementFromPoint(x,y+offset);
        while (e) {
            if (e.tagName) {
                tags += e.innerHTML + ',';
            }
            e = e.parentNode;
        }
        if (tags.search(",(A|IMG),") < 0) {
            e = document.elementFromPoint(x,y-offset);
            while (e) {
                if (e.tagName) {
                    tags += e.innerHTML + ',';
                }
                e = e.parentNode;
            }
        }
        offset++;
    }
    return tags;
    
}

function getLinkSRCAtPoint(x,y) {
    alert("getLinkSRCAtPoint");
    var tags = "";
    var e = "";
    var offset = 0;
    while ((tags.length == 0) && (offset < 20)) {
        e = document.elementFromPoint(x,y+offset);
        while (e) {
            if (e.src) {
                tags += e.src;
                break;
            }
            e = e.parentNode;
        }
        if (tags.length == 0) {
            e = document.elementFromPoint(x,y-offset);
            while (e) {
                if (e.src) {
                    tags += e.src;
                    break;
                }
                e = e.parentNode;
            }
        }
        offset++;
    }
    return tags;
    
}
