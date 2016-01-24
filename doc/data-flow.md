# Data flow design

Modem chat handles incoming data and produces the output. The processing operates on events, which may be external (data
fed to the modem) or internal (notifications).

## Event loop

The communication model loosely resembles a single-threaded event loop of NodeJs. In Java it is achieved with the help
of [single-threaded executor](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Executors.html#newSingleThreadExecutor()).

For every event (external and internal) a runnable task is placed on the queue. The queue is emptied sequentially. Input
data is read asynchronously. Every byte generates its own event. Data events mix with internal notifications.

Event loop runs indefinitely until input stream is closed.