# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
DESCRIPTION = "AESD Telegram Bot Server on .NET"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS:append = " dotnet-native"

RDEPENDS:${PN}:append = " \
    dotnet \
    icu \
    libgssapi-krb5 \
    zlib \
"

SRC_URI = "git://git@github.com/JustOxy666/aesd-finalprj-tgbotserver.git;protocol=ssh;branch=main \
	file://aesd-tgbot-server_start.sh \
	file://init_aesd-tgbot-server \
    "
PV = "1.0+git${SRCPV}"
SRCREV = "262024986dbdcbb7fd20d6abdd6b45bbb74649b3"


COMPATIBLE_HOST ?= "(x86_64|aarch64|arm).*-linux"

SRC_ARCH:aarch64 = "arm64"
SRC_ARCH:arm = "arm"
SRC_ARCH:x86-64 = "x64"

INSANE_SKIP:${PN} += "\
    already-stripped \
    staticdev \
"

S = "${WORKDIR}/git/aesd-tgbot-server"
do_compile[network] = "1"

# Startup
inherit update-rc.d

FILES:${PN} += "${base_bindir}/aesd/aesd-tgbot-server_start.sh"
FILES:${PN} += "${sysconfdir}/init.d/init_aesd-tgbot-server"

# Refrence class which handles install scripts
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "init_aesd-tgbot-server"

do_compile () {
    dotnet publish "${S}/${PN}.csproj" \
        -c Release -r linux-${SRC_ARCH} \
        --self-contained true \
        /p:PublishTrimmed=false \
        /p:IncludeAllContentForSelfExtract=true \    
        -o "${B}/${PN}"
    
    #FIXME: remove the following line. if the lttng-ust conflict is solved
    #FIXME: dotnet 8 doesn't produce libcoreclrtraceptprovider.so for the helloworld applications
    # When dotnet 6 and 7 reach end of life remove the following line.
    rm -f "${B}"/"${PN}"/libcoreclrtraceptprovider.so
}

do_install () {
    install -d ${D}/opt/
    cp -r --no-preserve=ownership ${B}/${PN} ${D}/opt
    if [ "${SRC_ARCH}" = "x64" ]; then
        ln -s ${base_libdir} ${D}/lib64
    fi

	install -d ${D}${base_bindir}
	install -d ${D}${base_bindir}/aesd
    install -d ${D}${sysconfdir}/init.d

	install -m 0755 ${WORKDIR}/aesd-tgbot-server_start.sh ${D}${base_bindir}/aesd/
	install -m 0755 ${WORKDIR}/init_aesd-tgbot-server ${D}${sysconfdir}/init.d
}

FILES:${PN}:append = " /opt/${PN}/ /lib64"
