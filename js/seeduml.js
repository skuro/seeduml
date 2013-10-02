(function autosubmit(){
    var composer = document.getElementById("composer");
    var textarea = document.getElementById("plantuml");
    var graph    = document.getElementById("graph");
    var errmsg   = document.getElementById("error");

    var updateImg = function() {
        var date = new Date();
        var rnd = "" + date.getFullYear() +
            date.getMonth() +
            date.getDay() +
            date.getHours() +
            date.getMinutes() +
            date.getSeconds() +
            date.getMilliseconds();
        graph.src = "/img/" + document.location.pathname.split("/")[1] + ".png#" + rnd;
    };

    var submit = function(){
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if(xhr.status === 200) {
                    error.style.visibility = "hidden";
                    graph.style.visibility = "visible";
                    updateImg();
                } else {
                    graph.style.visibility = "hidden";
                    error.innerText = xhr.responseText;
                    error.style.visibility = "visible";
                }
            }
        };

        xhr.open("POST", document.location);
        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        var params = "plantuml=" + encodeURIComponent(textarea.value);
        xhr.send(params);
    };

    var timer = null;
    textarea.onkeyup = function(){
        clearTimeout(timer);
        setTimeout(submit, 300);
    };
})();

var getStyle = function(element, style) {
    if(window.getComputedStyle){
        // Chrome, FF, Opera, Safari
        return window.getComputedStyle(element)[style];
    } else if(element.currentStyle) {
        // IE
        return element.currentStyle[style];
    }

    // revert to the declared style
    return element.style[style];
}

var toggle = function(element) {
    console.log(getStyle(element, "visibility"));
    element.style.visibility = (getStyle(element, "visibility") == "visible") ? "hidden" : "visible";
};

(function help(){
    var helpIcon = document.getElementById("help");
    helpIcon.onclick = function() {
        var helpOverlay = document.getElementById("help-overlay");
        toggle(helpOverlay);
    };
})();
