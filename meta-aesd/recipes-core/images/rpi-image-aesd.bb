# Base this image on core-image-base
include recipes-core/images/core-image-base.bb

COMPATIBLE_MACHINE = "^rpi$"


#IMAGE_INSTALL:append = " packagegroup-rpi-test \
#    packagegroup-meta-oe-core \
#    packagegroup-meta-python3 \
#    packagegroup-meta-networking-connectivity"

IMAGE_INSTALL:append = " packagegroup-rpi-test \
    python3 \
    python3-pip \
    python3-pyserial \
    python3-numpy \
    python3-wxgtk4 \
    bash \
    expect \
    vim \
    libgdiplus \
    mono \
    aesd-connman-connect \
    aesd-tgbot-server \
    aesd-gnssposget-server \
"
    
    
#networkmanager"

#packagegroup-meta-networking-connectivity"



inherit core-image
#CORE_IMAGE_EXTRA_INSTALL += "aesd-gnssaccel"
#CORE_IMAGE_EXTRA_INSTALL += "aesd-tgbot-server"
CORE_IMAGE_EXTRA_INSTALL += "openssh"
