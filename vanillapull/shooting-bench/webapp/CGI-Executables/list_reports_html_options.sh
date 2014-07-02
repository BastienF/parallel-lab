#!/bin/bash
 echo "Content-type: text/html"
 echo ""
 export HOME=/Users/Bastien
 cd "parallel-lab/scenarios/"
for report in `find ./ -maxdepth 2 -name "*.json"`
do
	echo '<option value="http://localhost/scenarios/'${report:3}'">'${report:3}'</option>'
done