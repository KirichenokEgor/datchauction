var auction;
var auctionId;
var lotTable;
var socketConn = new WebSocket('ws://' + window.location.host + '/' + window.location.pathname.split('/')[1] + '/activeAuction');
socketConn.onmessage = function(event) {
    var command = event.data.slice(0,2);
    if(command==="RA"){     //RA - Render Auction
	    auction = JSON.parse(event.data.slice(3));
        createLotTable();
    }
    if(command==="SE"){     //SE - Say Error
        window.alert(event.data.slice(3));
    }
    if(command==="AE"){     //AE - Auction Ended
        window.alert("Auction ended")
        history.back();
    }
    if(command==="LB"){     //LB - Lot Bought
        window.alert("Lot " + event.data.slice(3) + " is yours now");
        //TODO say and redirect
    }
    if(command==="WC")      //WC - Wrong Command (not Water Closet :-) )
        console.log("Wrong command!");
}
socketConn.onopen = function(event) {
    socketConn.send("joinAuction "+auctionId);
}

function loadBody(id){
 auctionId = id;
}

function createLotTable(){
     lotTable = document.getElementById("lotList");
     var rows = document.getElementsByClassName("lotRow");
     var length = rows.length;
     for(var i=0;i<length;i++)
     rows[0].remove();
     var lots = auction.lots;
     var index = 0;
     for(var lot of lots){
    	var tr = document.createElement('tr');
    	tr.setAttribute("class","lotRow");
    	var td1 = document.createElement('td');
    	td1.setAttribute("class","w3-center");
    	var td2 = document.createElement('td');
    	td2.setAttribute("class","w3-center");
    	var td3 = document.createElement('td');
    	td3.setAttribute("class","w3-center");
    	var td4 = document.createElement('td');
    	td4.setAttribute("class","w3-center");
    	var td5 = document.createElement('td');
    	td5.setAttribute("class","w3-center");
    	var itemsButton = document.createElement('button');
    	itemsButton.id = "l "+index;
    	itemsButton.setAttribute("class","w3-btn w3-block w3-theme-l3");
    	itemsButton.setAttribute("onclick","createItemTable(this)");
    	var text1 = document.createTextNode(lot.ID);
    	var text2 = document.createTextNode(lot.name);
    	var text3 = document.createTextNode(lot.currentPrice);
    	var text4 = document.createTextNode("items");
    	if(lot.status==="Registered"){
    	    var buyButton = document.createElement('button');
    	    buyButton.id = "b "+index;
    	    buyButton.setAttribute("onclick","buyLot(this)");
    	    buyButton.setAttribute("class","w3-btn w3-block w3-theme-l3");
    	    var text5 = document.createTextNode("buy");
    	    buyButton.appendChild(text5);
    	    td5.appendChild(buyButton);
    	}
    	else {
    	 var text5 = document.createTextNode("sold");
    	 td5.appendChild(text5);
    	}
    	index++;
    	td1.appendChild(text1);
    	td2.appendChild(text2);
    	td3.appendChild(text3);
    	itemsButton.appendChild(text4);
    	td4.appendChild(itemsButton);
    	tr.appendChild(td1);
    	tr.appendChild(td2);
    	tr.appendChild(td3);
    	tr.appendChild(td4);
    	tr.appendChild(td5);
    	lotTable.appendChild(tr);
    }
}
function createItemTable(button){
     var lots = auction.lots;
	 itemList = document.getElementById("itemList");
	 var rows = document.getElementsByClassName("itemRow");
     var length = rows.length;
         for(var i=0;i<length;i++)
         rows[0].remove();
	 var index = Number(button.id.slice(2));
	 var item;
	 for(item of lots[index].items){
	 var tr = document.createElement('tr');
	 tr.setAttribute("class","itemRow");
     var td1 = document.createElement('td');
     var td2 = document.createElement('td');
     var td3 = document.createElement('td');
     var td4 = document.createElement('td');
     td1.setAttribute("class","w3-center")
     td2.setAttribute("class","w3-center")
     td3.setAttribute("class","w3-center")
     td4.setAttribute("class","w3-center")
	 var text1 = document.createTextNode(item.ID);
     var text2 = document.createTextNode(item.name);
     var text3 = document.createTextNode(item.description);
     var text4 = document.createTextNode(item.owner);
     td1.appendChild(text1);
     td2.appendChild(text2);
     td3.appendChild(text3);
     td4.appendChild(text4);
     tr.appendChild(td1);
     tr.appendChild(td2);
     tr.appendChild(td3);
     tr.appendChild(td4);
     itemList.appendChild(tr);
	}
}
function buyLot(button){
    var lots = auction.lots;
	var index = Number(button.id.slice(2));
    if(window.confirm("You want to buy lot "+lots[index].ID+"?")){
        socketConn.send("buyLot "+lots[index].ID);
    }
}