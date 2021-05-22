package solutionCheck;

import model.Instance;
import model.Solution;
import model.Util;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class SolutionChecker {
    private final Solution solution;

    public SolutionChecker(Solution solution) {
        this.solution = solution;
    }

    public boolean checkAll() {
        int total=0;
        int pass=0;
        Reflections reflections = new Reflections("solutionCheck");
        Set<Class<? extends AbstractConstraint>> classes = reflections.getSubTypesOf(AbstractConstraint.class);
        for (Class<? extends AbstractConstraint> aClass : classes) {
            try {
                Constructor<?> constructor = aClass.getConstructor(Solution.class);

                AbstractConstraint constraint = (AbstractConstraint) constructor.newInstance(solution);
                if(!constraint.check()) {
                    System.err.println("Error constraint: "+aClass.getSimpleName());
                    //return false;
                } else {
                    Util.printGreen("Passed constraint: "+aClass.getSimpleName()+"\n");
                    pass++;
                }
                total++;
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        solution.setConstraint("Constraint: "+pass+"/"+total);
        return true;
    }

    /*public boolean checkAll() {
        AbstractConstraint constr=new ControlPath(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 2: ControlPath");
            return false;
        }

        constr=new ReturnToDestinationDepot(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 3: ReturnToDestinationDepot");
            return false;
        }

        constr=new VisitOnceOptionalDestination(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 4: VisitOnceOptionalDestination");
            return false;
        }

        constr=new FlowConservation(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 5: FlowConservation");
            return false;
        }

        constr=new MissionExecuteSameCar1(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 6: MissionExecuteSameCar1");
            return false;
        }

        constr=new MissionExecuteSameCar2(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 7: MissionExecuteSameCar2");
            return false;
        }

        constr=new VisitPickupBeforeDropoff(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 8: VisitPickupBeforeDropoff");
            return false;
        }

        constr=new BeginningOfService(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 9: BeginningOfService");
            return false;
        }

        constr=new MaxRideTime(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 10: MaxRideTime");
            return false;
        }

        constr=new ServiceStartTime(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 11: ServiceStartTime");
            return false;
        }

        constr=new ExcessRideTime(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 12: ExcessRideTime");
            return false;
        }

        constr=new ComputeLoad1(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 13: ComputeLoad1");
            return false;
        }

        constr=new ComputeLoad2(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 14: ComputeLoad2");
            return false;
        }

        constr=new OccupancyLowerBound(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 15: OccupancyLowerBound");
            return false;
        }

        constr=new OccupancyUpperBound(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 16: OccupancyUpperBound");
            return false;
        }

        constr=new EmptyVehiclesAtDepotsAndStation(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 17: EmptyVehiclesAtDepotsAndStation");
            return false;
        }

        constr=new VehiclesInitialBatteryLevels(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 18: VehiclesInitialBatteryLevels");
            return false;
        }

        constr=new BatteryLevelStateLocToLoc1(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 19: BatteryLevelStateLocToLoc1");
            return false;
        }

        constr=new BatteryLevelStateLocToLoc2(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 20: BatteryLevelStateLocToLoc2");
            return false;
        }

        constr=new BatteryLevelStateAfterCharging1(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 21: BatteryLevelStateAfterCharging1");
            return false;
        }

        constr=new BatteryLevelStateAfterCharging2(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 22: BatteryLevelStateAfterCharging2");
            return false;
        }

        constr=new BatteryLevelUpperBound(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 23: BatteryLevelUpperBound");
            return false;
        }

        constr=new MinimumBatteryLevelsReturningVehicles(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 24: MinimumBatteryLevelsReturningVehicles");
            return false;
        }

        constr=new RechargeTimeUpperBound(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 25: RechargeTimeUpperBound");
            return false;
        }

        constr=new RechargeTimeLowerBound(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 26: RechargeTimeLowerBound");
            return false;
        }

        constr=new Integrality(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 27: Integrality");
            return false;
        }

        constr=new BatteryLoadNonNegativity(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 28: BatteryLoadNonNegativity");
            return false;
        }

        constr=new ChargingTimeNonNegativity(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 29: ChargingTimeNonNegativity");
            return false;
        }

        constr=new ControlPath(solution);
        if(!constr.check()) {
            System.err.println("Error constraint 29: ControlPath");
            return false;
        }

        return true;
    }*/
}

