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
  </head><body style="margin:0; padding:15px; color:#00FF00; background-color:black; font-family:Monospace; font-size:13px;">'
  echo '<div class="dotted"><a href="http://localhost/cgi-bin/scenario_choose.sh"> << Change scenario</a> --- <a href="http://localhost/cgi-bin/list_reports.sh"> Go to reports list >></a></div>'
# Save the old internal field separator.
  OIFS="$IFS"

# Set the field separator to & and parse the QUERY_STRING at the ampersand.
  IFS="${IFS}&"
  set $QUERY_STRING
  Args="$*"
  IFS="$OIFS"
  cd parallel-lab
  for i in $Args ;do
#       Set the field separator to =
        IFS="${OIFS}="
        set $i
        IFS="${OIFS}"
				# Don't allow "/" changed to " ". Prevent hacker problems.
				scenario="`echo $2 | sed 's|[\]||g' | sed 's|%20| |g'`"
				echo '<h3>Scenario: '${scenario:4}'</h3>'
				echo '<div class="dotted"><p style="font-size:11px;"/>'
				./launch.sh ${scenario:4} 2>&1 | sed 's/$/<br\/>/g' | sed -E 's/((unreachable=[1-9])|(failed=[1-9])|(\[ERROR\].+))/<strong><font color="red">\1<\/font><\/strong>/g'
				echo '</p></div>'
  done 
 echo "</body></html>"