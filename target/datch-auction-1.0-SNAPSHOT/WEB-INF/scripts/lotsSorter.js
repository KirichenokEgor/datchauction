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
var lots=[];
var rows = document.getElementsByTagName("tr");
for(var i=2;i<rows.length;i++){
    var row = rows[i].cells;
    lots[i-2]=new Lot(row[0].innerHTML,row[1].innerHTML,row[2].innerHTML,row[3].children[0],row[4].children[0])
}
function sortById(){
    lots.sort(function(a,b){return Number(b.id)-Number(a.id)});
    if(lastSort==1){lots.reverse(); lastSort=0}
    else lastSort=1;
    renderTable();
}
function sortByName(){
    lots.sort(function(a,b){
       if(a.name>b.name)return -1;
       if(a.name<b.name)return 1;
       return 0;
    });
    if(lastSort==2){lots.reverse(); lastSort=0}
    else lastSort=2;
    renderTable();}
function sortByPrice(){
    lots.sort(function(a,b){return Number(b.price)-Number(a.price)});
    if(lastSort==3){lots.reverse(); lastSort=0}
    else lastSort=3;
    renderTable();}
function renderTable(){
    for(var i=2;i<rows.length;i++){
        var row = rows[i].cells;
        row[0].innerHTML=lots[i-2].id;
        row[1].innerHTML=lots[i-2].name;
        row[2].innerHTML=lots[i-2].price;
        if(row[3].children.length!=0)
        row[3].removeChild(row[3].children[0]);
        if(row[4].children.length!=0)
        row[4].removeChild(row[4].children[0]);
        row[3].appendChild(lots[i-2].firstLink);
        row[4].appendChild(lots[i-2].secondLink);
    }
}