var socketConn = new WebSocket('ws://'+window.location.host+'/app/usersManager');
var resultsCount;
var pageNumber = 1;
var pageSize = 20;
var usersList;
var prevButton = document.getElementById("prevPage");
var nextButton = document.getElementById("nextPage");
var pageLabel = document.getElementById("pageNumber");
prevButton.disabled = true;
nextButton.disabled = true;
prevButton.onclick = prevPage;
nextButton.onclick = nextPage;
socketConn.onmessage = function(event){
    var command = event.data.slice(0,2);
    var parameters = event.data.slice(3);
    switch(command){
    case "RC":                  //RC - results count
        resultsCount = Number(parameters);
        pageNumber = 1;
        prevButton.disable = true;
        pageLabel.innerHTML = pageNumber;
        getPage();
        break;
    case "SE":                  //SE - say error
        alert(parameters);
        break;
    case "RP":                  //RP - render page
        usersList = JSON.parse(parameters);
        if(pageNumber*pageSize < resultsCount) nextButton.disabled = false;
        renderList();
        break;
    case "OS":                  //OS - operation success
        getPage();
        break;
    default:
        console.log("Unknown message: " + parameters);
    }
}
socketConn.onclose =  function(event){
    alert("Connection with server lost.\nReturning to home...");
    history.back();
}
function prevPage(){
    pageNumber--;
    pageLabel.innerHTML = pageNumber;
    getPage();
    if(pageNumber==1)prevButton.disabled = true;
    nextButton.disabled = false;
}
function nextPage(){
    pageNumber++;
    pageLabel.innerHTML = pageNumber;
    getPage();
    prevButton.disabled = false;
    if(pageNumber*pageSize >= resultsCount)nextButton.disabled = true;
}
function getPage(){
    socketConn.send("getPage " + pageSize + " " + pageNumber);
}
function renderList(){
   var usersTable = document.getElementById("usersList");
   var rows = document.getElementsByClassName("userRow");
   var rowsCount = rows.length;
   for(var i = 0; i < rowsCount; i++)
        rows[0].remove();
   for(var user of usersList){
       var tr = document.createElement("tr");
       tr.setAttribute("class","userRow");
       var td1 = document.createElement("td");
       td1.setAttribute("class","usersRow w3-center");
       var td2 = document.createElement("td");
       td2.setAttribute("class","usersRow w3-center");
       var td3 = document.createElement("td");
       td3.setAttribute("class","usersRow");
       var td4 = document.createElement("td");
       td4.setAttribute("class","usersRow");
       td1.innerHTML = user.username;
       td2.innerHTML = user.role;
       var changeRoleButton = document.createElement("button");
       changeRoleButton.setAttribute("type","button");
       changeRoleButton.innerHTML = "change role";
       changeRoleButton.setAttribute("onclick","changeRole(this)");
       changeRoleButton.setAttribute("class","w3-btn w3-block w3-theme-l3");
       var banButton = document.createElement("button");
       banButton.setAttribute("type","button");
       if(user.enabled)
         banButton.innerHTML = "ban";
       else banButton.innerHTML = "unban";
       banButton.setAttribute("onclick","banUser(this)");
       banButton.setAttribute("class","w3-btn w3-block w3-theme-l3");
       td3.appendChild(changeRoleButton);
       td4.appendChild(banButton);
       tr.appendChild(td1); tr.appendChild(td2); tr.appendChild(td3); tr.appendChild(td4);
       usersTable.appendChild(tr);
   }
}
function changeRole(button){
    var row = button.parentElement.parentElement;
    var roleSelect = document.getElementById("roleSelect");
    if(roleSelect != null){
        var prevRow = roleSelect.parentElement.parentElement;
        prevRow.cells[2].children[0].remove();
        prevRow.cells[3].children[0].remove();
        var changeRoleButton = document.createElement("button");
        changeRoleButton.setAttribute("type","button");
        changeRoleButton.innerHTML = "change role";
        changeRoleButton.setAttribute("onclick","changeRole(this)");
        changeRoleButton.setAttribute("class","w3-btn w3-block w3-theme-l3");
        var banButton = document.createElement("button");
        banButton.setAttribute("type","button");
        banButton.setAttribute("class","w3-btn w3-block w3-theme-l3");
        var user;
        for(var usr of usersList)
           if(usr.username===prevRow.cells[0].innerHTML)user = usr;
        if(user.enabled)
          banButton.innerHTML = "ban";
        else banButton.innerHTML = "unban";
        banButton.setAttribute("onclick","banUser(this)");
        prevRow.cells[2].appendChild(changeRoleButton);
        prevRow.cells[3].appendChild(banButton);
    }
    roleSelect = document.createElement("select");
    roleSelect.setAttribute("id","roleSelect");
    roleSelect.setAttribute("class","w3-input");
    var option = document.createElement("option");
    option.setAttribute("value","buyer");
    option.innerHTML = "buyer";
    roleSelect.appendChild(option)
    option = document.createElement("option");
    option.setAttribute("value","seller");
    option.innerHTML = "seller";
    roleSelect.appendChild(option)
    option = document.createElement("option");
    option.setAttribute("value","admin");
    option.innerHTML = "admin";
    roleSelect.appendChild(option);
    roleSelect.value = row.cells[1].innerHTML;
    row.cells[2].children[0].remove();
    row.cells[3].children[0].remove();
    row.cells[2].appendChild(roleSelect);
    var confirmButton = document.createElement("button");
    confirmButton.setAttribute("type","button");
    confirmButton.setAttribute("class","w3-btn w3-block w3-theme-l3");
    confirmButton.setAttribute("onclick","confirmRole(this)");
    confirmButton.innerHTML = "change";
    row.cells[3].appendChild(confirmButton);
}
function confirmRole(button){
    var row = button.parentElement.parentElement;
    socketConn.send("changeRole " + row.cells[0].innerHTML + " " + document.getElementById("roleSelect").value);
    search();
}
function search(){
    var input = document.getElementById("searchWords");
    prevButton.disabled=true;
    socketConn.send("search " + input.value);
}
function banUser(button){
    var username = button.parentElement.parentElement.cells[0].innerHTML;
    if(button.innerHTML==="ban")
    socketConn.send("banUser " + username);
    else socketConn.send("unbanUser " + username);
    search();
}