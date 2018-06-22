package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Warehouse;

import java.util.LinkedList;

public class CheckAdministrativeObligations extends Action {

    private String department;
    private LinkedList<String> students;
    private Computer computer;
    private LinkedList<String> conditions;
    private Warehouse warehouse;

    /**
     * @param department value of department name
     * @param students   linked list of students
     * @param computer   computer value
     * @param conditions list of courses
     * @param warehouse  warehouse value
     */
    public CheckAdministrativeObligations(String department, LinkedList<String> students, Computer computer, LinkedList<String> conditions, Warehouse warehouse) {
        setActionName("Check Administrative Obligations");
        this.department = department;
        this.students = students;
        this.computer = computer;
        this.conditions = conditions;
        this.warehouse = warehouse;
    }

    /**
     * executing operation
     */
    public void start() {
        Promise tryToLockComputerPromise = warehouse.getSuspendingMutex(computer.getComputerType()).down();
        tryToLockComputerPromise.subscribe(() -> {
            LinkedList<Action> actionsBefore = new LinkedList<>();
            Action departmentCheckAndAssignExecuterAction = new DepartmentCheckAndAssignExecuterAction(students, computer, conditions);
            sendMessage(departmentCheckAndAssignExecuterAction, department, ((DepartmentCheckAndAssignExecuterAction) departmentCheckAndAssignExecuterAction).getActorState());
            actionsBefore.add(departmentCheckAndAssignExecuterAction);

            then(actionsBefore, () -> complete(true));

        });
    }

    /**
     * @return actor state
     */
    public PrivateState getActorState() {
        return this.actorState;
    }
}
