# State machine design

Modem chat runs a customizable state machine to handle incoming data and internally generated notifications.

Some parts of the state machine are defined by the standards and so the logic is hard-coded. The hard-coded logic mainly
serves the command line assembly, tokenization and normalization. However, `ModemChat` users are offered a flexible
framework for processing specific AT-commands.

`StateFactory` implementation is at the heart of the state machine. By request it instantiates specific states. There is
a default implementation that can be replaced.

## Command prefix

Processing begins with sensing first special character, which denotes command line beginning. 