# Model for the problem Edarp
	# X é definito corretamente?
	# diverso in s.t.
	# nomi s.t.
	# w1 e w2 cosa sono
	# funzione min max (15-16)
	# Come impostare G e M (11-13-14)
	# Come impostare corretamente o[k]
# Sets
param n;
set P := 1..n;							# Pickup locations
set D := n+1..2*n;						# Dropoff locations
set N := P union D;						# Pickup and Dropoff locations

param numVehicles;
set K := 1..numVehicles;				# Available vehicles 
set O within K;							# Origin depots for vehicles K

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
param alpha{S};		# Recharge rate at charging facility S
param Tp;			# Planning horizon

# Decision variables
var X{K,V,V} binary;	# 1 if vehicle K sequentially stops at locations V and V, 0 otherwise 	(constraint 27)
var T{K,V};				# Time at which vehicle k starts its service at location V
var L{K,V};				# Load of vehicle k at location V
var B{K,V} >=0;			# Battery load of vehicle k at location V								(constraint 28)
var E{K,S} >=0;			# Charging time of vehicle k at charging station S						(constraint 29)
var R{P};				# Excess ride-time of passenger	P

#Declaration of the objective function
minimize one: sum{k in K} (sum {i in V,j in V} X[k,i,j]) + sum {i in P} R[i];

#Declaration of constraints
#s.t. two		{k in K}:					sum {j in P union S union F} X[k, O[k],j]						= 	1;
#s.t. three		{j in K}:					sum {j in F}(	sum{j in D union S union O[k]}	X[k, i,j]) 		= 	1;
#s.t. four		{j in F union S}:			sum {k in K}(	sum{i in D union S union O[k]}	X[k, i,j]) 		<=	1;
s.t. five		{k in K, i in N union S}:	sum {j in V}(	X[k, i,j]) - sum {j in V}(X[k, j,i])			=	0; # j != i
s.t. six		{i in P}:					sum {k in K}(	sum{j in N}						X[k, i,j])		= 	1; # j != i
s.t. seven		{k in K, i in P}:			sum {j in N}(	X[k ,i,j]) - sum {j in N}(X[k, j,n+i])			=	0; # j != i , j != n+i
s.t. eight		{k in K, i in P}:			T[k,i] + d[i] + t[i,n+i]										<= 	T [k,n+i];
s.t. nine_a		{k in K, i in V}:			arr[i]															<= 	T [i,k];
s.t. nine_b 	{k in K, i in V}:			dep[i]															>= 	T [i,k];
s.t. ten		{k in K, i in P}:			T[k, n+i] - T[k,i] - d[i]										<= 	u[i];
#s.t. eleven 	{k in K, i in V, j in V}:	T[k,i] + t[i,j] + d[i] - M[i,j] * (1- x[k, i,j])				<= 	T[k,j]; # i != j	
																				#M[i,j] = max{0,dep[i]+d[i]+t[i,j]−arr[j]}
s.t. twelve 	{k in K, i in P}:			T[k, n+i] - T[k,i] - d[i] - t[i, n+i]							<= 	R[i];
#s.t. thirteen 	{k in K, i in V, j in V}:	L[i,k] + l[j] - G[k, i,j] * (1- x[k, i,j])						<= 	L[k,j]; # i != j
#s.t. fourteen 	{k in K, i in V, j in V}:	L[i,k] + l[j] + G[k, i,j] * (1- x[k, i,j])						<= 	L[k,j]; # i != j
																				#G[k,j] = min{C[k],C[k]+l[i]}
#s.t. fifteen	{k in K, i in N}:			max(0,l[i])														<=	L[k,i];
#s.t. sixteen	{k in K, i in N}:			min(0,l[i])														>=	L[k,i];

