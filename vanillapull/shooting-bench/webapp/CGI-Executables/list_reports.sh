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
  <script type="text/javascript">
  
  </script>

 </head><body style="margin:0; padding:15px; color:#EEEEEE; background-color:black; font-family:Monospace; font-size:13px;">'
 echo "Today is $OUTPUT <br>"
 echo "Current directory is $(pwd) <br>"
 echo "Shell Script name is $0<br/>"
 export HOME=/Users/Bastien
 echo "HOME : $HOME<br/>"
 echo `whoami` " <br/>"
 cd "parallel-lab/scenarios/"
 echo '<div class="dotted"><a href="http://localhost/cgi-bin/scenario_choose.sh"> << Change scenario</a></div>'
echo '<div  class="dotted"><ul>'
for report in `find ./ -maxdepth 2 -name "*.json"`
do
	echo '<li><a href="http://localhost/parallel-lab/index.html?reportURL=http://localhost/scenarios/'${report:3}'">'${report:3}'</a></li>'
done
echo '</ul></div>'
 echo "</body></html>"