import time

from py4j.java_gateway import JavaGateway

gateway = JavaGateway()
stack = gateway.entry_point.getStack()

while True:
    print("Current date & time " + time.strftime("%c"))
    if stack.isEmpty() == False:
        command = stack.pop()
        print(command)
        exec(command)
    time.sleep(1)
