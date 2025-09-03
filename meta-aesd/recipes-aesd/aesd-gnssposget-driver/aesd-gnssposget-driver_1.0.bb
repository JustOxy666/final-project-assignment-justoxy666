#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://git@github.com/JustOxy666/aesd-finalprj-linuxapps.git;protocol=ssh;branch=main"
PV = "1.0+git${SRCPV}"
SRCREV = "2c7c13448f502e07697d19466aac9932bea32aee"

S = "${WORKDIR}/git/aesd-gnssposget-driver"

# Startup
inherit module
inherit update-rc.d

FILES:${PN} += "${base_bindir}/aesd-gnssposget-driver_load"
FILES:${PN} += "${base_bindir}/aesd-gnssposget-driver_unload"
FILES:${PN} += "${sysconfdir}/init.d/init_aesd-gnssposget-driver"

# Refrence class which handles install scripts
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "init_aesd-gnssposget-driver"

# MODULES_INSTALL_TARGET = "modules"
EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}"
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

do_configure () {
	:
}

do_compile () {
	oe_runmake
}

do_install:append () {
	
	install -d ${D}${base_bindir}
    install -d ${D}${sysconfdir}/init.d

	install -m 0755 ${S}/aesd-gnssposget-driver_load ${D}${base_bindir}/
	install -m 0755 ${S}/aesd-gnssposget-driver_unload ${D}${base_bindir}/
	install -m 0755 ${S}/init_aesd-gnssposget-driver ${D}${sysconfdir}/init.d
}