class Lot{
    constructor(id,name,price,firstLink,secondLink){
        this.id=id;
        this.name=name;
        this.price=price;
        this.firstLink=firstLink;
        this.secondLink=secondLink;
    }
}
var lastSort;
var lots1=[];
var lots2=[];
var rows1 = document.getElementById("lotsTable").getElementsByClassName("lotRow");
var rows2 = document.getElementById("myLotsTable").getElementsByClassName("lotRow");
for(var i=0;i<rows1.length;i++){
    var row = rows1[i].cells;
    lots1[i]=new Lot(row[0].innerHTML,row[1].innerHTML,row[2].innerHTML,row[3].children[0], row[4].children.length!=0?row[4].children[0]:row[4].innerHTML)
}
for(var i=0;i<rows2.length;i++){
    var row = rows2[i].cells;
    lots2[i]=new Lot(row[0].innerHTML,row[1].innerHTML,row[2].innerHTML,row[3].children[0], row[4].children.length!=0?row[4].children[0]:row[4].innerHTML)
}

var rows = rows1;
var lots = lots1;

var sortID = document.getElementById("sortID");
var sortName = document.getElementById("sortName");
var sortPrice = document.getElementById("sortPrice");
var searchWords = document.getElementById("searchWords");

sortID.onclick = sortById;
sortName.onclick = sortByName;
sortPrice.onclick = sortByPrice;
searchWords.onchange = search;

function sortById(){
    lots.sort(function(a,b){return Number(a.id)-Number(b.id)});
    if(lastSort==1){lots.reverse(); lastSort=0}
    else lastSort=1;
    renderTable();
}
function sortByName(){
    lots.sort(function(a,b){
       if(a.name>b.name)return 1;
       if(a.name<b.name)return -1;
       return 0;
    });
    if(lastSort==2){lots.reverse(); lastSort=0}
    else lastSort=2;
    renderTable();}
function sortByPrice(){
    lots.sort(function(a,b){return Number(a.price)-Number(b.price)});
    if(lastSort==3){lots.reverse(); lastSort=0}
    else lastSort=3;
    renderTable();}
function renderTable(){
    var words = document.getElementById("searchWords").value;
    for(var i=0;i<rows.length;i++){
        //if(rows[i].parentNode.hidden) continue;
        var row = rows[i].cells;
        row[0].innerHTML=lots[i].id;
        row[1].innerHTML=lots[i].name;
        row[2].innerHTML=lots[i].price;
        if(row[3].children.length!=0)
        row[3].removeChild(row[3].children[0]);
        row[4].innerHTML="";
        row[3].appendChild(lots[i].firstLink);
        if(lots[i].secondLink==="Sold")
        row[4].innerHTML="Sold";
        else
        row[4].appendChild(lots[i].secondLink);
        rows[i].hidden = !improvedSearch(lots[i].name,words);
    }
}
function improvedSearch(name,words){
    for(var word of words.toLowerCase().split(" "))
        if(name.toLowerCase().search(word)!=-1)return true;
    return false;
}
function search(){
	renderTable();
}