SUMMARY = "Script to connect to WiFi on boot using connman"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://connman-wifi-connect-init \
		   file://connman-wifi-start.sh \
		   file://connman-wifi-interactive.exp \
			"
S = "${WORKDIR}"

# Startup
inherit update-rc.d

FILES:${PN} += "${base_bindir}/aesd/connman-wifi-start.sh"
FILES:${PN} += "${sysconfdir}/init.d/connman-wifi-connect-init"

# Refrence class which handles install scripts
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "connman-wifi-connect-init"

do_configure () {
	:
}

do_compile () {
	:
}

do_install:append () {
	install -d ${D}${base_bindir}
	install -d ${D}${base_bindir}/aesd
    install -d ${D}${sysconfdir}/init.d

	install -m 0755 ${WORKDIR}/connman-wifi-start.sh ${D}${base_bindir}/aesd/
	install -m 0755 ${WORKDIR}/connman-wifi-interactive.exp ${D}${base_bindir}/aesd/
	install -m 0755 ${WORKDIR}/connman-wifi-connect-init ${D}${sysconfdir}/init.d
}

RDEPENDS:aesd-connman-connect = "bash expect"

