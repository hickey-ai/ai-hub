"""Guard project READMEs against stale fixed catalog totals."""
import re
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


class ReadmeCatalogCountTests(unittest.TestCase):
    def test_claimed_totals_match_runnable_projects(self):
        projects = [p for p in ROOT.iterdir() if (p / "backend/pom.xml").is_file()]
        for project in projects:
            page = project / "README.md"
            if not page.is_file():
                continue
            for total in re.findall(r"全部\s+(\d+)\s+个项目", page.read_text(encoding="utf-8")):
                with self.subTest(project=project.name):
                    self.assertEqual(int(total), len(projects))


if __name__ == "__main__":
    unittest.main()
