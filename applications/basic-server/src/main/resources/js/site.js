function moveCities() {
    var source = document.getElementById('source');
    var destination = document.getElementById('destination');
    for (var i = 0; i < source.options.length; i++) {
        if (source.options[i].selected) {
            var option = source.options[i];
            destination.appendChild(option);
            i--;
        }
    }
}

function submitCity() {
    var source = document.getElementById('source');
    for (var i = 0; i < source.options.length; i++) {
        if (source.options[i].selected) {
            var option = source.options[i];
            destination.appendChild(option);
            i--;
        }
    }
}

function displayInput() {
    var userInput = document.getElementById('userInput').value;
    
    document.getElementById('displayArea').innerText = userInput;
}

function postMvpString() {
}