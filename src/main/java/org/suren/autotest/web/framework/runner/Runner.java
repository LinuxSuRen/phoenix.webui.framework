package org.suren.autotest.web.framework.runner;

/**
 * Indicate this is a runner which can run the test cases automatically.
 * For example, there'a class below. This framework will execute method automatically.
 * <pre>
 * import org.suren.autotest.web.framework.annotation.AutoRunner;
 * import org.suren.autotest.web.framework.runner.Runner;
 *
 * &#64;AutoRunner
 * public class Pipeline implements Runner {
 *   public void run() {}
 * }
 * </pre>
 * In order to make it happened, you only need to have a simple startup class like below:
 * <pre>
 * import org.suren.autotest.web.framework.annotation.AutoApplication;
 * import org.suren.autotest.web.framework.runner.RunnerStartup;
 *
 * &#64;AutoApplication
 * public class Entry {
 *     public static void main(String[] args) {
 *         new RunnerStartup().start(Entry.class);
 *     }
 * }
 * </pre>
 * @author linuxsuren
 */
public interface Runner {
}
