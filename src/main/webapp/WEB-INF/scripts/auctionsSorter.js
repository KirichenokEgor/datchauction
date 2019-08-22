class Auction{
    constructor(id,startTime,duration,firstLink,secondLink){
        this.id=id;
        this.startTime=startTime;
        this.duration=duration;
        this.firstLink=firstLink;
        this.secondLink=secondLink;
    }
}
var lastSort;
var auctions=[];
var rows = document.getElementsByTagName("tr");
for(var i=2;i<rows.length-1;i++){
    var row = rows[i].cells;
    auctions[i-2]=new Auction(row[0].innerHTML,row[1].innerHTML,row[2].innerHTML,row[3].children[0],row[4].children[0])
}
function sortById(){
    auctions.sort(function(a,b){return Number(b.id)-Number(a.id)});
    if(lastSort==1){auctions.reverse(); lastSort=0}
    else lastSort=1;
    renderTable();
}
function sortByTime(){
    auctions.sort(function(a,b){
        var timeA = Number(a.startTime.slice(0,2));
        var timeB = Number(b.startTime.slice(0,2));
        if(timeA!=timeB)return timeB-timeA;
        timeA = Number(a.startTime.slice(3,6));
        timeB = Number(b.startTime.slice(3,6));
        return timeB-timeA;
    });
    if(lastSort==2){auctions.reverse(); lastSort=0}
    else lastSort=2;
    renderTable();}
function sortByDuration(){
    auctions.sort(function(a,b){return Number(b.duration)-Number(a.duration)});
    if(lastSort==3){auctions.reverse(); lastSort=0}
    else lastSort=3;
    renderTable();}
function renderTable(){
    for(var i=2;i<rows.length-1;i++){
        var row = rows[i].cells;
        row[0].innerHTML=auctions[i-2].id;
        row[1].innerHTML=auctions[i-2].startTime;
        row[2].innerHTML=auctions[i-2].duration;
        if(row[3].children.length!=0)
        row[3].removeChild(row[3].children[0]);
        if(row[4].children.length!=0)
        row[4].removeChild(row[4].children[0]);
        row[3].appendChild(auctions[i-2].firstLink);
        row[4].appendChild(auctions[i-2].secondLink);
    }
}