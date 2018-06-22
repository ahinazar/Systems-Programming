package bgu.spl.a2;

import org.junit.Assert;
import org.junit.Test;

import java.rmi.server.ExportException;

import static org.junit.Assert.*;

public class VersionMonitorTest {

    @Test
    public void getVersion() {
        VersionMonitor monitor = new VersionMonitor();
        try {
            int version = monitor.getVersion();
            assertEquals(version, 0);
        } catch (Exception ex) { //something wrong with getVersion() - object already initialized
            Assert.fail("getVersion() - throws Exception");
        }
    }

    @Test
    public void inc() {
        VersionMonitor monitor = new VersionMonitor();
        try {
            monitor.inc();
            try {
                int version = monitor.getVersion();
                assertEquals(version, 1);
            } catch (Exception ex) { //something wrong with getVersion() - object already initialized
                Assert.fail("getVersion() - throws exception");
            }
        } catch (Exception ex) { //something wrong with inc() - object already initialized
            Assert.fail("inc()- throws exception ");
        }
    }

    @Test
    public void await() {

        VersionMonitor monitor = new VersionMonitor();

        boolean[] tmp={false};

        Thread thread = new Thread(() -> {
            try {
                monitor.await(0);

                tmp[0]=true; //for being sure the thread the waked
                assertTrue(tmp[0]);//be sure the thread the waked

            } catch (InterruptedException ex) { // didnt inc() yet -should not throw an exception
                Assert.fail("await() - throws InterruptedException without inc() before");
            }catch (Exception ex){ // something wrong with await()
                Assert.fail("await() - throws Exception");
            }
        });

            thread.start();
            assertFalse(tmp[0]);//be sure the thread waiting (monitor.await() - above)

            try {
                monitor.inc(); //threads supposed to wake up after awake command above
                thread.join(); //to be sure mainThread not finish before thread

            } catch (Exception ex1) { //inc() or join() - something wrong
                Assert.fail("inc() / join() - throwing Exception");
            }
    }
}