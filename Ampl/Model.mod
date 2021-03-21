# Model for the problem Edarp

# Sets
set P; 									# Pickup locations
set D; 									# Dropoff locations
set N := P union D;						# Pickup and Dropoff locations

param numVehicles;
set K := 1..numVehicles;				# Available vehicles 
set O{K};								# Origin depots for vehicles K

set F;									# All avaible destinations depots
set S;									# Charging stations 
set V := N union O union F union S;		# All possible locations

# Parameter
param t{V,V}; 		# Travel time from location V to location V
param arr{V}; 		# Earliest time at which service can begin at V
param dep{V}; 		# Latest time at which service can begin at V
param d{V};	 		# Service duration at location V
param l{N};			# Change in load at location N
param u{P};			# Maximum ride-time for customer with pickup at P
param c{K};			# Capacity of vehicle K
param Q;			# Effective battery capacity
param Binit{K};		# Initial battery capacity of vehicle K
param r;			# Final minimum battery level ratio
param beta{V,V};	# Battery consumption between nodes i, j in V
param alpha{s};		# Recharge rate at charging facility S
param Tp;			# Planning horizon

# Decision variables
var X{K,{V,V}} binary;	# 1 if vehicle K sequentially stops at locations V and V, 0 otherwise
var T{K,V};				# Time at which vehicle k starts its service at location V
var L{K,V};				# Load of vehicle k at location V
var B{K,V};				# Battery load of vehicle k at location V
var E{K,S};				# Charging time of vehicle k at charging station S
var R{P};				# Excess ride-time of passenger	P

#Declaration of the objective function
#minimize z:;

#Declaration of constraints