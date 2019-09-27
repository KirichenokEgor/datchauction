var select = document.getElementsByName("selectList");
var lotsTable = document.getElementById("lotsTable");
var myLotsTable = document.getElementById("myLotsTable");
var showLots = document.getElementById("showLots");
var showMyLots = document.getElementById("showMyLots");

showLots.onclick = changeTable;
showMyLots.onclick = changeTable;

function changeTable(){
    if(select[0].checked) {
        myLotsTable.hidden = true;
        lotsTable.hidden = false;
        rows = rows1;
        lots = lots1;
    }else {
        lotsTable.hidden = true;
        myLotsTable.hidden = false;
        rows = rows2;
        lots = lots2;
    }
    renderTable();
}