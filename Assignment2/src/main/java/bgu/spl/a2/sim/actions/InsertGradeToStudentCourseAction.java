package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class InsertGradeToStudentCourseAction extends Action {

    private String course;
    private Integer grade;

    /**
     *
     * @param course course's name
     * @param grade grade for course's name
     */
    public InsertGradeToStudentCourseAction(String course, String grade) {
        setActionName("Insert Grade To Student Course Action");
        this.course = course;
        if (!grade.equals("-")) {
            this.grade = Integer.valueOf(grade);
        }
    }

    /**
     * executing operation
     */
    public void start() {
        ((StudentPrivateState) this.getActorState()).getGrades().put(course, grade);
        complete(true);
    }

    /**
     *
     * @return actor's private state
     */
    public PrivateState getActorState() {
        return this.actorState;
    }
}
