# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"


SRC_URI = "git://git@github.com/JustOxy666/aesd-finalprj-linuxapps.git;protocol=ssh;branch=main"
PV = "1.0+git${SRCPV}"
SRCREV = "3b3d686f43097748913c614dae4c821b34c4282f"

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
	install -m 0755 ${S}/gnss_module_start.sh ${D}${bindir}/
	install -m 0755 ${S}/aesd-gnssposget-server-start-stop.sh ${D}${sysconfdir}/init.d
}

RDEPENDS:aesd-gnssposget-server = "bash"
