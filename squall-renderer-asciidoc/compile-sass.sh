#!/bin/bash

options='--no-charset --no-source-map --style compressed'
sourcedir='src/main/sass'
source="$sourcedir/style.scss"
outdir='src/main/resources/dev/openclosed/squall/renderer/asciidoc/style'

sass $options -I "$sourcedir/default" $source "$outdir/style.css"
sass $options -I "$sourcedir/default-ja" $source "$outdir/style-ja.css"
