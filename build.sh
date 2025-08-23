#!/bin/bash
# Script to build image for qemu.
# Author: Siddhant Jajoo.

git submodule init
git submodule sync
git submodule update

# local.conf won't exist until this step on first execution
source poky/oe-init-build-env

#################
# SET LOCAL.CONF
#################
add_to_local_conf() {
    cat conf/local.conf | grep "${confparam}" > /dev/null
    local_conf_info=$?

    if [ $local_conf_info -ne 0 ];then
        echo "Append ${confparam} in the local.conf file"
        echo ${confparam} >> conf/local.conf
        
    else
        echo "${confparam} already exists in the local.conf file"
    fi
}

add_multiline_to_local_conf() {
    cat conf/local.conf | grep "$1" > /dev/null
    local_conf_info=$?

    if [ $local_conf_info -ne 0 ];then
        echo "Append ${confparam} in the local.conf file"
        echo ${confparam} >> conf/local.conf
        
    else
        echo "${confparam} already exists in the local.conf file"
    fi
}

#################
# SET MACHINE
#################
CONFLINE="MACHINE = \"raspberrypi4\""
confparam=$CONFLINE
add_to_local_conf

#################
# SET RPi PARAMS
#################
CONFLINE="ENABLE_UART = \"1\""
confparam=$CONFLINE
add_to_local_conf

#################
# Configure UART3
# Pins (GPIO 4 and 5 to TXD3/RXD4 (ALT4))
# Overlay overlays/uart3.dtbo
#################
CONFLINE=" \
    RPI_EXTRA_CONFIG = \"\n# Configure UART3 pins\ngpio=4,5=a4 \
    \n\n# Enable UART3\ndtoverlay=uart3\" \
    "
confparam=$CONFLINE
add_multiline_to_local_conf RPI_EXTRA_CONFIG

#################
# Add UART3 devicetree overlay to /boot/overlays
#################
CONFLINE="RPI_KERNEL_DEVICETREE_OVERLAYS:append = \" overlays/uart3.dtbo\""
confparam=$CONFLINE
add_to_local_conf

#################
# ADD LAYERS
#################
####
bitbake-layers show-layers | grep "meta-raspberrypi" > /dev/null
layer_info=$?

if [ $layer_info -ne 0 ];then
	echo "Adding meta-raspberrypi layer"
	bitbake-layers add-layer ../meta-raspberrypi
else
	echo "meta-raspberrypi layer already exists"
fi
####
bitbake-layers show-layers | grep "meta-aesd" > /dev/null
layer_info=$?

if [ $layer_info -ne 0 ];then
	echo "Adding meta-aesd layer"
	bitbake-layers add-layer ../meta-aesd
else
	echo "meta-aesd layer already exists"
fi
####
add_metaembedded() {
    meta_openembedded_path="../meta-openembedded"
    echo "Adding meta-openembedded"
    bitbake-layers add-layer ""$meta_openembedded_path"/meta-oe"
    bitbake-layers add-layer ""$meta_openembedded_path"/meta-python"
}
add_metaembedded="0"
for layer in ../meta-openembedded/meta-*; do
    layer_name=$(basename "$layer")
    bitbake-layers show-layers | grep "$layer_name" > /dev/null
    layer_info=$?
    if [ $layer_info -ne 0 ]; then
        add_metaembedded="1"
    else
        echo "meta-openembedded layers already exists"
    fi
done

if [ $add_metaembedded -eq 1 ]; then
    add_metaembedded
fi
####
bitbake-layers show-layers | grep "meta-mono" > /dev/null
layer_info=$?

if [ $layer_info -ne 0 ];then
	echo "Adding meta-mono layer"
	bitbake-layers add-layer ../meta-mono  
else
	echo "meta-mono layer already exists"
fi



set -e
#bitbake core-image-base
bitbake rpi-image-aesd