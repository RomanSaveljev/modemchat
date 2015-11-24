import com.github.RomanSaveljev.modemchat.ModemChat

//String[] cmd = ["/bin/sh", "-c", "stty raw </dev/tty"]
//Runtime.getRuntime().exec(cmd).waitFor()
println System.getProperty("org.slf4j.simpleLogger.logFile")
println System.getProperty("org.slf4j.simpleLogger.defaultLogLevel")

def input = new ByteArrayInputStream("ATA\r".getBytes())
def chat = new ModemChat(input, System.out)
chat.v250.echo = true
chat.v250.s3 = '\r'
chat.loop()