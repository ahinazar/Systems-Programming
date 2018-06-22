package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

import java.util.LinkedList;

public class EraseCourseAction extends Action {

    /**
     * Constructor
     */
    public EraseCourseAction() {
        setActionName("Erase Course Action");
    }

    /**
     * executing operation
     */
    public void start() {
        LinkedList<Action> actionsBefore = new LinkedList<>();

        if (!((CoursePrivateState) this.getActorState()).getRegStudents().isEmpty()) {
            for (String studentId : ((CoursePrivateState) this.getActorState()).getRegStudents()) {
                Action removeGradeFromStudentCourseAction = new RemoveGradeFromStudentCourseAction(actorId);
                actionsBefore.add(removeGradeFromStudentCourseAction);
                sendMessage(removeGradeFromStudentCourseAction, studentId, ((RemoveGradeFromStudentCourseAction) removeGradeFromStudentCourseAction).getActorState());
            }
        }
        then(actionsBefore, () -> {
            CoursePrivateState thisCourseToClosePrivateState = ((CoursePrivateState) this.getActorState());
            thisCourseToClosePrivateState.getRegStudents().removeAll(thisCourseToClosePrivateState.getRegStudents());
            thisCourseToClosePrivateState.getRegStudents().removeAll(thisCourseToClosePrivateState.getPrequisites());
            thisCourseToClosePrivateState.setRegistered(0);
            thisCourseToClosePrivateState.setAvailableSpots(-1);
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
