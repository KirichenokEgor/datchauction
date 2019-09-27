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
var tableVersion = document.location.pathname.search("freeItems")!=-1;
var rows = document.getElementsByClassName("itemRow");
for(var i=0;i<rows.length;i++){
    var row = rows[i].cells;
    if(tableVersion)
         items[i]=new Item(row[0].innerHTML,row[1].innerHTML,row[2].innerHTML,row[3].innerHTML,row[4].innerHTML);
    else items[i]=new Item(row[0].innerHTML,row[1].innerHTML,row[2].innerHTML,null,null);
}
var sortId = document.getElementById("sortId");
var sortName = document.getElementById("sortName");
var sortDescription = document.getElementById("sortDescription");
var sortStatus = document.getElementById("sortStatus");
sortId.onclick = sortById;
sortName.onclick = sortByName;
sortDescription.onclick = sortByDescription;
sortStatus.onclick = sortByStatus;
function sortById(){
    items.sort(function(a,b){return Number(a.id)-Number(b.id)});
    if(lastSort==1){items.reverse(); lastSort=0}
    else lastSort=1;
    renderTable();
}
function sortByName(){
    items.sort(function(a,b){
       if(a.name.toLowerCase()>b.name.toLowerCase())return 1;
       if(a.name.toLowerCase()<b.name.toLowerCase())return -1;
       return 0;
    });
    if(lastSort==2){items.reverse(); lastSort=0}
    else lastSort=2;
    renderTable();}
function sortByDescription(){
    items.sort(function(a,b){
       if(a.description.toLowerCase()>b.description.toLowerCase())return 1;
       if(a.description.toLowerCase()<b.description.toLowerCase())return -1;
       return 0;
    });
    if(lastSort==3){items.reverse(); lastSort=0}
    else lastSort=3;
    renderTable();
    }
function sortByStatus(){
    items.sort(function(a,b){
       if(a.status>b.status)return 1;
       if(a.status<b.status)return -1;
       return 0;
    });
    if(lastSort==4){items.reverse(); lastSort=0}
    else lastSort=4;
    renderTable();
    }
function renderTable(){
    for(var i=0;i<rows.length;i++){
        var row = rows[i].cells;
        row[0].innerHTML=items[i].id;
        row[1].innerHTML=items[i].name;
        row[2].innerHTML=items[i].description;
        if(tableVersion){
        row[3].innerHTML=items[i].status;
        row[4].innerHTML=items[i].owner;
        }
    }
}