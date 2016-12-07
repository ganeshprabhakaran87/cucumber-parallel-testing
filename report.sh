#!/bin/sh

set -ev

publishReport() {
    for entry in $(pwd)/target/cucumber-html-reports/$1/*
    do
      if [ -f "$entry" ];then
        echo $(basename "$entry")
        curl --ftp-create-dirs -T "$entry" -u ${FTP_USERNAME}:${FTP_PASSWORD} ftp://ftp_hmtt_co_uk@upload.lcn.com/www.hmtt.co.uk/web/reports/cucumber-parallel-testing/$1/$(basename "$entry")
      fi
    done
}

publishReport
publishReport css
publishReport fonts
publishReport js