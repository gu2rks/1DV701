import socket
target_host = "localhost"
target_port = 8888
# create a socket object
client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# connect the client
client.connect((target_host,target_port))
# send some data

client.send("GET /look.png HTTP/1.1\r\nHost: localhost:8888/look.png\r\n\r\n")
# receive some data 
response = client.recv(4096)
print response

