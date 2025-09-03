#!/bin/bash
#set -x #echo on

####################################
#expected_ssid="OxyGen"
#wifi_pass="RAZVODNQEklu4i"
expected_ssid="coursera-test"
wifi_pass="12345678"
static_ip="192.168.0.100"
netmask="255.255.255.0"
gateway="192.168.0.1"
nameservers="8.8.8.8 4.4.4.4"
####################################

sleeptime=20
wifi_connected=0

wait_reconnection() {
    wifi_state="$(connmanctl state | grep "State" | awk '{print $3}')"
    if [ "$wifi_state" = "ready" ]; then
        echo "WiFi is connected to $wifi_ssid"
        exit 0
    else
        echo "Waiting $sleeptime seconds before new attempt"
        sleep $sleeptime
    fi
}

##############

get_ssid() {
    continue=0
    while [ $continue -eq 0 ]; do
        connmanctl scan wifi
        wifi_ssid=$(connmanctl services | grep -r "\b$expected_ssid\b" | awk '{print $2}')
        if [[ $wifi_ssid == *wifi_* ]]; then
            continue=1
        else
            echo "Waiting for WiFi SSID to appear..."
            sleep $sleeptime
        fi
    done
}

####################################
####################################
#               MAIN
####################################
dir="$(cd -P -- "$(dirname -- "$0")" && pwd -P)"
echo "Current directory: $dir"

connmanctl enable wifi

reoccuring_connection=$(cat /var/lib/connman/*/settings | grep Name | head -n 1)
if [ "$reoccuring_connection" = "Name=$expected_ssid" ]; then
    echo "Waiting for reconnection to $expected_ssid..."
    wait_reconnection
fi

get_ssid
echo "Found WiFi SSID: $wifi_ssid"
while [ $wifi_connected -eq 0 ]
do
    /usr/bin/expect /bin/aesd/connman-wifi-interactive.exp $wifi_ssid $wifi_pass
    sleep 10
    wifi_state="$(connmanctl state | grep "State" | awk '{print $3}')"
    if [ "$wifi_state" = "ready" ]; then
        wifi_connected=1
    else
        echo "Waiting $sleeptime seconds before new attempt"
        sleep $sleeptime
    fi
done

# Set static IP address 
# Doesn't work with iPhone connection :(
#connmanctl config $wifi_ssid --ipv4 manual $static_ip $netmask $gateway
#connmanctl config $wifi_ssid --nameservers $nameservers

echo "WiFi is connected to $wifi_ssid"
exit 0
