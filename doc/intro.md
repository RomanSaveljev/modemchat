## Transport chain

* `UBMODEM` talks to `PROXY` task through FIFO
* `PROXY` is started in the foreground of `NSH`, so it can use `STDIN/STDOUT`
* `NSH` forwards the IO to the virtual serial port available on `USB`
* `PC` picks up the data from `USB`
* `SER2NET` monitors the `USB` and allows to talk to it over TCP port
* `MODEM-CHAT` communicates directly to the TCP socket or device node

## Modem chat design

Borrowing from the [phonesim](https://kernel.googlesource.com/pub/scm/network/ofono/phonesim/)
we need:

* States and transitions
* Variables
* Scripts
* Canned responses specific for a state
* Control interface to manipulate the state and variables

Borrowing from [hubot](https://hubot.github.com/) we need:

* Assortment a libraries of message processors
* Custom adapter, which respects the exchange protocol
* Asynchronous response support

And specifics are:

* Two modes: data and command
* [Mux protocol](http://www.3gpp.org/DynaReport/27010.htm)
* Every command can be queried for existence by adding a trailing `=?` and it
also gives the information of its subparameters
* Action commands and read commands
* `V0` and `V1` commands affect to how the response is formatted
* [V.250](https://www.itu.int/rec/T-REC-V.250-200307-I/en) commands change behavior
globally, even basic parsing
* `;` separates commands requested on the same line
* Characters from TE may be echoed or not by the MT
* Final `OK` or `ERROR` is generated for the whole `AT..<CR>` line
* Command prefix (attention) may be `AT`, `at`, `A/` or `a/`
* Whitespace is ignored in the body unless inside numeric (?) or string constants
* Termination character (configured by **S3**) may not appear in the body
* Command may contain backspaces (configured by **S5**)
* Echo happens immediately without any buffering and literally, e.g. when user sends
`<CR>` it is not automatically extended with `<LF>`, so if the command is not
understood they will just erase what they just written
* Whitespace is allowed almost anywhere in the command, so things like these are
perfectly fine to do:
  * `AT    + CGMI    `
  * `AT+CG MI = ?`
  * `AT + CGM I= ?`
  * `AT+CSCS ?`
* Whitespace is not allowed in the `AT` or `A/` prefix
* Repeat last command sequence `A/` does not need to be followed with `<CR>` to
be acted upon; here is an example:
```
00000000  41 54 2b 43 47 4d 49 0d  0d 0a 4c 65 6e 6f 76 6f  |AT+CGMI...Lenovo|
00000010  0d 0a 0d 0a 4f 4b 0d 0a  61 2f 0d 0a 4c 65 6e 6f  |....OK..a/..Leno|
00000020  76 6f 0d 0a 0d 0a 4f 4b  0d 0a 61 2f 0d 0a 4c 65  |vo....OK..
```
* All lowercase characters are treated same as uppercase
* Command number may have any number of leading zeroes, so these are the same:
```
ATE000001
ATE1
```
* For basic commands from V.250 there is no need for a separator, so this works:

```
ate1e0e1e?
E: 1

OK
```

* Certain commands (e.g. `A`) cause the rest of the command line to be ignored
* According to V.250 specification, when a basic command value is skipped, then
it is assumed `0` - this was not the case in my experiments with *F5521gw*
* `S` commands are immediately followed with the "parameter number" and new value
is assigned after `=`; same whitespace rules apply
* In certain modes, the command echoing is suppressed and all user input is ignored
(e.g. ongoing `ATD`)
* Only allowed numeric constants are integers
* Characters in string constants are escaped by putting their hexadecimal codes,
e.g. `\0D`; the backslash itself is coded as `\5c` and `"` as `\22`
* Some implementations may disable escaping mechanism selectively per-command and
the `\` character is treated as a backslash
* Compound value passed to a command is a set of numeric/string constants separated
with `,`
* Any parameter may be omitted like this: `a,,c`; parameters skipped at the end
do not need to be surrounded with commas: `a,b` - CoffeeScript implementation of
default function parameters will work very well here
* Omitted parameter handling is specific for every command
* When an extended command name after `+` is not recognized, the rest of the command
line is dropped, i.e. the `ERROR` response is relevant for that unrecognized command
* Rest of the command line is dropped also, whenever a command returns an `ERROR`
* Giving too many subparameters is an `ERROR`
* There is a single `OK` response for the whole command line
* Parameter values are applied all or nothing
* Extended commands (start with `+`) may be separated with `;`
* Ending `;` may be there, but is not necessary
* Basic commands may be separated with `;` as well
* Extended command may follow a basic command without a `;` separator
* Multiple `;` generate an error
* Command execution begins only after *termination character* is received
* Final result code may be textual or numeric (depends on `ATV?`)
* The modems are different in subtle aspects, e.g. whether printing URC is allowed
while a command line is entered
* There needs to be a NV pool of settings, which are applied upon `ATZ` handling

### Feeding data

Two options:

* Data is fed from the outside as it is received
  * Modem has to buffer incoming data
  * Users have to implement a (event) loop, which should be simple
  * Modem easily assumes asynchronous reading
  * Does not do anything before it is told
* Modem chat runs a loop and retrieves the data by itself
  * Users have to implement the interface
  * Modem has to implement asynchronous reading
  * Either user interface or the modem have to buffer data

A modem is allowed to ignore all input received, while a command line is being
executed. Not even echoed characters should appear. If input processing receives
a bigger buffer, where the data continues after `<CR>`, then the rest can be
discarded. The client is supposed to retrieve the previous command line results
before feeding more commands.

### Outputting data

Preferred is to have the same communication channel for output. Input processing
function returns the output to be printed. Asynchronous data presence is notified
to the client, upon which they should call the same input processing function
with or without more input. Modem chat may decide to ignore the input or to
process it. Unclaimed output data is buffered inside modem chat.

At the very least input processing shall return the command's echo, when it is
enabled. Because of buffering previous unclaimed command output (or URC) may
come before the echo.

A real modem would ignore any input received within 100 msec after command line
termination character. This can be implemented by notifying output data presence
after a timeout.

### Backspace

Backspace implementation is manifacturer-specific and is not supported, because
modem chat is intended for programmatic use. So, when a `ATS5?` character is
entered, it should be ignored and not echoed. But the `ATS5` itself must be
implemented.

### State machine design

An executing command takes over the input stream, no buffering happens. A command
may abort itself or ignore the input.

Reading a character from the input stream is the main asynchronous operation at
the heart of the state machine. If it is collecting a command line, then received
character must be echoed back immediately (depending on `ATE?`). Detection of
command line termination depends on `ATS3?`. So, the data context must be shared
between the states, but the behavior may be different.

The initial state drops (and echoes) all received characters until a command line
prefix `AT` or `A/` is detected. On repeat last command request it jumps straight
into command execution state using previously recorded command line.

### Application structure

Every simulation is an executable nodejs application. Re-usable parts are published
as modules to NPM and custom parts are developed on a per-case basis.

### States

There is only going forward, no going back in the state machine. When states switch,
it changes the message handling behavior. The stateful context is built of
methods and variables. When a state is entered, it should arbitrarily replace the
methods and set default values for variables.

When a message is received, basic parsing determines its kind and routed to a proper
handler. Proper handler is the one, which understands the message. There is no
priorotization and user should ensure no two handlers exist, which expect the
same message.

### Message handlers

Many different handlers would allow to implement the "intersection of features"
paradigm. For instance, one handler could be responsible for registration status,
another for ICC and yet another for SIM application. On the other hand a handler
may need access to the contextual information of another handler: to produce
proper registration status message it would need to know is the SIM card there.

Mixing few handlers together is simpler and helps to make the system understandable:

> When I assign a method to an object, I know it will replace whatever was there
> before, so routing issues become more visible

It is hard to see what immediate challenge multiple handlers support would solve.

### Message

A typical successful message with echo enabled should look like this:

```
00000000  41 54 2b 43 47 4d 49 0d  0d 0a 4c 65 6e 6f 76 6f  |AT+CGMI...Lenovo|
00000010  0d 0a 0d 0a 4f 4b 0d 0a                           |....OK..
```

The echo is suppressed (with `ATE0`):

```
00000000  0d 0a 4c 65 6e 6f 76 6f  0d 0a 0d 0a 4f 4b 0d 0a  |..Lenovo....OK..|
```

### Message handler internals

There is a single message fetching function, which is called on every received
distinguished request. This represents *command mode* handling. If the context
has a method to handle the command, then it is called. The method may modify
the context (including adding/removing other methods). *Existence* `=?` and *read*
`?` commands do not receive special treatment and need to be implemented as any
other kind.

V.250 commands affect to how the commands are extracted from the input stream and
how the responses need to be presented. We have to make sure it is hard for a user
to break these rules. Response sending should happen via a special object passed
to a command method. This will also allow for asynchronous processing.
