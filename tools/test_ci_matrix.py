import importlib.util
import json
import subprocess
import sys
import unittest
from pathlib import Path

spec = importlib.util.spec_from_file_location("ci_matrix", Path(__file__).with_name("ci-matrix.py"))
module = importlib.util.module_from_spec(spec)
spec.loader.exec_module(module)


class MatrixTests(unittest.TestCase):
    def test_catalog(self):
        self.assertEqual(len(module.projects()), 88)
        self.assertIn("testops", module.projects())
        self.assertIn("barber", module.projects())

    def test_changed_project(self):
        self.assertEqual(module.select(["ticketops/frontend/src/App.vue", "README.md"], module.projects()), ["ticketops"])

    def test_shared_changes_require_full_build(self):
        known = module.projects()
        self.assertEqual(module.select(["tools/create-sector.py"], known), known)
        self.assertEqual(module.select(["test-all.sh"], known), known)

    def test_cli_outputs_valid_github_job_values(self):
        result = subprocess.run(
            [sys.executable, str(Path(__file__).with_name("ci-matrix.py")), "--base", "", "--head", "HEAD"],
            text=True, capture_output=True, check=True,
        )
        outputs = dict(line.split("=", 1) for line in result.stdout.splitlines())
        self.assertEqual(outputs["has_projects"], "true")
        self.assertEqual(len(json.loads(outputs["matrix"])["include"]), 88)
    def test_docs_only(self):
        self.assertEqual(module.select(["README.md", "docs/architecture.svg"], module.projects()), [])


if __name__ == "__main__":
    unittest.main()
