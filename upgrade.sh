#!/bin/bash
#
# Purpose:  Upgrade example applications to the latest version of frameworks
#
# Author:  Steve Hu, stevehu@gmail.com
#
# Parameters:
#   $1:  old version
#   $2:  new version
#

old=$1
new=$2

function showHelp {
    echo " "
    echo "Error: $1"
    echo " "
    echo "    upgrade.sh [old-version] [new-version]"
    echo " "
    echo "    where [old-version] is the previous version number that needs to be replaced."
    echo "          [new-version] is the new version number for the next release"
    echo " "
    echo "    example: ./upgrade.sh 1.4.6 1.5.0"
    echo " "
}

if [ -z $1 ]; then
    showHelp "[old-version] parameter is missing"
    exit
fi

if [ -z $2 ]; then
    showHelp "[new-version] parameter is missing"
    exit
fi

rpx -c config.yaml --var 'old'=$old --var 'new'=$new
