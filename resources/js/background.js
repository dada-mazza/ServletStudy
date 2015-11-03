function getRandomColor() {
    var letters = '0123456789ABCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

function foo() {
    var date = new Date();
    var sec = date.getSeconds();
    document.body.style.backgroundColor = getRandomColor();
    setTimeout("foo()", 5000);
}

foo();
