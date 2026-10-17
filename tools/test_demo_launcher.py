"""Exercise demo delegation without installing dependencies or starting a server."""
import os
import shutil
import subprocess
import tempfile
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


class DemoLauncherTests(unittest.TestCase):
    def test_shell_launcher_handles_other_directory_and_propagates_exit_status(self):
        if os.name == "nt":
            git_root = Path(os.environ.get("PROGRAMFILES", r"C:\Program Files")) / "Git"
            shell = next((p for p in (git_root / "bin/bash.exe", git_root / "usr/bin/bash.exe") if p.is_file()), None)
        else:
            shell = shutil.which("sh")
        if not shell:
            self.skipTest("POSIX shell unavailable")
        with tempfile.TemporaryDirectory(prefix="aihub demo ") as temporary:
            root = Path(temporary) / "repository with spaces"
            (root / "shop").mkdir(parents=True)
            launcher = root / "run-demo.sh"
            launcher.write_text((ROOT / "run-demo.sh").read_text(encoding="utf-8"), encoding="utf-8", newline="\n")
            child = root / "shop/run.sh"
            for code in (0, 7):
                with self.subTest(exit_code=code):
                    child.write_text(
                        "#!/usr/bin/env sh\n"
                        "test -f ./run-demo.sh || exit 99\n"
                        "printf 'demo-child\\n'\n"
                        f"exit {code}\n",
                        encoding="utf-8", newline="\n",
                    )
                    child.chmod(0o755)
                    result = subprocess.run(
                        [str(shell), str(launcher)], cwd=temporary,
                        text=True, encoding="utf-8", errors="replace",
                        capture_output=True, timeout=10,
                    )
                    self.assertEqual(result.returncode, code, result.stdout + result.stderr)
                    self.assertEqual(result.stdout.strip(), "demo-child")


if __name__ == "__main__":
    unittest.main()
