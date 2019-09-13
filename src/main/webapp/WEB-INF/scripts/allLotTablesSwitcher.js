var select = document.getElementsByName("selectList");
var lotsTable = document.getElementById("lotsTable");
var myLotsTable = document.getElementById("myLotsTable");

function changeTable(){
    if(select[0].checked) {
        myLotsTable.hidden = true;
        lotsTable.hidden = false;
    }else {
        lotsTable.hidden = true;
        myLotsTable.hidden = false;
    }
}