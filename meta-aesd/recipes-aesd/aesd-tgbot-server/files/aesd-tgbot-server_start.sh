#!/bin/sh

print_usage() {
    echo "Usage: $0 <aesd-tgbot-server path> <Bot Token> <API ID> <API Hash> <Socket IP or 'localhost'> <Socket Port>"
}

if [ "$1" = "" ] || [ "$2" = "" ] || [ "$3" = "" ] || [ "$4" = "" ] || [ "$5" = "" ] || [ "$6" = "" ]; then
    print_usage
    exit 1
fi

tgbot_server_path="$1"
bot_token="$2"
api_id="$3"
api_hash="$4"
socket_ip="$5"
socket_port="$6"

internet_connected=0

while [ $internet_connected -eq 0 ]; do
    ping_result="$(ping -s 56 -W 5 -c 1 google.com | grep "1 packets received")"
    if [ "$ping_result" = "1 packets transmitted, 1 packets received, 0% packet loss" ]; then
        echo "Internet connection established."
        internet_connected=1
    else
        echo "No internet connection, retrying in 5 seconds..."
        sleep 5
    fi
done

dotnet "$tgbot_server_path" "$bot_token" "$api_id" "$api_hash" "$socket_ip" "$socket_port" > /tmp/aesd-tgbot-server.log 2>&1
