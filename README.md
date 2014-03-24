parallel-lab
============

Laboratory to demonstrate different way to do parallel programming.

### Launch ##

The project must be launched with the parameter `implementation` (see launch.sh). 
Available values are:
- naive
- akka
- mono
- executor
- pool


### VM Configuration ###

Performance tests have to be done on two distinct machines. The script `launch.sh` in scripts/ is for the gatling machine. This machine control the Tomcat machine over ssh.

Executes on the gatling machine to connect directly without password : 
`ssh-keygen -t rsa`
`ssh-copy-id user@gatling_machine`

### Running industrial testing ###

Test scenarios can be run automagically.
On the first environnement initialization the script init.sh has to be run. This script will be set up and provisionning the test VM.

After that for each testing scenarios, the desired scenario has to be write in lauch.sh and the script launch.sh has to be run.
This script will run up the Gatling and the Server VM. Upload the latest version of the project and run the testing scenario for each implementation.
The results will be downloaded on the local folder "results" with the full json and csv logs in the sub-directories "json" and "csv"

An exemple of testing scenario could be :<br/>
//launch_all number_of_clients number_of_iterations duration_time<br/>
launch_all 100 62500 30;<br/>
launch_all 100 93750 30;
