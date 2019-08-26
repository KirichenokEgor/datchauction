class Item{
    constructor(id,name,description,status,owner){
        this.id=id;
        this.name=name;
        this.description = description;
        this.status = status;
        this.owner = owner;
    }
}
var lastSort;
var items=[];
var rows = document.getElementsByTagName("tr");
for(var i=2;i<rows.length-1;i++){
    var row = rows[i].cells;
    items[i-2]=new Item(row[0].innerHTML,row[1].innerHTML,row[2].innerHTML,row[3].innerHTML,row[4].innerHTML)
}
function sortById(){
    items.sort(function(a,b){return Number(b.id)-Number(a.id)});
    if(lastSort==1){items.reverse(); lastSort=0}
    else lastSort=1;
    renderTable();
}
function sortByName(){
    items.sort(function(a,b){
       if(a.name>b.name)return -1;
       if(a.name<b.name)return 1;
       return 0;
    });
    if(lastSort==2){items.reverse(); lastSort=0}
    else lastSort=2;
    renderTable();}
function sortByDescription(){
    items.sort(function(a,b){
       if(a.description>b.description)return -1;
       if(a.description<b.description)return 1;
       return 0;
    });
    if(lastSort==3){items.reverse(); lastSort=0}
    else lastSort=3;
    renderTable();
    }
function sortByStatus(){
    items.sort(function(a,b){
       if(a.status>b.status)return -1;
       if(a.status<b.status)return 1;
       return 0;
    });
    if(lastSort==4){items.reverse(); lastSort=0}
    else lastSort=4;
    renderTable();
    }
function renderTable(){
    for(var i=2;i<rows.length-1;i++){
        var row = rows[i].cells;
        row[0].innerHTML=items[i-2].id;
        row[1].innerHTML=items[i-2].name;
        row[2].innerHTML=items[i-2].description;
        row[3].innerHTML=items[i-2].status;
        row[4].innerHTML=items[i-2].owner;
    }
}