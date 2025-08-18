# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"


SRC_URI = "git://git@github.com/JustOxy666/aesd-finalprj-linuxapps.git;protocol=ssh;branch=main"
PV = "1.0+git${SRCPV}"
SRCREV = "613d143f4849650176896c6f45becea35d1f1d61"

S = "${WORKDIR}/git/aesd-gnssposget-server"

FILES:${PN} += "${bindir}/aesd-gnssposget-server"

TARGET_LDFLAGS += "-pthread -lpthread -lrt"

# Startup script
inherit update-rc.d

# Refrence class which handles install scripts
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "aesd-gnssposget-server-start-stop.sh"

do_configure () {
	:
}

do_compile () {
	oe_runmake all
}

do_install () {
	install -d ${D}${bindir}
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${S}/aesd-gnssposget-server ${D}${bindir}/	
	install -m 0755 ${S}/aesd-gnssposget-server-start-stop.sh ${D}${sysconfdir}/init.d
}
