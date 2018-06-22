package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.Computer;

import java.util.LinkedList;

public class DepartmentCheckAndAssignExecuterAction extends Action {

    private LinkedList<String> students;
    private Computer computer;
    private LinkedList<String> conditions;

    /**
     *
     * @param students list of students
     * @param computer computer
     * @param conditions list of courses conditions
     */
    public DepartmentCheckAndAssignExecuterAction(LinkedList<String> students, Computer computer, LinkedList<String> conditions) {
        setActionName("Department Check And Assign Executer Action");
        this.students = students;
        this.computer = computer;
        this.conditions = conditions;
    }

    /**
     * executing operation
     */
    public void start() {
            LinkedList<Action> actionsBefore = new LinkedList<>();
            for (String studentId : students) {
                Action updateSignatureForStudentAction = new UpdateSignatureForStudentAction(computer, conditions);
                sendMessage(updateSignatureForStudentAction, studentId, ((UpdateSignatureForStudentAction) updateSignatureForStudentAction).getActorState());
                actionsBefore.add(updateSignatureForStudentAction);
            }
            then(actionsBefore, () -> {
                computer.getSuspendingMutex().up();
                    complete(true);
            });
    }

    /**
     *
     * @return actor's private state
     */
    public PrivateState getActorState() {
        return this.actorState;
    }
}
