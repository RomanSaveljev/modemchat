import com.github.RomanSaveljev.modemchat.ModemChat

//String[] cmd = ["/bin/sh", "-c", "stty raw </dev/tty"]
//Runtime.getRuntime().exec(cmd).waitFor()

def input = new ByteArrayInputStream("At".getBytes())
def chat = new ModemChat(input, System.out)
chat.v250.echo = true
chat.loop()