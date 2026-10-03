import importlib.util
import unittest
from pathlib import Path

spec = importlib.util.spec_from_file_location("ci_matrix", Path(__file__).with_name("ci-matrix.py"))
module = importlib.util.module_from_spec(spec)
spec.loader.exec_module(module)


class MatrixTests(unittest.TestCase):
    def test_catalog(self):
        self.assertEqual(len(module.projects()), 87)
        self.assertIn("testops", module.projects())
        self.assertIn("barber", module.projects())

    def test_changed_project(self):
        self.assertEqual(module.select(["ticketops/frontend/src/App.vue", "README.md"], module.projects()), ["ticketops"])

    def test_shared_changes_require_full_build(self):
        known = module.projects()
        self.assertEqual(module.select(["tools/create-sector.py"], known), known)
        self.assertEqual(module.select(["test-all.sh"], known), known)

    def test_docs_only(self):
        self.assertEqual(module.select(["README.md", "docs/architecture.svg"], module.projects()), [])


if __name__ == "__main__":
    unittest.main()
