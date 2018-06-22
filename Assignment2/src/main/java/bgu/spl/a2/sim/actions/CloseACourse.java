package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

import java.util.LinkedList;

public class CloseACourse extends Action {

    private String department;
    private String course;

    /**
     * Constructor
     *
     * @param department value of department name
     * @param course     value of course name
     */
    public CloseACourse(String department, String course) {
        setActionName("Close a course");
        this.department = department;
        this.course = course;
    }

    /**
     * executing operation
     */
    public void start() {
        LinkedList<Action> actionsBefore = new LinkedList<>();

        Action eraseCourseAction = new EraseCourseAction();
        sendMessage(eraseCourseAction, course, ((EraseCourseAction) eraseCourseAction).getActorState());
        actionsBefore.add(eraseCourseAction);

        then(actionsBefore, () -> {
                    ((DepartmentPrivateState) this.getActorState()).getCourseList().remove(course);
                    complete(true);
                }
        );

    }

    /**
     * @return actor private state
     */
    public PrivateState getActorState() {
        return this.actorState;
    }
}
