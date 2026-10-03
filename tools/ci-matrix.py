"""Select runnable ai-hub projects for the GitHub Actions build matrix.

A scheduled/manual run checks every project. Push/PR runs check changed projects,
including mini-program-only projects. Shared build/test tools trigger the full matrix.
"""
import argparse
import json
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SHARED = {".github", "tools", "test-all.ps1", "test-all.sh", "smoke-test.ps1", ".gitattributes", ".gitignore"}


def projects():
    return sorted(path.name for path in ROOT.iterdir() if path.is_dir() and (path / "backend/pom.xml").is_file())


def select(changed, known):
    if any(path.split("/", 1)[0] in SHARED for path in changed):
        return known
    selected = {path.split("/", 1)[0] for path in changed}
    return [name for name in known if name in selected]


def changed_files(base, head):
    if not base or set(base) == {"0"}:
        return None
    result = subprocess.run(
        ["git", "diff", "--name-only", "--no-renames", f"{base}...{head}"],
        cwd=ROOT, text=True, capture_output=True,
    )
    return result.stdout.splitlines() if result.returncode == 0 else None


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--base", default="", help="push before SHA or PR base SHA")
    parser.add_argument("--head", default="HEAD")
    args = parser.parse_args()
    known = projects()
    if len(known) != 98:
        raise SystemExit(f"Expected 98 runnable projects, found {len(known)}; update this guard when the catalog changes")
    changed = changed_files(args.base, args.head)
    chosen = known if changed is None else select(changed, known)
    print("matrix=" + json.dumps({"include": [{"project": name} for name in chosen]}, separators=(",", ":")))
    print("has_projects=" + str(bool(chosen)).lower())
    print(f"Selected {len(chosen)}/{len(known)} projects", file=sys.stderr)


if __name__ == "__main__":
    main()
