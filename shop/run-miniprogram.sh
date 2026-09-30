#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")/miniprogram"
npm ci
npm run build:mp-weixin
printf 'Import miniprogram/dist/build/mp-weixin into WeChat DevTools.\n'
