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

SRC_URI = "git://git@github.com/JustOxy666/aesd-finalprj-tgbotserver.git;protocol=ssh;branch=main"
PV = "1.0+git${SRCPV}"
SRCREV = "64f9198c88cdd6e22863fe6b9dca47e9d1d201e3"


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

#S = "${WORKDIR}/aesd-tgbot-server"
#FILES:${PN} += "aesd-tgbot-server"
#FILES:${PN} += "/opt/dotnet/${PN}"

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
    #bbwarn "cp -r --no-preserve=ownership ${B}/${PN} ${D}/opt"
    if [ "${SRC_ARCH}" = "x64" ]; then
        ln -s ${base_libdir} ${D}/lib64
    fi
}

FILES:${PN}:append = " /opt/${PN}/ /lib64"
