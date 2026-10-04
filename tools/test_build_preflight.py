"""The full-catalog build must reject incomplete npm manifests before doing work."""
import os
import shutil
import subprocess
import tempfile
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


class BuildPreflightTests(unittest.TestCase):
    def verify(self, script, client, missing):
        with tempfile.TemporaryDirectory(prefix="aihub-preflight-") as temporary:
            root = Path(temporary)
            shutil.copy2(ROOT / script, root / script)
            for index in range(98):
                (root / f"project-{index:02d}" / "backend").mkdir(parents=True)
                (root / f"project-{index:02d}" / "backend" / "pom.xml").write_text("<project/>")
            client_dir = root / "project-00" / client
            client_dir.mkdir()
            (client_dir / missing).write_text("{}")
            if script.endswith(".ps1"):
                executable = shutil.which("pwsh") or shutil.which("powershell")
                if not executable:
                    self.skipTest("PowerShell unavailable")
                command = [executable, "-NoProfile", "-File", str(root / script)]
            else:
                if os.name == "nt":
                    git = Path(os.environ.get("PROGRAMFILES", r"C:\Program Files")) / "Git"
                    executable = next((str(path) for path in (git / "bin/bash.exe", git / "usr/bin/bash.exe") if path.is_file()), None)
                else:
                    executable = shutil.which("bash")
                if not executable:
                    self.skipTest("Bash unavailable")
                command = [executable, str(root / script)]
            result = subprocess.run(command, text=True, encoding="utf-8", errors="replace", capture_output=True, timeout=30)
            self.assertNotEqual(result.returncode, 0, result.stdout + result.stderr)
            self.assertIn(f"project-00/{client} needs both package.json and package-lock.json", result.stdout + result.stderr)

    def test_bash_rejects_missing_frontend_lock(self):
        self.verify("test-all.sh", "frontend", "package.json")

    def test_bash_rejects_orphan_miniprogram_lock(self):
        self.verify("test-all.sh", "miniprogram", "package-lock.json")

    def test_powershell_rejects_missing_frontend_lock(self):
        self.verify("test-all.ps1", "frontend", "package.json")

    def test_powershell_rejects_orphan_miniprogram_lock(self):
        self.verify("test-all.ps1", "miniprogram", "package-lock.json")


if __name__ == "__main__":
    unittest.main()
