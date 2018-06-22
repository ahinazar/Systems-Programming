package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

import java.util.LinkedList;

public class OpenANewCourse extends Action {

    private String department;
    private String course;
    private Integer space;
    private CoursePrivateState coursePrivateState = new CoursePrivateState();

    /**
     *
     * @param department department's name
     * @param course course's name
     * @param space space number
     * @param prerequisites list of prerequisites courses
     */
    public OpenANewCourse(String department, String course, Integer space, LinkedList<String> prerequisites) {
        setActionName("Open a new course");
        this.department = department;
        this.course = course;
        this.space = space;
        coursePrivateState.setAvailableSpots(space);
        coursePrivateState.setPrequisites(prerequisites);
    }

    /**
     * executing operation
     */
    public void start() {
        boolean courseExists = true;
        sendMessage(null, course, coursePrivateState);
        DepartmentPrivateState thisDepartmentPrivateState = (DepartmentPrivateState) this.getActorState();

        if (!thisDepartmentPrivateState.getCourseList().contains(course)) {
            thisDepartmentPrivateState.getCourseList().add(course);

            complete(true);
            courseExists = false;
        }

        if (courseExists) {
            complete(false);
        }
    }

    /**
     *
     * @return actor's private state
     */
    public PrivateState getActorState() {
        return this.actorState;
    }
}



