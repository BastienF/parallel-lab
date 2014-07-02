#!/bin/bash
# You must add following two lines before
# outputting data to the web browser from shell
# script
 export HOME=/Users/Bastien
 echo "Content-type: text/html"
 echo ""
 echo '<html><head><title>Parallel-Lab</title>
     <style type="text/css">
  a {
    color: #EEEEEE;
  }
  input, button, select, option {
  	background-color: #222222;
  	color: #EEEEEE;
  	-moz-border-radius:5px;
-webkit-border-radius:5px;-o-border-radius:5px;
border-radius:5px;
  }
  .dotted {
  	border-style:dashed; border-color:#EEEEEE; border-width:1px; padding:5px; margin:5px
  }
  </style>
  </head><body style="margin:0; padding:15px; color:#EEEEEE; background-color:black; font-family:Monospace; font-size:13px;">'
# Save the old internal field separator.
  OIFS="$IFS"

# Set the field separator to & and parse the QUERY_STRING at the ampersand.
  IFS="${IFS}&"
  set $QUERY_STRING
  Args="$*"
  IFS="$OIFS"
  scenarioContent="users;iterations;duration\n"
  scenarioName="error"
  iter=1;
  for i in $Args ;do
    #Set the field separator to =
    IFS="${OIFS}="
    set $i
    IFS="${OIFS}"
    if [ "$1" = "scenarioName" ]; then
      scenarioName=$2
    else
      scenarioContent="${scenarioContent}${2}"
      if [ `echo "$iter % 3" | bc` -eq 0 ]; then
        scenarioContent="${scenarioContent}\n"
        iter=1
      else
        scenarioContent="${scenarioContent};"
        iter=$(($iter + 1))
      fi
    fi
    

  done 
  cd parallel-lab
	mkdir -p "scenarios/$scenarioName"
	echo -ne $scenarioContent > "scenarios/$scenarioName/scenario.csv"
  echo '<div class="dotted"><a href="http://localhost/cgi-bin/scenario_choose.sh"> << Change scenario</a></div>'
  echo '<div class="dotted"><p/>'
  echo "Scenario $scenarioName created ! (or not, I did not really check.)"
echo '</p></div>'
 echo "</body></html>"