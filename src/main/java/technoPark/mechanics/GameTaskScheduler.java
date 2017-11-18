package technoPark.mechanics;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameTaskScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameTaskScheduler.class);
    private final TreeMap<Long, ScheduledTask> scheduledTasks = new TreeMap<>();

    private final MechanicsTimeService timeService;

    public GameTaskScheduler(MechanicsTimeService timeService) {
        this.timeService = timeService;
    }

    public void schedule(long timerMillis, ScheduledTask task) {
        final long scheduleTime = timeService.time() + timerMillis;
        final ScheduledTask existedTask = scheduledTasks.get(scheduleTime);
        if (existedTask != null) {
            final ScheduledTaskList container = new ScheduledTaskList(existedTask);
            container.addTask(task);
            scheduledTasks.put(timerMillis, container);
            return;
        }
        scheduledTasks.put(scheduleTime, task);
    }

    public void tick() {
        final long now = timeService.time();

        Map.Entry<Long, ScheduledTask> entry;
        // headMap Returns a view of the portion of this map whose keys are strictly less than toKey.
        // pollFirstEntry Removes and returns a key-value mapping associated with the least key in this map, or null if the map is empty.
        while ((entry = scheduledTasks.headMap(now, true).pollFirstEntry()) != null) {
            performOperation(entry.getValue());
        }
    }

    public void reset() {
        scheduledTasks.clear();
    }

    private static void performOperation(@NotNull ScheduledTask task) {
        try {
            task.operate();
        } catch (RuntimeException ex) {
            LOGGER.error("Task "+ task.getClass().getSimpleName() +" execution failed", ex);
            task.onError();
        }
    }

    private interface ScheduledTask {
        void operate();
        default void onError() {}
    }

    public abstract static class GameSessionTask implements ScheduledTask {
        private final technoPark.mechanics.GameSession gameSession;

        public GameSessionTask(technoPark.mechanics.GameSession gameSession) {
            this.gameSession = gameSession;
        }

        public technoPark.mechanics.GameSession getGameSession() {
            return gameSession;
        }

        @Override
        public void onError() {
            gameSession.terminateSession();
        }
    }

    private static final class ScheduledTaskList implements ScheduledTask {
        final List<ScheduledTask> tasks;

        private ScheduledTaskList(@NotNull ScheduledTask... tasks) {
            this.tasks = new ArrayList<>(Arrays.asList(tasks));
        }

        private ScheduledTaskList(@NotNull ScheduledTaskList task) {
            this.tasks = task.tasks;
        }

        void addTask(@NotNull ScheduledTask task) {
            tasks.add(task);
        }

        @Override
        public void operate() {
            tasks.forEach(GameTaskScheduler::performOperation);
        }
    }
}
