package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/* -Behavior: This action adds a new student to a specified department.
   -Actor: Must be initially submitted to the Department's actor.*/
public class AddStudent extends Action {

    private String studentId;
    private String department;
    private StudentPrivateState studentPrivateState;


    /**
     * Constructor for Add Student
     *
     * @param department value of department name
     * @param studentId  value of student ID
     */
    public AddStudent(String department, String studentId) {
        setActionName("Add Student");
        this.department = department;
        this.studentId = studentId;
        this.studentPrivateState = new StudentPrivateState();
    }

    /**
     * executing operation
     */
    public void start() {
        ((DepartmentPrivateState) getActorState()).getStudentList().add(studentId);

        sendMessage(null, studentId, studentPrivateState);

        complete(true);
    }

    /**
     * @return actor state
     */
    public PrivateState getActorState() {
        return actorState;
    }
}