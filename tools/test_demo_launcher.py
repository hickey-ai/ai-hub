"""Exercise demo delegation without installing dependencies or starting a server."""
import os
import shutil
import subprocess
import tempfile
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


class DemoLauncherTests(unittest.TestCase):
    def verify(self, script, executable):
        with tempfile.TemporaryDirectory(prefix="aihub demo ") as temporary:
            root = Path(temporary) / "repository with spaces"
            root.mkdir()
            launcher = root / script
            launcher.write_text((ROOT / script).read_text(encoding="utf-8"), encoding="utf-8", newline="\n")
            for project in ("shop", "crm"):
                (root / project).mkdir()
            for arguments, project in (([], "shop"), (["shop"], "shop"), (["crm"], "crm")):
                for code in (0, 7):
                    with self.subTest(script=script, arguments=arguments, exit_code=code):
                        for child_project in ("shop", "crm"):
                            child = root / child_project / ("run.ps1" if script.endswith(".ps1") else "run.sh")
                            content = (
                                'if (!(Test-Path ./run-demo.ps1)) { exit 99 }\n'
                                f"Write-Output '{child_project}-child'\nexit {code}\n"
                                if script.endswith(".ps1") else
                                '#!/usr/bin/env sh\ntest -f ./run-demo.sh || exit 99\n'
                                f"printf '{child_project}-child\\n'\nexit {code}\n"
                            )
                            child.write_text(content, encoding="utf-8", newline="\n")
                            child.chmod(0o755)
                        result = self.run_launcher(executable, launcher, arguments, temporary)
                        self.assertEqual(result.returncode == 0, code == 0, result.stdout + result.stderr)
                        if not script.endswith(".ps1"):
                            self.assertEqual(result.returncode, code)
                        self.assertEqual(result.stdout.strip(), project + "-child")
            for arguments in (["unknown"], ["../shop"], ["crm", "shop"]):
                with self.subTest(script=script, arguments=arguments):
                    result = self.run_launcher(executable, launcher, arguments, temporary)
                    self.assertNotEqual(result.returncode, 0, result.stdout + result.stderr)
                    self.assertNotIn("-child", result.stdout)

    def run_launcher(self, executable, launcher, arguments, cwd):
        command = [str(executable)]
        if launcher.suffix == ".ps1":
            command += ["-NoProfile", "-File"]
        return subprocess.run(command + [str(launcher), *arguments], cwd=cwd,
                              text=True, encoding="utf-8", errors="replace",
                              capture_output=True, timeout=15)

    def test_shell_project_selection_and_exit_status(self):
        if os.name == "nt":
            git_root = Path(os.environ.get("PROGRAMFILES", r"C:\Program Files")) / "Git"
            shell = next((p for p in (git_root / "bin/bash.exe", git_root / "usr/bin/bash.exe") if p.is_file()), None)
        else:
            shell = shutil.which("sh")
        if not shell:
            self.skipTest("POSIX shell unavailable")
        self.verify("run-demo.sh", shell)

    def test_powershell_project_selection_and_exit_status(self):
        shell = shutil.which("pwsh") or shutil.which("powershell")
        if not shell:
            self.skipTest("PowerShell unavailable")
        self.verify("run-demo.ps1", shell)


if __name__ == "__main__":
    unittest.main()
