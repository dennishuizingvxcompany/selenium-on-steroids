<!DOCTYPE html>
<html lang="en">
<head><title>Test page</title></head>
<body>
<h2 id="h2">Hello World!</h2>
<h3 id="h3">first div</h3>
<div id="div1"></div>
<div id="area1">
    <button id="button1" onClick="updateLabel1();">button1</button>
    <label id="label1">initial label value</label>
</div>
<div>
    <button id="button2">button2</button>
</div>
<div>
    <button id="button3">button3</button>
</div>
<div>
    <selfMadeTagName class="searchForClassName" name="nameOfElement">homeMade</selfMadeTagName>
    <div>
        <a href="http://some.url">linkText</a>
    </div>
</div>
<div>
    <div><button id="hiddenButton">hidden button</button></div>
    <div><button id="toggleButton" onClick="toggleButton()">toggle button</button></div>
</div>
<p>
    <div>
        <p>Select a value:</p>
        <input type="radio" id="radioButton1" value="radio1" name="options"/>
        <label for="radioButton1">option1</label>
        <input type="radio" id="radioButton2" value="radio2" name="options" disabled>
        <label for="radioButton2">option2</label>
    </div>
</p>
<p>
    <div>
        <input id="searchBox" value="search input" type="text"/><br>
        <input id="getTextOfElement" onload="updateText()" value="text within an element"/>
    </div>
</p>
<div>
    <textarea>
    </textarea>
</div>
<script>
    function updateLabel1() {
        var element = document.getElementById("label1");
        if (element.innerHTML == "button clicked") {
        element.innerHTML = "initial label value";
        } else {
        element.innerHTML = "button clicked"
        }
    }

    function toggleButton() {
        var x = document.getElementById("hiddenButton");
            if (x.style.display === "none") {
              x.style.display = "block";
            } else {
              x.style.display = "none";
            }
    }

    function updateText() {
        setTimeout(function () {
            if (newState == -1) {
                document.getElementById("getTextOfElement").innerHTML = "Updated text of element";
            }
        }, 5000);
    }
</script>
</body>
</html>