#!/usr/bin/env sh
set -eu
if [ "$#" -gt 1 ]; then
    printf 'Usage: %s [shop|crm]\n' "$0" >&2
    exit 2
fi
project=${1-shop}
case "$project" in
    shop|crm) ;;
    *) printf 'Usage: %s [shop|crm]\n' "$0" >&2; exit 2 ;;
esac
cd "$(dirname "$0")"
exec "./$project/run.sh"
