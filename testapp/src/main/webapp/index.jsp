<html>
<body>
<h2 id="h2">Hello World! lkjlkj</h2>
<h3 id="h3">first div</h3>
<div id="div1"></div>
<div id="area1">
    <button id="button1" onClick="updateLabel1('button clicked!');">button1</button>
    <label id="label1">intial label value</label>
</div>
<script>
    function updateLabel1(updateText) {
        var element = document.getElementById("label1");
        element.innerHTML = updateText;
    }
</script>
</body>
</html>