"""Keep the bilingual documentation index aligned with the runnable catalog."""
import re
import unittest
from pathlib import Path
from urllib.parse import unquote

ROOT = Path(__file__).resolve().parents[1]
PROJECTS = {p.name for p in ROOT.iterdir() if (p / "backend/pom.xml").is_file()}
CATALOGS = ("docs/project-catalog.md", "docs/project-catalog.en.md")
SCREENSHOTS = ("docs/screenshots.md", "docs/screenshots.en.md")
PAGES = ("README.md", "README.en.md", "docs/README.md", "docs/product-matrix.md", "docs/product-matrix.en.md", "docs/strategic-software-gap.md", "docs/strategic-software-gap.en.md", "docs/strategic-software-design.md", "docs/strategic-software-design.en.md", *CATALOGS, *SCREENSHOTS)


class DocumentationTests(unittest.TestCase):
    def test_exactly_one_category_per_project_in_both_languages(self):
        self.assertEqual(len(PROJECTS), 102)
        for name in CATALOGS:
            with self.subTest(name=name):
                text = (ROOT / name).read_text(encoding="utf-8")
                sections = re.findall(r'<a id="([a-z]+)"></a>\n## [^\n]+ · (\d+)\n(.*?)(?=<a id=|## How to|## 如何使用)', text, re.S)
                self.assertEqual(len(sections), 12)
                projects = []
                for anchor, expected, body in sections:
                    self.assertIn(f"(#{anchor})", text)
                    found = re.findall(r'\]\(\.\./([^/]+)/README\.md\)', body)
                    self.assertEqual(len(found), int(expected), anchor)
                    projects.extend(found)
                self.assertEqual(len(projects), len(set(projects)))
                self.assertEqual(set(projects), PROJECTS)

    def test_local_documentation_links_and_images_exist(self):
        for name in PAGES:
            page = ROOT / name
            text = page.read_text(encoding="utf-8")
            links = re.findall(r'\]\(([^)]+)\)|(?:src|href)="([^"]+)"', text)
            for markdown_link, html_link in links:
                link = markdown_link or html_link
                if link.startswith(("http:", "https:", "mailto:", "#")):
                    continue
                target = unquote(link.split("#", 1)[0])
                if not target:
                    continue
                with self.subTest(page=name, target=target):
                    self.assertTrue((page.parent / target).exists())

    def test_every_project_screenshot_is_indexed(self):
        actual = {p.relative_to(ROOT).as_posix() for p in ROOT.glob("*/screenshots/*.png")}
        for name in SCREENSHOTS:
            with self.subTest(name=name):
                text = (ROOT / name).read_text(encoding="utf-8")
                projects = re.findall(r'^\| \[([a-z0-9]+)\]\(\.\./[^/]+/README\.md\)', text, re.M)
                self.assertEqual(set(projects), PROJECTS)
                self.assertEqual(len(projects), len(PROJECTS))
                indexed = {p.removeprefix("../") for p in re.findall(r'\]\((\.\./[^)]+/screenshots/[^)]+\.png)\)', text)}
                self.assertEqual(indexed, actual)

    def test_independent_projects_have_unique_default_ports(self):
        owners = {}
        for project in sorted(PROJECTS):
            config = ROOT / project / "backend/src/main/resources/application.properties"
            self.assertTrue(config.is_file(), project)
            matches = re.findall(r"^server\.port=(\d{4})$", config.read_text(encoding="utf-8"), re.M)
            self.assertEqual(len(matches), 1, project)
            port = matches[0]
            self.assertNotIn(port, owners, f"{project} and {owners.get(port)} both use port {port}")
            owners[port] = project

    def test_catalog_ports_match_backend_configuration(self):
        for name in CATALOGS:
            text = (ROOT / name).read_text(encoding="utf-8")
            for project, port in re.findall(r'\]\(\.\./([^/]+)/README\.md\).*?\| `(8\d{3})` \|', text):
                files = (ROOT / project / "backend/src/main/resources").glob("application.*")
                actual = set()
                for file in files:
                    content = file.read_text(encoding="utf-8")
                    actual.update(re.findall(r'(?:server\.port\s*=|port:\s*)(\d{4})', content))
                with self.subTest(catalog=name, project=project):
                    self.assertIn(port, actual)


if __name__ == "__main__":
    unittest.main()
