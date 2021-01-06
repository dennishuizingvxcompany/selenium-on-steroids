<!DOCTYPE html>
<html lang="en">
<head><title>Test page</title></head>
<body>
<h2 id="h2">Hello World!</h2>
<h3 id="h3">first div</h3>
<div id="div1"></div>
<div id="area1">
    <button id="button1" onClick="updateLabel1();">button1</button>
    <label id="label1">intial label value</label>
</div>
<div>
    <button id="button2">button2</button>
</div>
<div>
    <button id="button3">button3</button>
</div>
<div>
    <selfMadeTagName>homeMade</selfMadeTagName>
</div>
<script>
    function updateLabel1() {
        var element = document.getElementById("label1");
        if (element.innerHTML == "button clicked") {
        element.innerHTML = "intial label value";
        } else {
        element.innerHTML = "button clicked"
        }
    }
</script>
</body>
</html>