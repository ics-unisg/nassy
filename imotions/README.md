# Project Configuration in IMOTIONS

The project in IMOTIONS should have two types of stimuli (baseline and experiment). The names of these stimuli should use the following pattern:

"baseline"-digit

"experiment"-digit

Example of an experiment stimuli: baseline-1, experiment-1, baseline-2, experiment-2 ...
 

# Event Forwarding

Preferences > Global Settings > API

![image](https://user-images.githubusercontent.com/2293142/121386317-2fb3af80-c94a-11eb-8d97-a596373646eb.png)

Set the Hostname to the ip where you want to have all events sent to.

-----

For reduced latency, you can use TCP and connect the IMOTIONS machine with the machine where NASSY is hosted using an ethernet cable.

In this case, choose "Use TCP" in IMOTIONS and empty the Hostname field.

Remember that both machines should have the same IP prefix (e.g., 192.168.1.x).

Set the IP address of the IMOTIONS machine in et-reader/reader.py


# Event Recieving

Preferences > Global Settings > API

![image](https://user-images.githubusercontent.com/2293142/121386530-5eca2100-c94a-11eb-9de1-57f8bb17fb81.png)

Add the definition in eventsource-itrace.xml and enable reception over tcp port 8089. This port is hardcode in the itrace eclipse plugin.

# Itrace

Install itrace and configure the plugin. Use the adapted version: https://github.com/ics-unisg/iTrace-Eclipse/pull/1

A current build can be found in this folder. Copy `org.itrace_0.1.2.jar` to the `eclipse\dropin` folder inside the eclipse installation.
