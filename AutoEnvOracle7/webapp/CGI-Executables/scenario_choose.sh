#!/bin/bash
# get today's date
OUTPUT="$(date)"
# You must add following two lines before
# outputting data to the web browser from shell
# script
 echo "Content-type: text/html"
 echo ""
  echo '<html><head><title>Parallel-Lab</title>
    <style type="text/css">
  a {
    color: #00FF00;
  }
  input, button, select, option {
  	background-color: #222222;
  	color: #00FF00;
  	-moz-border-radius:5px;
-webkit-border-radius:5px;-o-border-radius:5px;
border-radius:5px;
  }
  .dotted {
  	border-style:dashed; border-color:#00FF00; border-width:1px; padding:5px; margin:5px
  }
  </style>
  <script type="text/javascript">
  var nbEntry=0;
  var users_="users_"
  var iterations_="iterations_"
  var duration_="duration_"
  var numberOfShoot="numberOfShoot"
  function addFields(){
    // Number of inputs to create
    var number = document.getElementById("fields").value;
    // Container <div> where dynamic content will be placed
    var table = document.getElementById("createScenarioTable");
    var rowCount = table.rows.length;
   

    for (i=0;i<number;i++){
		nbEntry = nbEntry + 1;
		var row = table.insertRow(rowCount);
		rowCount = rowCount + 1;

		var cell1 = row.insertCell(0);
		var element1 = document.createElement("input");
		element1.type = "text";
		element1.name="users_" + nbEntry;
		element1.value="0";
		element1.id="users_" + nbEntry;
		cell1.appendChild(element1);

		var cell2 = row.insertCell(1);
		var element2 = document.createElement("input");
		element2.type = "text";
		element2.name="iterations_" + nbEntry;
		element2.value="0";
		element2.id="iterations_" + nbEntry;
		cell2.appendChild(element2);

		var cell3 = row.insertCell(2);
		var element3 = document.createElement("input");
		element3.type = "text";
		element3.name="duration_" + nbEntry;
		element3.value="0";
		element3.id="duration_" + nbEntry;
		cell3.appendChild(element3);
    }
}
function addScenario(){
	var e = document.getElementById("addScenarioChoice");
	var scenar = e.options[e.selectedIndex].value;
	// Container <div> where dynamic content will be placed
	var table = document.getElementById("toRunScenariosTable");
	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);
	var cell1 = row.insertCell(0);
	var element1 = document.createElement("input");
	element1.type = "text";
	element1.name="scenar_" + rowCount;
	element1.value=scenar;
	element1.id="scenar_" + rowCount;
	cell1.appendChild(element1);
}
function duplicateValue(idSuf) {
	var value = document.getElementById(idSuf + "1").value;
	for (i=1;i<=nbEntry;i++) {
		document.getElementById(idSuf + i).value = value;
	}
}
  </script>

  </head><body style="margin:0; padding:15px; color:#00FF00; background-color:black; font-family:Monospace; font-size:13px;">'
 echo "Today is $OUTPUT <br>"
 echo "Current directory is $(pwd) <br>"
 echo "Shell Script name is $0<br/>"
 export HOME=/Users/Bastien
 echo "HOME : $HOME<br/>"
 echo `whoami` " <br/>"
 cd "parallel-lab/scenarios/"
 echo '<div class="dotted"><a href="http://localhost/cgi-bin/list_reports.sh"> Go to reports list >></a></div>'
echo '<div  class="dotted"><h3> Run existing scenarios</h3>
<select name="scenario" id="addScenarioChoice">'
for scenario in ./*
do
  echo '<option value="'$scenario'">'$scenario'</option>'
done
echo '</select><button id="addScenario" onclick="addScenario()">Add</button>'
echo '<form name="chooseScenario" action="http://localhost/cgi-bin/run.sh" method="get">
<table border="0" id="toRunScenariosTable">'
echo '</table><input type="submit"/></form></div>'
echo '<div class="dotted"><h3>Create a new scenario</h3>
<form name="createScenario" action="http://localhost/cgi-bin/create_scenario.sh" method="get" >
<label for="scenarioName">Name </label><input id="scenarioName" name="scenarioName" type="text"/><br/><br/>'
echo '<table border="1" id="createScenarioTable"> 
 <tr> 
 <th> <a href="#" onclick="duplicateValue(users_)">Users</a> </th> 
 <th> <a href="#" onclick="duplicateValue(iterations_)">Iterations</a> </th> 
 <th> <a href="#" onclick="duplicateValue(duration_)">Duration</a> </th> 
  </tr> 
</table>'
echo '<input type="submit"/> </form>'
echo '<input type="text" id="fields" name="fields" value="1">
    <button href="#" id="addFields" onclick="addFields()">Add shoots</button></div>'
 echo "</body></html>"