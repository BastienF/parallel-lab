#!/bin/bash
# You must add following two lines before
# outputting data to the web browser from shell
# script
 echo $$ > runningPID

 export HOME=/Users/Bastien
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
        ./launch.sh ${scenario:4}
  done

  echo "Parallel-Lab has executed all the scheduled tasks at " `date` | mail -s "Parallel-Lab has executed all the scheduled tasks" bfi@octo.com