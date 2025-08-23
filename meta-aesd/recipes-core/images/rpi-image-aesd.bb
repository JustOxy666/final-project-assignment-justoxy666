# Base this image on core-image-base
include recipes-core/images/core-image-base.bb

COMPATIBLE_MACHINE = "^rpi$"

IMAGE_INSTALL:append = " packagegroup-rpi-test \
    python3 \
    python3-pip \
    python3-pyserial \
    python3-numpy \
    python3-wxgtk4 \
    screen \
    bash \
    expect \
    vim \
    libgdiplus \
    mono \
    aesd-connman-connect \
    aesd-tgbot-server \
    aesd-gnssposget-server \
"
    
inherit core-image
CORE_IMAGE_EXTRA_INSTALL += "openssh"

inherit logging
