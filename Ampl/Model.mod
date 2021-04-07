# Model for the problem Edarp

# Sets
param n;
set P := 1..n;							# Pickup locations
set D := n+1..2*n;						# Dropoff locations
set N := P union D;						# Pickup and Dropoff locations

param numVehicles;
set K := 1..numVehicles;				# Available vehicles 
set Origin;								# Origin depots
set O;			# Origin depots for vehicles K

set F;											# All avaible destinations depots
set S;											# Charging stations 
set V := N union Origin union F union S;		# All possible locations

# Parameter
param t{V,V}; 		# Travel time from location V to location V
param arr{V}; 		# Earliest time at which service can begin at V
param dep{V}; 		# Latest time at which service can begin at V
param d{V};	 		# Service duration at location V
param l{V};			# Change in load at location N 				TODO: l'abbiamo cambiato (V)
param u{P};			# Maximum ride-time for customer with pickup at P
param c{K};			# Capacity of vehicle K
param Q;			# Effective battery capacity
param Binit{K};		# Initial battery capacity of vehicle K
param r;			# Final minimum battery level ratio
param beta{V,V};	# Battery consumption between nodes i, j in V
param alpha{S};		# Recharge rate at charging facility S
param Tp;			# Planning horizon
param Ok{K};		# Origin depots for vehicles K (array)

#Pesi per la funzione di minimizzazione
param w1;
param w2;

param M{V,V};		#M[i,j] = max{0,dep[i]+d[i]+t[i,j]*arr[j]} --> in file .dat
param G{K,V};		#G[k,j] = min{C[k],C[k]+l[i]}

# Decision variables
var X{K,V,V} binary;	# 1 if vehicle K sequentially stops at locations V and V, 0 otherwise 	(constraint 27)
var T{K,V};				# Time at which vehicle k starts its service at location V
var L{K,V};				# Load of vehicle k at location V
var B{K,V} >=0;			# Battery load of vehicle k at location V								(constraint 28)
var E{K,S} >=0;			# Charging time of vehicle k at charging station S						(constraint 29)
var R{P};				# Excess ride-time of passenger	P

#Declaration of the objective function
minimize one:  w1*(sum {k in K,i in V,j in V} t[i,j] * X[k,i,j]) + w2 * sum {i in P} R[i];

#Declaration of constraints
s.t. two		{k in K}:					sum {j in P union S union F} X[k, Ok[k],j]				= 	1;
s.t. three		{k in K}:					sum {j in F}(	sum{i in D union S union O}	X[k, i,j]) 		= 	1;
s.t. four		{j in F union S}:			sum {k in K}(	sum{i in D union S union O}	X[k, i,j]) 		<=	1;
s.t. five		{k in K, i in N union S}:	sum {j in V : j!=i}				(	X[k, i,j]) - sum {j in V : j!=i}(X[k, j,i])		=	0; 
s.t. six		{i in P}:					sum {k in K}(	sum{j in N : j!=i}				X[k, i,j])		= 	1;
s.t. seven		{k in K, i in P}:			sum {j in N : j!=i}	(	X[k ,i,j]) - sum {j in N : j!= n+i}(X[k, j,n+i])		=	0;
s.t. eight		{k in K, i in P}:			T[k,i] + d[i] + t[i,n+i]										<= 	T [k,n+i];
s.t. nine_a		{k in K, i in V}:			arr[i]															<= 	T [k,i];
s.t. nine_b 	{k in K, i in V}:			dep[i]															>= 	T [k,i];
s.t. ten		{k in K, i in P}:			T[k, n+i] - T[k,i] - d[i]										<= 	u[i];
s.t. eleven 	{k in K, i in V, j in V : i!=j}:	T[k,i] + t[i,j] + d[i] - M[i,j] * (1- X[k, i,j])		<= 	T[k,j];  	# TODO: manca questo | Mi,j > 0
s.t. twelve 	{k in K, i in P}:			T[k, n+i] - T[k,i] - d[i] - t[i, n+i]							<= 	R[i];
s.t. thirteen 	{k in K, i in V, j in V : i!=j}:	L[k,i] + l[j] - G[k, i] * (1- X[k, i,j])				<= 	L[k,j]; # TODO: è diverso dal model
s.t. fourteen 	{k in K, i in V, j in V : i!=j}:	L[k,i] + l[j] + G[k, i] * (1- X[k, i,j])				>= 	L[k,j]; # TODO: è diverso dal model
s.t. fifteen	{k in K, i in N}:			max(0,l[i])														<=	L[k,i];
s.t. sixteen	{k in K, i in N}:			min(c[k],c[k]+l[i])														>=	L[k,i];
s.t. seventeen 	{k in K, i in O union F union S}: 	L[k, i] 											= 0;
s.t. eighteen 	{k in K, i in O}:					B[k, i] 											= Binit[k];
s.t. nineteen 	{k in K, i in V diff S, j in V diff O : i != j}: 	B[k, i] - beta[i, j] + Q*(1 - X[k, i, j]) >= B[k, j];  
s.t. twenty 	{k in K, i in V diff S, j in V diff O : i != j}: 	B[k, i] - beta[i, j] - Q*(1 - X[k, i, j]) <= B[k, j]; 				
s.t. twentyone 	{k in K, s in S, j in P union F union S : s != j}:  	B[k, s] + alpha[s] * E[k, s] - beta[s, j] + Q * (1 - X[k, s, j]) >= B[k, j];	
s.t. twentytwo 	{k in K, s in S, j in P union F union S : s != j}: 		B[k, s] + alpha[s] * E[k, s] - beta[s, j] - Q * (1 - X[k, s, j]) <= B[k, j];	
s.t. twentythree{k in K, s in S}: 			B[k, s]+ alpha[s]*E[k, s] 										<= Q; 
s.t. twentyfour {k in K, i in F}: 			B[k, i] 														>= r*Q; 
s.t. twentyfive {k in K, s in S, i in D union S union O : i != s}: 	T[k, s] - t[i, s] - T[k, i] + M[i, s]*(1 - X[k, i, s]) >= E[k, s]; # TODO: è diverso dal model
s.t. twentysix 	{k in K, s in S, i in D union S union O : i != s}: 	T[k, s] - t[i, s] - T[k, i] - M[i, s]*(1 - X[k, i, s]) <= E[k, s]; # TODO: è diverso dal model
