package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class OpeningNewPlacesInACourse extends Action {

    private int number;
    private String course;

    /**
     *
     * @param course course's name
     * @param number number of new places
     */
    public OpeningNewPlacesInACourse(String course, int number) {
        setActionName("Add spaces");
        this.course = course;
        this.number = number;
    }

    /**
     * executing operation
     */
    public void start() {
        CoursePrivateState thisCoursePrivateState = ((CoursePrivateState) this.getActorState());

        if (thisCoursePrivateState.getAvailableSpots() != -1) { //if -1 course closed!
            thisCoursePrivateState.setAvailableSpots(thisCoursePrivateState.getAvailableSpots() + number);
            complete(true);
        } else {
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
